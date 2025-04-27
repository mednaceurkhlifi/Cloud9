package tn.cloudnine.queute.service.user;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import tn.cloudnine.queute.model.user.User;

import java.util.List;

@Repository
public interface IUserService {
    User addUser(User user);
    User updateUser(Long id,User user);
    User retrieveUser(Long id);
    List<User> retrieveAll();
    void deleteUser(Long id);
    User findByEmail(String email);
    ResponseEntity<String> findByEmailAndPassword(String email,String password);
    String updatePWDUser(Long userid,String oldPwd,String pwd);
    public String resetpwd(String email);
    String resetPasswordWithToken(String token, String newPassword);

//    public User create(User user);
//    public User update(User user);
//    public User delete(long id);
//    public User findById(Long id);
//    public List<User> findAll();
}
