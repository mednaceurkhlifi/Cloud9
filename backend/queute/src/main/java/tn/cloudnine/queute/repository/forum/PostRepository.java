package tn.cloudnine.queute.repository.forum;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.cloudnine.queute.enums.forum.SentimentType;
import tn.cloudnine.queute.model.forum.Post;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    public List<Post> getPostsBySentimentType(SentimentType sentimentType);
}
