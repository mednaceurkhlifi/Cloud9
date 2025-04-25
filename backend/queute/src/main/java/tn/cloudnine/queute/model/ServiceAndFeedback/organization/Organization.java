package tn.cloudnine.queute.model.ServiceAndFeedback.organization;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.stereotype.Component;
import tn.cloudnine.queute.model.ServiceAndFeedback.feedback.Feedback;
import tn.cloudnine.queute.model.ServiceAndFeedback.office.Office;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Entity
@Table(name = "organizations")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE organizations o SET o.is_deleted = true WHERE o.organization_id=? AND o.is_deleted = false ")
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "organization_id")
    private Long id;

    @Column()
    private String name;

    @Column()
    private String address;

    @Column(unique = true)
    private String phone_number;

    @Column(unique = true)
    private String email;


    private String image;


    // @OneToOne(mappedBy = "organization", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
   // @JsonManagedReference
    //private Workspace workspace;

    private boolean is_deleted;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime created_at;

    @LastModifiedDate
    private LocalDateTime updated_at;


    @LastModifiedBy
    @Column(insertable = false)
    private  Integer lastModifiedBy;

    @OneToMany(mappedBy = "organisation", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JsonIgnore
    private List<Office> offices;

    @OneToMany(mappedBy = "organisation")
    @JsonIgnore
    private List<Feedback> feedbacks;

    private float averageRate;


}
