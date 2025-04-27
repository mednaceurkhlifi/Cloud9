package tn.cloudnine.queute.model.roadmap;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import tn.cloudnine.queute.model.user.User;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoadMap {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id ;
    private String title ;
    @Column(length = 1000)
    private String description ;
    private int nbrApprove ;
    private int nbrDisapproval;
    @OneToMany
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<RoadMapApproval> approvals ;

    @ManyToOne(cascade = jakarta.persistence.CascadeType.PERSIST)
    private User creator ;
    @OneToMany
    @Cascade(CascadeType.ALL)
    private List<Step> steps ;

    @Override
    public String toString() {
        return "RoadMap{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", nbrApprove=" + nbrApprove +
                ", nbrDisapproval=" + nbrDisapproval +
                ", approvals=" + approvals +
                ", creator=" + creator +
                ", steps=" + steps +
                '}';
    }
}
