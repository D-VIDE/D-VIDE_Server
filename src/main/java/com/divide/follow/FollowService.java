package com.divide.follow;

import com.divide.exception.RestApiException;
import com.divide.exception.code.CommonErrorCode;
import com.divide.follow.dto.request.GetFollowResponse;
import com.divide.user.User;
import com.divide.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    public Long save(User follower, User followee) {
        /* 자기 자신일 경우 */
        if (follower.getId() == followee.getId()) throw new RestApiException(CommonErrorCode.INVALID_REQUEST);

        Follow newFollow = new Follow(follower, followee);
        followRepository.save(newFollow);
        return newFollow.getId();
    }

    public GetFollowResponse getFFFList(String userEmail) {
        List<Follow> followList = followRepository.getFFFList(userRepository.findByEmail(userEmail).orElseThrow());
        return new GetFollowResponse(
                followList.stream().map(f -> new GetFollowResponse.GetFollowResponseElement(
                        f.getFollowee().getId(),
                        f.getFollowee().getProfileImgUrl(),
                        f.getFollowee().getNickname(),
                        FollowRelation.FFF.name()
                )).collect(Collectors.toList())
        );
    }

    public GetFollowResponse getFollowingList(String userEmail) {
        List<Follow> followList = followRepository.getFollowingList(userRepository.findByEmail(userEmail).orElseThrow());
        return new GetFollowResponse(
                followList.stream().map(f -> new GetFollowResponse.GetFollowResponseElement(
                        f.getFollowee().getId(),
                        f.getFollowee().getProfileImgUrl(),
                        f.getFollowee().getNickname(),
                        FollowRelation.FOLLOWING.name()
                )).collect(Collectors.toList())
        );
    }

    public GetFollowResponse getFollowerList(String userEmail) {
        List<Follow> followList = followRepository.getFollowerList(userRepository.findByEmail(userEmail).orElseThrow());
        return new GetFollowResponse(
                followList.stream().map(f -> new GetFollowResponse.GetFollowResponseElement(
                        f.getFollower().getId(),
                        f.getFollower().getProfileImgUrl(),
                        f.getFollower().getNickname(),
                        FollowRelation.FOLLOWER.name()
                )).collect(Collectors.toList())
        );
    }

    public Integer getFollowingCount(String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        return followRepository.getFollowingCount(user);
    }

    public Integer getFollowerCount(String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        return followRepository.getFollowerCount(user);
    }
}
