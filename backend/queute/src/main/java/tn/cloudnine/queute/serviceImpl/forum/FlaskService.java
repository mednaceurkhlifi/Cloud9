package tn.cloudnine.queute.serviceImpl.forum;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import tn.cloudnine.queute.dto.forum.SentimentResponse;
import tn.cloudnine.queute.dto.forum.ToxicityReponse;
import tn.cloudnine.queute.enums.forum.SentimentType;
import tn.cloudnine.queute.model.forum.Post;
import tn.cloudnine.queute.model.forum.Votable;
import tn.cloudnine.queute.service.forum.IFlaskService;

import java.util.Map;

@Service
public class FlaskService implements IFlaskService {
    private final WebClient webclient = WebClient.create("http://localhost:5000");
    @Override
    public String Summarizer(Post post) {
        StringBuilder result = new StringBuilder();
        result.append(post.getTitle()).append(post.getContent());
        if(post.getComments()!=null && post.getComments().size()>0) {
            post.getComments().forEach(comment -> {
                result.append(comment.getContent());
            });
        }
        var ret =webclient.post()
                .uri("/summarize")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of("text",result.toString()))
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return ret;
    }

    @Override
    public SentimentType sentimentAnalysis(Votable votable) {
        var ret =webclient.post()
                .uri("/sentiment")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of("text",votable.getContent()))
                .retrieve()
                .bodyToMono(SentimentResponse.class)
                .block();
        if(ret != null){
            if(ret.getLabel().equals("LABEL_0")){
                return SentimentType.NEGATIVE;
            }
            else if(ret.getLabel().equals("LABEL_1")){
                return SentimentType.NEUTRAL;
            }
            else if(ret.getLabel().equals("LABEL_2")){
                return SentimentType.POSITIVE;
            }
        }
        return SentimentType.NEUTRAL;
    }

    @Override
    public boolean isToxic(Votable votable) {
        var ret =webclient.post()
                .uri("/summarize")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of("text",votable.getContent()))
                .retrieve()
                .bodyToMono(ToxicityReponse.class)
                .block();
        if(ret != null){
            if(ret.getIdentity_attack()<0.2 && ret.getInsult()<0.4 && ret.getToxicity()<0.6 && ret.getObscene()<0.55 && ret.getSevere_toxicity()<0.4 && ret.getThreat()<0.55){
                return false;
            }
            return true;
        }
        return true;
    }
}
