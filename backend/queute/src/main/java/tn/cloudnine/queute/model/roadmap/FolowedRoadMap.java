package tn.cloudnine.queute.model.roadmap;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class FolowedRoadMap {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id ;
}
