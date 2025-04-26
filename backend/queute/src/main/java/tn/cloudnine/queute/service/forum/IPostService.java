package tn.cloudnine.queute.service.forum;

import tn.cloudnine.queute.dto.forum.PostCountDTO;
import tn.cloudnine.queute.dto.forum.PostDTO;
import tn.cloudnine.queute.enums.forum.SentimentType;
import tn.cloudnine.queute.model.forum.Post;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface IPostService {
    public Post create(Post post);
    public Post update(Post post);
    public Post delete(long id);
    public Post findById(Long id);
    public List<Post> findAll();
    public List<Post> findBySentimentType(SentimentType sentimentType);
    public List<PostCountDTO> countPostsByDate();
    public PostDTO findTopPostBetween(LocalDateTime startDate, LocalDateTime endDate);
}
