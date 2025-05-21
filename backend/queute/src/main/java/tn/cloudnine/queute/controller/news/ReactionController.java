package tn.cloudnine.queute.controller.news;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.cloudnine.queute.model.newsAndOffers.news.Reaction;
import  tn.cloudnine.queute.service.news.IReactionService;

import java.util.List;

@RestController
@RequestMapping("/react")
@CrossOrigin(origins = "*")

public class ReactionController {

    @Autowired
    private IReactionService reactionService;

    @GetMapping("/getAllReactions")
    public List<Reaction> getAllReactions()
    {
        return reactionService.getAllReaction();
    }

    @GetMapping("getReaction")
    public Reaction getReactionByUserAndNews(@RequestParam Long userId, @RequestParam Long newsId)
    {
        return this.reactionService.getReactionByUserAndNews(userId, newsId);
    }

    @GetMapping("/getAllReactionsByNews/{newsId}")
    public List<Reaction> getAllReactionsByNews(@PathVariable Long newsId)
    {
        return reactionService.getAllReactionByNewsId(newsId);
    }
    @PostMapping("/addReaction")
    public Reaction addReaction(@RequestBody Reaction reaction)
    {
        return reactionService.addReaction(reaction);

    }

    @DeleteMapping("/removeReaction/{reactionId}")
    public void removeReaction(@PathVariable Long reactionId)
    {
        reactionService.deleteReaction(reactionId);
    }

    @PutMapping("/updateReaction")
    public Reaction updateReaction(@RequestBody Reaction reaction)
    {
        System.out.println(reaction);
        return reactionService.updateReaction(reaction);
    }


}
