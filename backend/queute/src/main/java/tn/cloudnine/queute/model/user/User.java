package tn.cloudnine.queute.model.user;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    private String firstName;
    private String lastName;
    private String fullName;
    private LocalDate birthDate;
    private String image;
    private String address;
    private String password;
    private String resetPwdCode;
    private LocalDateTime resetPwdDate;

    @Column(unique = true)
    private String phoneNumber;

    @Column(unique = true)
    private String email;

    private boolean isLocked;
    private boolean isDeleted;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", fullName='" + fullName + '\'' +
                ", birthDate=" + birthDate +
                ", image='" + image + '\'' +
                ", address='" + address + '\'' +
                ", password='" + password + '\'' +
                ", resetPwdCode='" + resetPwdCode + '\'' +
                ", resetPwdDate=" + resetPwdDate +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", isLocked=" + isLocked +
                ", isDeleted=" + isDeleted +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
