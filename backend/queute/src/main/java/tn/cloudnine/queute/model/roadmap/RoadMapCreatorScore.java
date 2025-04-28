package tn.cloudnine.queute.model.roadmap;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tn.cloudnine.queute.model.user.User;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoadMapCreatorScore {
    private User user;
    private int TotlaNbrRoadMap;
    private int TotaleNbrApprovalls;
    private int TotaleNbrFollow;
    private Long score;
}
