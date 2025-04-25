package tn.cloudnine.queute.service.news;

import tn.cloudnine.queute.model.newsAndOffers.news.Reaction;

import java.util.List;

public interface IReactionService {

    public Reaction addReaction(Reaction reaction);
    public List<Reaction> getAllReaction();
    public Reaction getReactionById(Long id);
    public List<Reaction> getReactionByUser(Long userId);
    public Reaction updateReaction(Reaction reaction);
    public void deleteReaction(Long id);
    public Reaction getReactionByUserAndNews(Long userId, Long newsId);
    public List<Reaction> getAllReactionByNewsId(Long newsId);
}
