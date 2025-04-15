package tn.cloudnine.queute.model.user;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import tn.cloudnine.queute.model.workspace.ProjectUser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE users u SET u.is_deleted = true WHERE u.user_id=? AND u.is_deleted = false ")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String first_name;
    private String last_name;
    private String full_name;
    private LocalDateTime birth_date;
    private String image;
    private String address;
    private String password;
    private String reset_pwd_code;
    private LocalDateTime reset_pwd_date;

    @Column(unique = true)
    private String phone_number;

    @Column(unique = true)
    private String email;

    private boolean is_locked;
    private boolean is_deleted;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime created_at;

    @LastModifiedDate
    private LocalDateTime updated_at;
}
