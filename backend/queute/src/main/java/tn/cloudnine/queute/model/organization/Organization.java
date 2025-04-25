package tn.cloudnine.queute.model.organization;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import tn.cloudnine.queute.model.newsAndOffers.news.News;
import tn.cloudnine.queute.model.workspace.Workspace;

import java.time.LocalDateTime;
import java.util.Set;

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
    private Long organizationId;

    private String name;
    private String address;

    @Column(unique = true)
    private String phone_number;

    @Column(unique = true)
    private String email;

    @OneToOne(mappedBy = "organization", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JsonManagedReference
    private Workspace workspace;

    private boolean is_deleted;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime created_at;

    @LastModifiedDate
    private LocalDateTime updated_at;

    @OneToMany(mappedBy = "organisation")
    @JsonIgnore
    private Set<News> news;

    @Override
    public String toString() {
        return "Organization{" +
                "organizationId=" + organizationId +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phone_number='" + phone_number + '\'' +
                ", email='" + email + '\'' +
                ", workspace=" + workspace +
                ", is_deleted=" + is_deleted +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                ", news=" + news +
                '}';
    }
}
