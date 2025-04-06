package tn.cloudnine.queute.service.forum;

import tn.cloudnine.queute.model.forum.Comment;
import tn.cloudnine.queute.model.forum.Vote;

import java.util.List;

public interface IVoteService {

    public Vote create(Vote vote);
    public Vote update(Vote vote);
    public Vote delete(long id);
    public Vote findById(Long id);
    public List<Vote> findAll();
}
