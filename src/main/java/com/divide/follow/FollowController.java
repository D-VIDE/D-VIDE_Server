package com.divide.follow;

import com.divide.follow.dto.request.DeleteFollowRequest;
import com.divide.follow.dto.request.GetFollowResponse;
import com.divide.follow.dto.request.PostFollowRequest;
import com.divide.follow.dto.response.DeleteFollowResponse;
import com.divide.follow.dto.response.GetFollowRequest;
import com.divide.follow.dto.response.PostFollowResponse;
import com.divide.post.dto.response.Result;
import com.divide.user.User;
import com.divide.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class FollowController {
    private final FollowService followService;
    private final UserService userService;

    @GetMapping("follow")
    public ResponseEntity getFollow(
            @AuthenticationPrincipal UserDetails userDetails,
            @ModelAttribute GetFollowRequest getFollowRequest
    ) {
        List<GetFollowResponse> getFollowResponse = null;
        switch (getFollowRequest.getRelation()) {
            case FOLLOWING -> getFollowResponse = followService.getFollowingList(userDetails.getUsername(), getFollowRequest.getFirst());
            case FOLLOWER -> getFollowResponse = followService.getFollowerList(userDetails.getUsername(), getFollowRequest.getFirst());
        }
        return ResponseEntity.ok(new Result(getFollowResponse));
    }

    @PostMapping("follow")
    public ResponseEntity<PostFollowResponse> postFollow(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody PostFollowRequest postFollowRequest
    ) {
        User follower = userService.getUserByEmail(userDetails.getUsername());
        User followee = userService.getUserById(postFollowRequest.getUserId());
        Long saveId = followService.save(follower, followee);
        return ResponseEntity.status(201).body(new PostFollowResponse(saveId));
    }

    @DeleteMapping("follow")
    public ResponseEntity<DeleteFollowResponse> deleteFollow(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody DeleteFollowRequest deleteFollowRequest
    ) {
        User follower = userService.getUserByEmail(userDetails.getUsername());
        User followee = userService.getUserById(deleteFollowRequest.getUserId());
        Long removedId = followService.remove(follower, followee);
        return ResponseEntity.ok(new DeleteFollowResponse(removedId));
    }
}
