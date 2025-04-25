package tn.cloudnine.queute.serviceImpl.news;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.cloudnine.queute.service.news.IReadLaterService;
import tn.cloudnine.queute.model.newsAndOffers.news.ReadLater;
import tn.cloudnine.queute.enums.newsAndOffers.SavedStatus;
import tn.cloudnine.queute.repository.news.IReadLaterRepository;

import java.util.List;

@Service
public class ReadLaterService implements IReadLaterService {

    @Autowired
    private IReadLaterRepository repository;
    @Override
    public ReadLater addReadLater(ReadLater readLater) {
        return repository.save(readLater);
    }

    @Override
    public List<ReadLater> getAllReadLater() {
        return repository.findAll();
    }

    @Override
    public List<ReadLater> getSavedReadLater(Long idUser,SavedStatus status) {
        return repository.findAllByUser_UserIdAndStatus(idUser,status);

    }

    @Override
    public ReadLater getSavedReadLaterChecked(Long idNews, Long idUser) {
        return repository.findByNews_NewsIdAndUser_UserId(idNews,idUser);

    }

    @Override
    public void deleteReadLater(Long id) {
         repository.deleteById(id);
    }
@Override
@Transactional //khater custom method elli bech ta3mel maj
    public  void deleteReadLaterByUserAndNews(Long idUser, Long idNews)
{
    repository.deleteByUser_UserIdAndNews_NewsId(idUser,idNews);
}

}
