package tn.cloudnine.queute.dto.workspace.responses;

import tn.cloudnine.queute.dto.workspace.projections.MeetingProjection;

import java.util.List;

public record MeetingResponse (
        List<MeetingProjection> meetings,
        int pageNo,
        int pageSize,
        long totalElements,
        int totalPages,
        boolean isLast
){
}
