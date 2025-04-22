package tn.cloudnine.queute.controller.workspace;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.cloudnine.queute.dto.workspace.projections.MeetingProjection;
import tn.cloudnine.queute.dto.workspace.responses.MeetingResponse;
import tn.cloudnine.queute.model.user.User;
import tn.cloudnine.queute.model.workspace.Meeting;
import tn.cloudnine.queute.service.workspace.IMeetingService;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("meeting")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class MeetingController {

    private final IMeetingService service;

    @PostMapping("create-new-meeting")
    public ResponseEntity<MeetingProjection> createNewMeeting(
            @RequestBody Meeting meeting
    ) {
        return ResponseEntity.ok(service.createNewMeeting(meeting));
    }

    @DeleteMapping("delete-meeting/{meeting_id}")
    public ResponseEntity<Map<String, String>> deleteMeeting(
            @PathVariable("meeting_id") Long meeting_id
    ) {
        service.deleteMeeting(meeting_id);
        return ResponseEntity.ok(
                Map.of("status", "success", "message", "Meeting deleted successfully.")
        );
    }

    @PatchMapping("invite-to-meeting/{meeting_id}")
    public ResponseEntity<MeetingProjection> inviteToMeeting(
            @PathVariable("meeting_id") Long meeting_id,
            @RequestBody Set<User> invited
            ) {
        return ResponseEntity.ok(service.inviteToMeeting(meeting_id, invited));
    }

    @GetMapping("get-meeting/{meeting_id}")
    public ResponseEntity<MeetingProjection> getMeetingById(@PathVariable("meeting_id") Long meeting_id) {
        return ResponseEntity.ok(service.getMeetingById(meeting_id));
    }

    @GetMapping("get-invited-meetings/{user_email}/{size}/{page_no}")
    public ResponseEntity<MeetingResponse> getInvitedMeetings(
            @PathVariable("user_email") String user_email,
            @PathVariable("size") Integer size,
            @PathVariable("page_no") Integer page_no
    ) {
        return ResponseEntity.ok(service.getInvitedMeetings(user_email, size, page_no));
    }

    @GetMapping("get-user-meetings/{user_email}/{size}/{page_no}")
    public ResponseEntity<MeetingResponse> getUserMeetings(
            @PathVariable("user_email") String user_email,
            @PathVariable("size") Integer size,
            @PathVariable("page_no") Integer page_no
            ) {
        return ResponseEntity.ok(service.getUserMeetings(user_email, size, page_no));
    }

}
