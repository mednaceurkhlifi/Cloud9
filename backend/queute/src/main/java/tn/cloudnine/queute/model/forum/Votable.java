package tn.cloudnine.queute.model.forum;

import jakarta.persistence.*;
import lombok.*;
import tn.cloudnine.queute.enums.forum.SentimentType;

import java.util.Date;
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Data
public class Votable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private Date date;
    private SentimentType sentimentType;;

}
