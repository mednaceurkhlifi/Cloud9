package tn.cloudnine.queute.dto.forum;

import lombok.Data;
import tn.cloudnine.queute.enums.forum.VoteType;
import tn.cloudnine.queute.model.forum.Vote;
@Data
public class VoteDTO {
    private Long id;
    private Long votableId;
    private VoteType voteType;
    private Long userId;
    public VoteDTO(Vote vote){
        this.votableId=vote.getVotable().getId();
        this.userId=vote.getUser().getUser_id();
        this.id=vote.getId();
        this.voteType=vote.getVoteType();

    }
}
