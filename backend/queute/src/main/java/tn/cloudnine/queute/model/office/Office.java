package tn.cloudnine.queute.model.office;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;
import tn.cloudnine.queute.model.organization.Organization;
import tn.cloudnine.queute.model.services.Service;

import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Office implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String location;
    private String phoneNumber;

    @ManyToOne
    @JoinColumn(name = "organisation_id")
    private Organization organisation; // Relation avec Organisation

    @OneToMany(mappedBy = "office", cascade = CascadeType.ALL)
    private List<Service> services; // Un Office a plusieurs Services
}
