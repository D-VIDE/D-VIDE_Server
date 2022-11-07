package com.divide.follow;

import com.divide.exception.RestApiException;
import com.divide.exception.code.UserErrorCode;
import com.divide.follow.dto.request.DeleteFollowRequest;
import com.divide.follow.dto.request.GetFollowOtherRequest;
import com.divide.follow.dto.response.GetFollowResponse;
import com.divide.follow.dto.request.PostFollowRequest;
import com.divide.follow.dto.response.DeleteFollowResponse;
import com.divide.follow.dto.response.GetFollowOtherResponse;
import com.divide.follow.dto.request.GetFollowRequest;
import com.divide.follow.dto.response.PostFollowResponse;
import com.divide.post.dto.response.Result;
import com.divide.user.User;
import com.divide.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class FollowController {
    private final FollowService followService;
    private final UserService userService;

    @Deprecated
    @GetMapping("/v1/follow")
    public ResponseEntity getFollowV1(
            @AuthenticationPrincipal UserDetails userDetails,
            @ModelAttribute GetFollowRequest getFollowRequest
    ) {
        List<GetFollowResponse> getFollowResponse = new ArrayList<>();
        switch (getFollowRequest.getRelation()) {
            case FOLLOWING -> getFollowResponse.addAll(followService.getFollowingList(userDetails.getUsername(), getFollowRequest.getFirst()));
            case FOLLOWER -> getFollowResponse.addAll(followService.getFollowerListWithFFF(userDetails.getUsername(), getFollowRequest.getFirst()));
        }
        return ResponseEntity.ok(new Result(getFollowResponse));
    }

    @Deprecated
    @GetMapping("/v2/follow")
    public ResponseEntity getFollowV2(
            @AuthenticationPrincipal UserDetails userDetails,
            @ModelAttribute GetFollowRequest getFollowRequest
    ) {
        List<GetFollowResponse> getFollowResponse = new ArrayList<>();
        switch (getFollowRequest.getRelation()) {
            case FOLLOWING -> getFollowResponse.addAll(followService.getFollowingList(userDetails.getUsername(), getFollowRequest.getFirst()));
            case FOLLOWER -> getFollowResponse.addAll(followService.getFollowerList(userDetails.getUsername(), getFollowRequest.getFirst()));
        }
        return ResponseEntity.ok(new Result(getFollowResponse));
    }

    @GetMapping("/v3/follow")
    public ResponseEntity getFollowV3(
            @AuthenticationPrincipal UserDetails userDetails,
            @ModelAttribute GetFollowRequest getFollowRequest
    ) {
        List<GetFollowResponse> getFollowResponse = new ArrayList<>();
        switch (getFollowRequest.getRelation()) {
            case FOLLOWING -> getFollowResponse.addAll(followService.getFollowingListWithFollowId(userDetails.getUsername(), getFollowRequest.getFirst()));
            case FOLLOWER -> getFollowResponse.addAll(followService.getFollowerListWithFollowId(userDetails.getUsername(), getFollowRequest.getFirst()));
        }
        return ResponseEntity.ok(new Result(getFollowResponse));
    }

    @Deprecated
    @GetMapping("/v1/follow/other")
    public ResponseEntity getFollowOtherV1(
            @AuthenticationPrincipal UserDetails userDetails,
            @ModelAttribute GetFollowOtherRequest getFollowOtherRequest
    ) {
        User me = userService.getUserByEmail(userDetails.getUsername());
        User other = userService.getUserById(getFollowOtherRequest.getUserId());
        if (me.getId().equals(other.getId())) {
            throw new RestApiException(UserErrorCode.OTHER_USER_IS_ME);
        }

        List<GetFollowOtherResponse> getFollowOtherResponses = new ArrayList<>();
        switch (getFollowOtherRequest.getRelation()) {
            case FOLLOWING -> getFollowOtherResponses.addAll(followService.getOtherFollowingList(me, other, getFollowOtherRequest.getFirst()));
            case FOLLOWER -> getFollowOtherResponses.addAll(followService.getOtherFollowerList(me, other, getFollowOtherRequest.getFirst()));
        }

        return ResponseEntity.ok(getFollowOtherResponses);
    }

    @Deprecated
    @GetMapping("/v2/follow/other")
    public ResponseEntity getFollowOtherV2(
            @AuthenticationPrincipal UserDetails userDetails,
            @ModelAttribute GetFollowOtherRequest getFollowOtherRequest
    ) {
        User me = userService.getUserByEmail(userDetails.getUsername());
        User other = userService.getUserById(getFollowOtherRequest.getUserId());
        if (me.getId().equals(other.getId())) {
            throw new RestApiException(UserErrorCode.OTHER_USER_IS_ME);
        }

        List<GetFollowOtherResponse> getFollowOtherResponses = new ArrayList<>();
        switch (getFollowOtherRequest.getRelation()) {
            case FOLLOWING -> getFollowOtherResponses.addAll(followService.getOtherFollowingListWithFollowId(me, other, getFollowOtherRequest.getFirst()));
            case FOLLOWER -> getFollowOtherResponses.addAll(followService.getOtherFollowerListWithFollowId(me, other, getFollowOtherRequest.getFirst()));
        }

        return ResponseEntity.ok(getFollowOtherResponses);
    }

    @PostMapping("/v1/follow")
    public ResponseEntity<PostFollowResponse> postFollow(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody PostFollowRequest postFollowRequest
    ) {
        User follower = userService.getUserByEmail(userDetails.getUsername());
        User followee = userService.getUserById(postFollowRequest.getUserId());
        Long saveId = followService.save(follower, followee);
        return ResponseEntity.status(201).body(new PostFollowResponse(saveId));
    }

    @DeleteMapping("/v1/follow")
    public ResponseEntity<DeleteFollowResponse> deleteFollow(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody DeleteFollowRequest deleteFollowRequest
    ) {
        User me = userService.getUserByEmail(userDetails.getUsername());
        Long removedId = followService.remove(deleteFollowRequest.getFollowId(), me);
        return ResponseEntity.ok(new DeleteFollowResponse(removedId));
    }
}
