package tn.cloudnine.queute.model.ServiceAndFeedback.organization;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationEntityCountsDTO {
    private Long organizationId;
    private String organizationName;
    private int officeCount;
    private int feedbackCount;
    private int userCount;
}
