package com.divide.follow;

import com.divide.user.User;
import com.divide.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    public void save(String myUserEmail, Long targetId) {
        User myUser = userRepository.findByEmail(myUserEmail).orElseThrow();
        User targetUser = userRepository.findById(targetId);
        followRepository.save(new Follow(myUser, targetUser));
    }

    public List<Follow> getFFFList(String userEmail) {
        return followRepository.getFFFList(userRepository.findByEmail(userEmail).orElseThrow());
    }

    public List<Follow> getFollowingList(String userEmail) {
        return followRepository.getFollowingList(userRepository.findByEmail(userEmail).orElseThrow());

    }

    public List<Follow> getFollowerList(String userEmail) {
        return followRepository.getFollowerList(userRepository.findByEmail(userEmail).orElseThrow());
    }
}
