package tn.cloudnine.queute.service.user;

import tn.cloudnine.queute.model.forum.Vote;
import tn.cloudnine.queute.model.user.User;

import java.util.List;

public interface IUserService {

    public User create(User user);
    public User update(User user);
    public User delete(long id);
    public User findById(Long id);
    public List<User> findAll();
}
