package tn.cloudnine.queute.service.news;

import tn.cloudnine.queute.model.newsAndOffers.news.ReadLater;
import tn.cloudnine.queute.enums.newsAndOffers.SavedStatus;

import java.util.List;

public interface IReadLaterService {

    public ReadLater addReadLater(ReadLater readLater);
    public List<ReadLater> getAllReadLater();
    public List<ReadLater> getSavedReadLater(Long idUser, SavedStatus status);
    public ReadLater getSavedReadLaterChecked(Long idNews, Long idUser);
    public void deleteReadLater(Long id);
    public  void deleteReadLaterByUserAndNews(Long idUser, Long idNews);


}
