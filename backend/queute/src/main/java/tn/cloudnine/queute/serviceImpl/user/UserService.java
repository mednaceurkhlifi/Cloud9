package tn.cloudnine.queute.serviceImpl.user;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tn.cloudnine.queute.model.forum.Post;
import tn.cloudnine.queute.model.user.User;
import tn.cloudnine.queute.repository.forum.PostRepository;
import tn.cloudnine.queute.repository.user.UserRepository;
import tn.cloudnine.queute.service.user.IUserService;

import java.util.List;
@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;

    @Override
    public User addUser(User user) {
        return null;
    }

    @Override
    public User updateUser(Long id, User user) {
        return null;
    }

    @Override
    public User retrieveUser(Long id) {
        return null;
    }

    @Override
    public List<User> retrieveAll() {
        return List.of();
    }

    @Override
    public void deleteUser(Long id) {

    }

    @Override
    public User findByEmail(String email) {
        return null;
    }

    @Override
    public ResponseEntity<String> findByEmailAndPassword(String email, String password) {
        return null;
    }

    @Override
    public String updatePWDUser(Long userid, String oldPwd, String pwd) {
        return "";
    }

    @Override
    public String resetpwd(String email) {
        return "";
    }

    @Override
    public String resetPasswordWithToken(String token, String newPassword) {
        return "";
    }

//    @Override
//    public User create(User user) {
//        //TODO: add user to the users list
//        return userRepository.save(user);
//    }
//
//    @Override
//    public User update(User user) {
//        //TODO: update user to the users list
//        User oldUser = findById(user.getUserId());
//        return userRepository.save(oldUser);
//    }
//
//    @Override
//    public User delete(long id) {
//        //TODO: remove user from the users list
//        try{
//            User user = findById(id);
//            userRepository.deleteById(id);
//            return user;
//        }catch (EntityNotFoundException e){
//            //TODO: do some abt this
//            return null;
//        }
//    }
//
//    @Override
//    public User findById(Long id) {
//        User user = userRepository.findById(id).orElseThrow(()->new EntityNotFoundException("User not found with id :"+id));
//        return user;
//    }
//
//    @Override
//    public List<User> findAll() {
//        return userRepository.findAll();
//    }
}
