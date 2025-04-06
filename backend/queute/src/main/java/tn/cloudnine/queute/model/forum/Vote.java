package com.example.projforum.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private VoteType voteType;
    @ManyToOne
    private Votable votable;
    @ManyToOne
    private User user;
}
