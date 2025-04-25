package tn.cloudnine.queute.model.Embeddable;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class ProjectUserId implements Serializable {
    private Long project;
    private Long user;
}
