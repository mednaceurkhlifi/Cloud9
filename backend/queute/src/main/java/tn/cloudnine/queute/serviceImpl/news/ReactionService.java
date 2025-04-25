package tn.cloudnine.queute.serviceImpl.news;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.cloudnine.queute.service.news.IReactionService;
import tn.cloudnine.queute.model.newsAndOffers.news.Reaction;
import tn.cloudnine.queute.repository.news.IReactionRepository;

import java.util.List;
@Service
public class ReactionService implements IReactionService {

    @Autowired
    private IReactionRepository reactionRepository;
    @Override
    public Reaction addReaction(Reaction reaction) {
        return reactionRepository.save(reaction);
    }

    @Override
    public List<Reaction> getAllReaction() {
        return reactionRepository.findAll();
    }

    @Override
    public Reaction getReactionById(Long id) {
        return reactionRepository.findById(id).orElse(new Reaction());
    }

    @Override
    public List<Reaction> getReactionByUser(Long userId) {
        return reactionRepository.findReactionByUser_UserId(userId);
    }

    @Override
    public Reaction updateReaction(Reaction reaction) {
        return reactionRepository.save(reaction);
    }

    @Override
    public void deleteReaction(Long id) {
    reactionRepository.deleteById(id);
    }

    @Override
    public Reaction getReactionByUserAndNews(Long userId, Long newsId) {
        return reactionRepository.findReactionByUser_UserIdAndNews_NewsId(userId, newsId);
    }

    @Override
    public List<Reaction> getAllReactionByNewsId(Long newsId) {
        return reactionRepository.findAllByNews_NewsId(newsId);
    }


}
