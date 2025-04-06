package com.example.projforum.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Votable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private Date date;

}
