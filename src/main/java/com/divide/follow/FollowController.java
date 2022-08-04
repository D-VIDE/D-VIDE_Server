package com.divide.follow;

import com.divide.follow.dto.request.GetFollowResponse;
import com.divide.follow.dto.request.PostFollowRequest;
import com.divide.follow.dto.response.PostFollowResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class FollowController {
    private final FollowService followService;

    @GetMapping("follow")
    public ResponseEntity<GetFollowResponse> getFollow(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam("relation") FollowRelation relation
    ) {
        GetFollowResponse getFollowResponse = null;
        switch (relation) {
            case FFF -> getFollowResponse = followService.getFFFList(userDetails.getUsername());
            case FOLLOWING -> getFollowResponse = followService.getFollowingList(userDetails.getUsername());
            case FOLLOWER -> getFollowResponse = followService.getFollowerList(userDetails.getUsername());
        }
        return ResponseEntity.ok(getFollowResponse);
    }

    @PostMapping("follow")
    public ResponseEntity<PostFollowResponse> postFollow(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody PostFollowRequest postFollowRequest
    ) {
        Long saveId = followService.save(userDetails.getUsername(), postFollowRequest.getUserId());
        return ResponseEntity.status(201).body(new PostFollowResponse(saveId));
    }
}
