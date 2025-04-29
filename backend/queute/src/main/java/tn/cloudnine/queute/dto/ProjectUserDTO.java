package tn.cloudnine.queute.dto.workspace;

import tn.cloudnine.queute.enums.workspace.ProjectRole;


public record ProjectUserDTO (
        ProjectRole role,
        ProjectDTO project,
        UserDTO user
){

}
