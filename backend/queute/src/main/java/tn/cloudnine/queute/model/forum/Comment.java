package com.example.projforum.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Entity
public class Comment extends Votable {

    @OneToMany(mappedBy = "votable", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vote> votes;
    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
