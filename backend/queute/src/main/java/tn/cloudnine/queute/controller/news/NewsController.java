package tn.cloudnine.queute.controller.news;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.cloudnine.queute.dto.newsDTO.MonthlyStatsDTO;
import tn.cloudnine.queute.enums.newsAndOffers.ArticleCategory;
import tn.cloudnine.queute.enums.newsAndOffers.ArticleType;
import tn.cloudnine.queute.enums.newsAndOffers.NewsStatus;
import tn.cloudnine.queute.enums.newsAndOffers.SavedStatus;
import tn.cloudnine.queute.model.newsAndOffers.news.News;
import tn.cloudnine.queute.model.newsAndOffers.news.ReadLater;
import tn.cloudnine.queute.serviceImpl.news.GeminiService;
import tn.cloudnine.queute.service.news.INewsService;
import tn.cloudnine.queute.service.news.IReadLaterService;
import tn.cloudnine.queute.utils.IFileUploader;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/news")
public class NewsController {
    @Autowired
    private  IFileUploader fileUploader;
    private  String DEFAULT_IMAGE = "default_news.jpg";
    @Autowired
    INewsService newsService;
    @Autowired
    IReadLaterService readLaterService;

    @Autowired
    private GeminiService geminiService;


    @PostMapping(value = "/addNews",consumes = "multipart/form-data")
    public News addNews(@RequestPart News news, @RequestPart(value = "image", required = false) MultipartFile image)

    {
        System.out.println("yo");
        System.out.println(news);
        if (image != null && !image.isEmpty()) {
            news.setImage(fileUploader.saveImage(image));
        } else {
            news.setImage(DEFAULT_IMAGE);
        }
        return newsService.addNews(news);
    }
    @GetMapping("/images/{filename}")
    public ResponseEntity<Resource> getImage(@PathVariable("filename") String filename) {
        return fileUploader.serveFile("images", filename, "image/jpeg");
    }
    @GetMapping("/getStats/{orgId}")
    public List<MonthlyStatsDTO> getStats(@PathVariable    Long orgId) {
        return newsService.getMonthlyStats(orgId);
    }
    @GetMapping("/getAll")
    public List<News> getAllNews()
    {
        return newsService.getAllNews();

    }

    @PostMapping("/generateNews")
    public String generateArticle(@RequestBody String description) {
        // Call the generateContent method from GeminiService and return the generated content
        String generatedContent = geminiService.generateContent(description);
        return generatedContent;
    }

    @GetMapping("/get/{newsId}")
    public News getAllNews(@PathVariable Long newsId)
    {
        return newsService.getNewsById(newsId);
    }

    @GetMapping("getOrganisationNews")
    public List<News> getOrganisatioNews(@RequestParam Long orgId)
    {
        return newsService.getNewsByOrg(orgId);

    }
    @PutMapping("/changeNewsStatus")
    public News changeNewsStatus(@RequestParam Long newsId, @RequestParam NewsStatus status)
    {
        return newsService.updateNewsStatus(newsId, status);
    }
    @PutMapping(value = "/update",consumes = "multipart/form-data")
    public News updateNews(@RequestPart News news,@RequestPart(value = "image", required = false) MultipartFile image)
    {
        if (image != null && !image.isEmpty()) {
            news.setImage(fileUploader.saveImage(image));
        }
        return newsService.updateNews(news);

    }

    @DeleteMapping("/delete/{newsId}")
    public void deleteNews(@PathVariable Long newsId)
    {
        newsService.deleteNews(newsId);
    }

    @PostMapping("/addReadLater")
    public ReadLater addReadLater(@RequestBody ReadLater readLater)
    {
        return readLaterService.addReadLater(readLater);
    }


    @GetMapping("/getSaved")
    public List<ReadLater> getSavedReadLater(@RequestParam Long userId)
    {
        return readLaterService.getSavedReadLater(userId, SavedStatus.SAVED);

    }

    @GetMapping("/getNewsChecked")
    public int getSavedReadLaterChecked(@RequestParam Long userId, @RequestParam Long newsId)
    {
        ReadLater rl=readLaterService.getSavedReadLaterChecked(newsId,userId);
        System.out.println(rl);

        if(rl !=null)
            return 1;
        else return 0;

    }

    @GetMapping("/getArticleType")
    public List<String> getArticleTyep()
    {
        ArticleType[] articleTypes=ArticleType.values();
        List<String> list=new ArrayList();
        for (ArticleType articleType:articleTypes)
        {
            list.add(articleType.toString());
        }
        System.out.println(list);
        return list;

    }

    @GetMapping("/getArticleCategories")
    public List<String> getArticleCategories()
    {
        ArticleCategory[] articleCategory=ArticleCategory.values();
        List<String> list=new ArrayList();
        for (ArticleCategory articleC:articleCategory)
        {
            list.add(articleC.toString());
        }
        System.out.println(list);
        return list;

    }

    @DeleteMapping("/removeChecked")
    public void removeChecked(@RequestParam Long userId, @RequestParam Long newsId)
    {
        readLaterService.deleteReadLaterByUserAndNews(userId,newsId);
    }

    @DeleteMapping("/removeReadLater")
    public void removeReadLater(@RequestParam Long readLaterId)
    {
        readLaterService.deleteReadLater(readLaterId);
    }

@PostMapping("/addDraft")
    public News addDraft(@RequestBody News news)
{
    return newsService.addNews(news);
}
}
