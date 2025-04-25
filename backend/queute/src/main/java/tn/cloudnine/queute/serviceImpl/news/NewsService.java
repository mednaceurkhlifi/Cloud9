package tn.cloudnine.queute.serviceImpl.news;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.cloudnine.queute.service.news.INewsService;
import tn.cloudnine.queute.model.newsAndOffers.news.News;
import tn.cloudnine.queute.enums.newsAndOffers.NewsStatus;
import tn.cloudnine.queute.dto.newsDTO.MonthlyStatsDTO;
import tn.cloudnine.queute.repository.news.NewsRepository;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class NewsService implements INewsService {

    @Autowired
    private NewsRepository newsRepository;

    @Override
    public News addNews(News news) {
        System.out.println(news );
    return newsRepository.save(news);
    }

    @Override
    public List<News> getAllNews() {
         return newsRepository.findAll();
    }

    @Override
    public News getNewsById(Long id) {
        return newsRepository.findById(id).orElse(new News());
    }

    @Override
    public List<News> getNewsByOrg(Long orgId) {
       // return newsRepository.findAllByOrganisation_OrganisationId(orgId);
        return newsRepository.findAllByOrganisation_OrganizationId(orgId);

    }

    @Override
    public News updateNews(News news) {
        return newsRepository.save(news);
    }

    @Override
    public void deleteNews(Long id) {
        newsRepository.deleteById(id);
    }

    @Override
    public News updateNewsStatus(Long newsId, NewsStatus status) {
        News n=newsRepository.findById(newsId).orElse(new News());
        n.setStatus(status);
        return newsRepository.save(n);
    }

    @Override
    public List<MonthlyStatsDTO> getMonthlyStats(Long orgId) {
       List< MonthlyStatsDTO> stats=new ArrayList<>();
        List<Object[]> actions=newsRepository.getNumberOfActions(orgId);

        for (Object[] row : actions) {
            MonthlyStatsDTO stat = new MonthlyStatsDTO();
            stat.setTotalActions((Long) row[0]);

            Integer month = (Integer) row[1];
            Integer year = (Integer) row[2];

            // Convert to month name
            Month monthEnum = Month.of(month); // 1 = January, 12 = December
            String monthName = monthEnum.getDisplayName(TextStyle.FULL, Locale.ENGLISH);

            stat.setMonth(monthName); // Example: "March"
            stat.setYear(year.toString());
            stat.setTotalNews(newsRepository.getNumberNews(orgId));

            stats.add(stat);
        }
        System.out.println(stats);
        return stats;
    }

}
