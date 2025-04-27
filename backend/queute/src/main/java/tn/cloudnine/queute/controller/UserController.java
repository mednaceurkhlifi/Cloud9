package tn.cloudnine.queute.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.cloudnine.queute.model.forum.Vote;
import tn.cloudnine.queute.model.user.User;
import tn.cloudnine.queute.service.forum.IVoteService;
import tn.cloudnine.queute.service.user.IUserService;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("user")
public class UserController {

//    public final IUserService userService;
//    @PostMapping("create-user")
//    public ResponseEntity<User> createVote(@RequestBody User user) {
//        return ResponseEntity.ok().body(userService.addUser(user));
//    }
}
