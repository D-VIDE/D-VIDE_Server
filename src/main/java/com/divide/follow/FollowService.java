package com.divide.follow;

import com.divide.exception.RestApiException;
import com.divide.exception.code.CommonErrorCode;
import com.divide.exception.code.FollowErrorCode;
import com.divide.exception.code.UserErrorCode;
import com.divide.follow.dto.request.GetFollowResponse;
import com.divide.user.User;
import com.divide.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    public Long save(User follower, User followee) {
        /* 자기 자신일 경우 */
        if (follower.getId() == followee.getId()) throw new RestApiException(FollowErrorCode.FOLLOW_SELF_ERROR);
        /* 이미 등록된 Follow일 경우 */
        if (followRepository.find(follower, followee).isPresent()) throw new RestApiException(FollowErrorCode.DUPLICATED_FOLLOW);

        Follow newFollow = new Follow(follower, followee);
        followRepository.save(newFollow);
        return newFollow.getId();
    }

    public Long remove(User follower, User followee) {
        Follow follow = followRepository.find(follower, followee).orElseThrow(() -> new RestApiException(FollowErrorCode.FOLLOW_NOT_FOUND));

        followRepository.remove(follow);
        return follow.getId();
    }

    public GetFollowResponse getFollowingList(String userEmail, Integer first) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException(""));
        List<Follow> followList = followRepository.getFollowingList(user, first);
        return new GetFollowResponse(
                followList.stream().map(f -> new GetFollowResponse.GetFollowResponseElement(
                        f.getFollowee().getId(),
                        f.getFollowee().getProfileImgUrl(),
                        f.getFollowee().getNickname(),
                        FollowRelation.FOLLOWING.name()
                )).collect(Collectors.toList())
        );
    }

    public GetFollowResponse getFollowerList(String userEmail, Integer first) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException(""));
        List<Follow> followList = followRepository.getFollowerList(user, first);
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
