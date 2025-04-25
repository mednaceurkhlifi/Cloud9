package tn.cloudnine.queute.serviceImpl.news;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import tn.cloudnine.queute.service.news.ITrendingService;
import tn.cloudnine.queute.model.newsAndOffers.news.News;
import tn.cloudnine.queute.model.newsAndOffers.news.Trending;
import tn.cloudnine.queute.repository.news.ITrendingRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TrendingService implements ITrendingService {

    LocalDate today= LocalDate.now();
    LocalDate monday=today.minusDays(today.getDayOfWeek().getValue()-1);
    LocalDate sunday=today.plusDays(7-today.getDayOfWeek().getValue());
    public static final Map<String,Integer> scoreMap=Map.of(
            "visit",20,
            "like", 30,
            "heart", 40,
            "wowemoji", 23
    );
    @Autowired
    ITrendingRepository trendingRepository;
    @Autowired
    private ConversionService conversionService;

    @Override
    public Optional<Trending> createTrending(Trending trending) {
         boolean shouldSave=true;
       String type=trending.getType().toLowerCase();

       if(type.equals("visit")){
           Trending exists=trendingRepository.findByUser_UserIdAndNews_NewsIdAndTypeAndDateBetween(trending.getUser().getUserId(),trending.getNews().getNewsId(),"visit",monday,sunday);

           if (exists ==null || exists.getDate().isBefore(monday) || exists.getDate().isAfter(sunday)) {
               trending.setScore(scoreMap.get("visit"));
           }

           else
               shouldSave = false;

       }

       else {

           String typeReturned=getType(trending.getNews().getNewsId(),trending.getUser().getUserId());
           int state=changeActiveState(trending.getNews().getNewsId(), trending.getUser().getUserId(), trending.getType());
           System.out.println("Returned Type : "+typeReturned);
           System.out.println("Type"+type);

           if(state!=0) {
               if (typeReturned.equalsIgnoreCase(type))
                   shouldSave = false;

           }

       }
       if(shouldSave) {
           scoreMap.entrySet().stream()
                   .filter(entry -> type.contains(entry.getKey()))
                   .map(Map.Entry::getValue)
                   .findFirst().ifPresent(trending::setScore);
           trending.setDate(today);
           trending.setActive("true");
           return Optional.of(trendingRepository.save(trending));
       }
       else return Optional.empty();
    }



    @Override
    public List<News> getTrendings() {

        List<Object[]> results = trendingRepository.countScoresGroupedByNews(monday, sunday);
        List<News> news=new ArrayList<>();
        for (Object[] row : results) {
            News n = (News) row[0];
            Long score = (Long) row[1];
            news.add(n);

        }
        if(news.size()<5)
        return news;
        else return news.subList(0,5);

    }

    @Override
    public Trending getTrendingById(Long id) {
        return null;
    }

    @Override
    public int changeActiveState(Long newsId, Long userId, String type) {
       return trendingRepository.changeActiveState(newsId, userId,type);
    }


    @Override
    public Trending updateTrending(Trending trending) {
        return null;
    }

    @Override
    public String getType(Long newsId, Long userId) {
        return trendingRepository.getType(newsId,userId);
    }
}
