package tn.cloudnine.queute.controller.news;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.cloudnine.queute.model.newsAndOffers.news.News;
import tn.cloudnine.queute.model.newsAndOffers.news.Trending;
import tn.cloudnine.queute.service.news.ITrendingService;

import java.util.List;

@RestController
@RequestMapping("/trending")
@CrossOrigin(origins = "*")
public class TrendingController {

    @Autowired
    private ITrendingService trendingService;

    @PostMapping("/addTrending")
    public Trending addTrending(@RequestBody Trending trending) {
        return trendingService.createTrending(trending).orElse(new Trending());
    }

    @GetMapping("getTrending")
    public List<News> getTrending() {
        return trendingService.getTrendings();
    }

    /*@PutMapping ("/changeActiveState")
    public void changeActiveState(@RequestParam Long newsId, @RequestParam Long userId) {
    trendingService.changeActiveState(newsId,userId);
    }*/
}
