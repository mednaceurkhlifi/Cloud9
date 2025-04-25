package tn.cloudnine.queute.service.news;

import tn.cloudnine.queute.model.newsAndOffers.news.News;
import tn.cloudnine.queute.model.newsAndOffers.news.Trending;

import java.util.List;
import java.util.Optional;

public interface ITrendingService {
    public Optional<Trending> createTrending(Trending trending);
    public List<News> getTrendings();
    public Trending getTrendingById(Long id);
    public int changeActiveState(Long newsId, Long userId, String type);
    public Trending updateTrending(Trending trending);
    public String getType(Long newsId, Long userId);
}
