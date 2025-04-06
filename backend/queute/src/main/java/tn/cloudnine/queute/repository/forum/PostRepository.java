package tn.cloudnine.queute.repository.forum;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.cloudnine.queute.model.forum.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
}
