package com.divide.follow;

import com.divide.follow.dto.request.GetFollowResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
