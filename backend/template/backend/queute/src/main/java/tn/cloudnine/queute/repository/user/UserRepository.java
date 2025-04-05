package tn.cloudnine.queute.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.cloudnine.queute.model.user.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
