package tn.cloudnine.queute.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.cloudnine.queute.model.user.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailEquals(String email);
    Optional <User> findByEmail(String emailAddress);
    Optional<User> findByResetToken(String token);
}
