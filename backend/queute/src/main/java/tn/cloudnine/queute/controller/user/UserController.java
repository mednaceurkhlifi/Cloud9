package tn.cloudnine.queute.controller.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import tn.cloudnine.queute.config.user.UserToUserDetails;
import tn.cloudnine.queute.model.user.LoginRequest;
import tn.cloudnine.queute.model.user.User;
import tn.cloudnine.queute.repository.user.UserRepository;
import tn.cloudnine.queute.service.user.JwtService;
import tn.cloudnine.queute.service.user.UserServiceImpl;

import java.util.List;
import java.util.Map;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/User")
public class UserController {
    private final UserServiceImpl userService;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    public UserController(UserServiceImpl userService,
                          UserRepository userRepository,
                          JwtService jwtService,
                          PasswordEncoder passwordEncoder)
    {this.userService=userService;
    this.jwtService=jwtService;
    this.userRepository=userRepository;
    this.passwordEncoder = passwordEncoder;
    }
    @PostMapping("/addUser")
    public ResponseEntity<?> addUser(@RequestBody User user) {
        logger.info("Received user creation request for email: {}", user.getEmail());

        if ( user.getPassword() == null || user.getBirthDate() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing required fields!");
        }

        try {
            User savedUser = userService.addUser(user);
            return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving user.");
        }
    }
    @PutMapping("/updateUser/{id}")
    public User updateUser(@PathVariable Long id,@RequestBody User user){
        return userService.updateUser(id,user);
    }
    @GetMapping("/retrieveUser/{id}")
    public User retrieveUser(@PathVariable Long id){
        return userService.retrieveUser(id);
    }
    @GetMapping("/deleteUser/{id}")
    public void deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
    }
    @GetMapping("/retrieveAll")
    public List<User> retrieveAll(){
        return userService.retrieveAll();
    }

    @GetMapping("/oauth2/signup")
    public ResponseEntity<String> signupSuccess(@AuthenticationPrincipal OAuth2User oauth2User) {
        return ResponseEntity.ok("User signed up as: " + oauth2User.getAttribute("email"));
    }
    @PostMapping("/sign-in")
    public ResponseEntity <Map<String,String>> singIn(@RequestBody LoginRequest loginRequest)
    {
        UserDetails userDetails = userService.loadUserByUsername(loginRequest.getEmail());
        UserToUserDetails connected = (UserToUserDetails) userDetails;
        if(passwordEncoder.matches(loginRequest.getPassword(), userDetails.getPassword())){
            String token = jwtService.generateToken(userDetails.getAuthorities().toString(),
                    userDetails.getUsername(), connected.getUserId(), connected.getOrganizationId());
            logger.info(token);
            return ResponseEntity.ok(Map.of("token",token));}
        else return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "Invalid email or password"));

    }
    @GetMapping("/findByEmail/{email}")
    public User findByEmail(@PathVariable String email)
    {return userService.findByEmail(email);}

    @PostMapping("/reset-password-request")
    public ResponseEntity<String> resetPasswordRequest(@RequestParam String email)
    {     logger.info("Received forgot password request for email: " + email);
        String result = userService.resetpwd(email);
        return ResponseEntity.ok(result);
    }
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String token,@RequestParam String newPassword)
    {String result=userService.resetPasswordWithToken(token,newPassword);
        return ResponseEntity.ok(result);
    }
    @GetMapping("/protected-endpoint")
    public ResponseEntity<String> protectedEndpoint(@AuthenticationPrincipal UserToUserDetails userDetails) {
        return ResponseEntity.ok("Hello " + userDetails.getUsername());
    }

    @GetMapping("/current")
    public ResponseEntity<User> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                logger.error("Invalid authorization header: {}", authHeader);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }

            String token = authHeader.substring(7); // Remove "Bearer "
            logger.info("Received token: {}", token);

            String email = jwtService.extractUsername(token);
            logger.info("Extracted email: {}", email);

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            return ResponseEntity.ok(user);
        } catch (Exception e) {
            logger.error("Error in current user endpoint: ", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }



}