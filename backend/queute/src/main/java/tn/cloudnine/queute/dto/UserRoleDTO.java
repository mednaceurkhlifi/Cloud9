package tn.cloudnine.queute.dto;

import lombok.*;
import tn.cloudnine.queute.enums.Role;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleDTO {
    private Long userId;
    private Role role;      // Rôle de l'utilisateur (CLIENT, ADMIN, STAFF)

}