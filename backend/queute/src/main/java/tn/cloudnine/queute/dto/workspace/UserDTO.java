package tn.cloudnine.queute.dto.workspace;

import tn.cloudnine.queute.enums.workspace.ProjectRole;

public record UserDTO(
        String fullName,
        String email,
        String image,
        ProjectRole role
) {
}
