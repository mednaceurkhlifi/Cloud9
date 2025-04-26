package tn.cloudnine.queute.service.user;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tn.cloudnine.queute.config.user.UserToUserDetails;
import tn.cloudnine.queute.enums.roles.Role;
import tn.cloudnine.queute.model.user.User;
import tn.cloudnine.queute.repository.user.UserRepository;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
public class UserServiceImpl implements IUserService, UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender emailSender;

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    public UserServiceImpl(
            PasswordEncoder passwordEncoder,
            UserRepository userRepository,
            JavaMailSender emailSender)
    {this.passwordEncoder=passwordEncoder;
        this.userRepository=userRepository;
        this.emailSender=emailSender;
    }
    @Override
    public User addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Random random = new Random();
        int randomInt = 1000 + random.nextInt(9000);
        user.setLogin(user.getFirstName().toLowerCase()+"-"+randomInt);
        user.setRole(Role.ADMIN);
        user.setEnabled(true);
        user.setImage("default_user.jpg");
        user.setFullName(user.getLastName() + " " + user.getFirstName());
        user.setCreated_at(LocalDateTime.now());
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long id, User user) {
        User userExisting = userRepository.findById(id).orElseThrow(()->
                new RuntimeException("User Not Found with ID : "+ id));
        userExisting.setFirstName(user.getFirstName());
        userExisting.setLastName(user.getLastName());
        userExisting.setBirthDate(user.getBirthDate());
        userExisting.setLogin(user.getLogin());
        userExisting.setRole(user.getRole());
        userExisting.setAddress(user.getAddress());
        userExisting.setPhone_number(user.getPhone_number());

        return userRepository.save(userExisting);
    }

    @Override
    public User retrieveUser(Long id) {
        return userRepository.findById(id).
                orElseThrow(() -> new RuntimeException("User " + id + " not found."));    }

    @Override
    public List<User> retrieveAll() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }


    @Override
    public ResponseEntity<String> findByEmailAndPassword(String email, String password) {
        User user = userRepository.findByEmail(email).orElse(null);

        if(user == null)
        {return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
        if(!passwordEncoder.matches(password, user.getPassword()))
        {return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");}
        if(!user.isEnabled())
        {return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Account is disabled please contact admin");}
        if(user.getRole().equals(Role.ADMIN))
        {return ResponseEntity.status(HttpStatus.OK).body("Welcome Admin");}
        else{return ResponseEntity.status(HttpStatus.OK).body("Welcome Client");}

    }

    @Override
    public String updatePWDUser(Long userid, String oldPwd, String pwd) {
        Optional<User> optionalUser = userRepository.findById(userid);
        if(optionalUser.isPresent()){
            User user =optionalUser.get();
            if (passwordEncoder.matches(oldPwd, user.getPassword())) {
                user.setPassword(passwordEncoder.encode(pwd));
                userRepository.save(user);
                return "Password updated successfully";}
        }
        return "Incorrect password";

    }

    @Override
    public String resetpwd(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isEmpty())
            return "If an account with that email exists, a reset link has been sent." ;


        User user = userOptional.get();
        if (user.getResetToken() != null && user.getTokenExpiration() != null &&
                user.getTokenExpiration().isAfter(LocalDateTime.now())) {
            return "A reset link has already been sent. Please check your email.";
        }
        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        user.setTokenExpiration(LocalDateTime.now().plusHours(1));
        userRepository.save(user);

        sendEmail(user.getEmail(), token, user);
        return "Password reset link sent to your email.";

    }

    @Override
    public String resetPasswordWithToken(String token, String newPassword) {
        Optional<User> optionalUser = userRepository.findByResetToken(token);
        if (optionalUser.isEmpty()) return "Invalid token";

        User user = optionalUser.get();
        if (user.getTokenExpiration().isBefore(LocalDateTime.now())) {
            return "Token expired";
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        user.setTokenExpiration(null);
        userRepository.save(user);

        return "Password successfully reset";
    }
    public void sendEmail(String recipient, String token, User user) {
        try {
            logger.info("Attempting to send email to: " + recipient);
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,true);
            helper.setFrom("no-reply@quete.com", "Queute-App");
            helper.setTo(recipient);
            helper.setSubject("Reset Your Password");

            String resetUrl = "http://localhost:4200/auth/reset-password?token=" + token;
            String content = "<p>Hi " + user.getFirstName() + ",</p>"
                    + "<p>You requested a password reset. Click the link below to reset your password:</p>"
                    + "<p><a href=\"" + resetUrl + "\">Reset Password</a></p>"
                    + "<br><p>If you did not request this, please ignore this email.</p>";
            helper.setText(content,true);
            emailSender.send(message);
            logger.info("Email sent successfully to: " + recipient); // Add this
        } catch (MessagingException e)
        {
            logger.error("Failed to send reset email to: " + recipient, e); // Include recipient in log
            throw new RuntimeException("Failed to send password reset email to: " + recipient, e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(username);

        return user.map(UserToUserDetails::new)
                .orElseThrow(()-> new UsernameNotFoundException("user not found " + username));

    }




}