package tn.cloudnine.queute.repository.forum;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.cloudnine.queute.model.forum.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
