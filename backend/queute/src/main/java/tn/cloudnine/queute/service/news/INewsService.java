package tn.cloudnine.queute.service.news;

import tn.cloudnine.queute.model.newsAndOffers.news.News;
import tn.cloudnine.queute.enums.newsAndOffers.NewsStatus;
import tn.cloudnine.queute.dto.newsDTO.MonthlyStatsDTO;

import java.util.List;

public interface INewsService {

    public News addNews(News news);
    public List<News> getAllNews();
    public News getNewsById(Long id);
    public List<News> getNewsByOrg(Long orgId);
    public News updateNews(News news);
    public void deleteNews(Long id);
    public News updateNewsStatus(Long newsId, NewsStatus status);
    public List<MonthlyStatsDTO> getMonthlyStats(Long orgId);

}
