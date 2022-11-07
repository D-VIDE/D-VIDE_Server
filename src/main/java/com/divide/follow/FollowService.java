package com.divide.follow;

import com.divide.exception.RestApiException;
import com.divide.exception.code.FollowErrorCode;
import com.divide.follow.dto.response.GetFollowResponse;
import com.divide.follow.dto.response.GetFollowResponseWithFollowId;
import com.divide.follow.dto.response.GetFollowResponseWithRelation;
import com.divide.follow.dto.response.GetFollowOtherResponse;
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
        if (follower.getId().equals(followee.getId())) throw new RestApiException(FollowErrorCode.FOLLOW_SELF_ERROR);
        /* 이미 등록된 Follow일 경우 */
        if (followRepository.find(follower, followee).isPresent()) throw new RestApiException(FollowErrorCode.DUPLICATED_FOLLOW);

        Follow newFollow = new Follow(follower, followee);
        followRepository.save(newFollow);
        return newFollow.getId();
    }

    public Long remove(Long followId, User me) throws RestApiException {
        Follow follow = followRepository.findById(followId)
                .orElseThrow(() -> new RestApiException(FollowErrorCode.FOLLOW_NOT_FOUND));

        if (!follow.getFollower().equals(me) && !follow.getFollowee().equals(me)) {
            throw new RestApiException(FollowErrorCode.FOLLOW_ACCESS_UNAUTHORIZED);
        }

        followRepository.remove(follow);
        return followId;
    }

    @Deprecated
    public List<GetFollowResponse> getFollowingList(String userEmail, Integer first) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException(""));
        List<Follow> followList = followRepository.getFollowingList(user, first);
        return followList.stream().map(f -> new GetFollowResponse(
                f.getFollowee().getId(),
                f.getFollowee().getProfileImgUrl(),
                f.getFollowee().getNickname()
        )).collect(Collectors.toList());
    }

    public List<GetFollowResponse> getFollowingListWithFollowId(String userEmail, Integer first) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException(""));
        List<Follow> followList = followRepository.getFollowingList(user, first);
        return followList.stream().map(f -> new GetFollowResponseWithFollowId(
                f.getFollowee().getId(),
                f.getFollowee().getProfileImgUrl(),
                f.getFollowee().getNickname(),
                f.getId()
        )).collect(Collectors.toList());
    }

    @Deprecated
    public List<GetFollowResponse> getFollowerListWithFFF(String userEmail, Integer first) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException(""));
        List<Follow> followList = followRepository.getFollowerList(user, first);
        return followList.stream().map(f -> new GetFollowResponseWithRelation(
                f.getFollower().getId(),
                f.getFollower().getProfileImgUrl(),
                f.getFollower().getNickname(),
                followRepository.checkFFF(f)
        )).collect(Collectors.toList());
    }

    @Deprecated
    public List<GetFollowResponse> getFollowerList(String userEmail, Integer first) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException(""));
        List<Follow> followList = followRepository.getFollowerList(user, first);
        return followList.stream().map(f -> new GetFollowResponse(
                f.getFollower().getId(),
                f.getFollower().getProfileImgUrl(),
                f.getFollower().getNickname()
        )).collect(Collectors.toList());
    }

    public List<GetFollowResponse> getFollowerListWithFollowId(String userEmail, Integer first) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException(""));
        List<Follow> followList = followRepository.getFollowerList(user, first);
        return followList.stream().map(f -> new GetFollowResponseWithFollowId(
                f.getFollower().getId(),
                f.getFollower().getProfileImgUrl(),
                f.getFollower().getNickname(),
                f.getId()
        )).collect(Collectors.toList());
    }

    public Integer getFollowingCount(String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        return followRepository.getFollowingCount(user);
    }

    public Integer getFollowerCount(String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        return followRepository.getFollowerCount(user);
    }

    public Boolean getFollowed(User me, User other) {
        return followRepository.find(me, other).isPresent();
    }

    public List<GetFollowOtherResponse> getOtherFollowingList(User me, User other, Integer first) {
        List<Follow> followingList = followRepository.getFollowingList(other, first);
        return followingList.stream().map(
                f -> GetFollowOtherResponse.builder()
                        .userId(f.getFollowee().getId())
                        .nickname(f.getFollowee().getNickname())
                        .profileImgUrl(f.getFollowee().getProfileImgUrl())
                        .followed(followRepository.find(me, f.getFollowee()).isPresent())
                        .build()
            ).toList();
    }

    public List<GetFollowOtherResponse> getOtherFollowerList(User me, User other, Integer first) {
        List<Follow> followerList = followRepository.getFollowerList(other, first);
        return followerList.stream().map(
                f -> GetFollowOtherResponse.builder()
                        .userId(f.getFollower().getId())
                        .nickname(f.getFollower().getNickname())
                        .profileImgUrl(f.getFollower().getProfileImgUrl())
                        .followed(followRepository.find(me, f.getFollower()).isPresent())
                        .build()
        ).toList();
    }
}
