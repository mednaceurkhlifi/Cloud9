package tn.cloudnine.queute.dto.workspace;

import tn.cloudnine.queute.enums.workspace.ProjectRole;

public record UserDTO(
        String full_name,
        String email,
        String image,
        ProjectRole role
) {
}
