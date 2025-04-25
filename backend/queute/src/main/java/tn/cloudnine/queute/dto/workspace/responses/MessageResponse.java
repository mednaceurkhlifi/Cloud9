package tn.cloudnine.queute.dto.workspace.responses;

import tn.cloudnine.queute.dto.workspace.projections.MessageProjection;

import java.util.List;

public record MessageResponse (
        List<MessageProjection> messages,
        int pageNo,
        int pageSize,
        long totalElements,
        int totalPages,
        boolean isLast
){
}
