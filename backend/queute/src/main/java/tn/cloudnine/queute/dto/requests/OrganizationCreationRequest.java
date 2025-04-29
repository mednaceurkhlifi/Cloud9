package tn.cloudnine.queute.dto.requests;

import lombok.*;
import tn.cloudnine.queute.dto.UserRoleDTO;

import java.util.List;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationCreationRequest {
    private String name;
    private String address;
    private String phoneNumber;
    private String email;
    private List<UserRoleDTO> usersWithRoles;

}
