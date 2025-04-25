package tn.cloudnine.queute.model.roadmap;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Step {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id ;
    private int stepOrder;
    private boolean isStrict;
    private String stepName ;
    private String stepDescription ;
    private String requiredPapers ;

    @Override
    public String toString() {
        return "Step{" +
                "id=" + id +
                ", stepOrder=" + stepOrder +
                ", isStrict=" + isStrict +
                ", stepName='" + stepName + '\'' +
                ", stepDescription='" + stepDescription + '\'' +
                ", requiredPapers='" + requiredPapers + '\'' +
                '}';
    }
}
