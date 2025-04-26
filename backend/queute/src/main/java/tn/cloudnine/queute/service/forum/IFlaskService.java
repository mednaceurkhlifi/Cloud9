package tn.cloudnine.queute.service.forum;

import tn.cloudnine.queute.enums.forum.SentimentType;
import tn.cloudnine.queute.model.forum.Post;
import tn.cloudnine.queute.model.forum.Votable;

public interface IFlaskService {
    public String Summarizer(Post post);
    public SentimentType sentimentAnalysis(Votable votable);
    public boolean isToxic(Votable votable);
}
