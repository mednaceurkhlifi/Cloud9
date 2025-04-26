package tn.cloudnine.queute.serviceImpl.forum;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.cloudnine.queute.model.forum.Vote;
import tn.cloudnine.queute.repository.forum.VoteRepository;
import tn.cloudnine.queute.service.forum.IVoteService;
import tn.cloudnine.queute.service.user.IUserService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VoteService implements IVoteService {

    private final VoteRepository voteRepository;
    private final IUserService userService;
    @Override
    public Vote create(Vote vote) {
        //TODO: add vote to the users list
        var user = userService.findById(vote.getUser().getUserId());
        vote.setUser(user);
        return voteRepository.save(vote);
    }

    @Override
    public Vote update(Vote vote) {
        //TODO: update vote to the users list
        Vote oldVote = findById(vote.getId());
        oldVote.setVoteType(vote.getVoteType());

        return voteRepository.save(oldVote);
    }

    @Override
    public Vote delete(long id) {
        //TODO: remove vote from the users list
        try{
            Vote vote = findById(id);
            voteRepository.deleteById(id);
            return vote;
        }catch (EntityNotFoundException e){
            return null;
        }
    }

    @Override
    public Vote findById(Long id) {
        Vote vote = voteRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Vote not found with id :"+id));
        return vote;
    }

    @Override
    public List<Vote> findAll() {
        return voteRepository.findAll();
    }

    @Override
    public Vote findByUserIdAndPostId(Long userId, Long postId) {
        var votes = voteRepository.findByVotable_Id(postId).orElse(new ArrayList<>());
        Vote vote = votes.stream().filter(v -> v.getUser().getUserId().equals(userId)).findFirst().orElse(null);
        return vote;
    }


}
