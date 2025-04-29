package tn.cloudnine.queute.dto.requests;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class WorkspaceRequest{
    private String name;
    private String description;
    private Long organization;
    private boolean imageOnUpdate;
}
