package com.divide.follow;

import com.divide.user.User;
import com.divide.user.UserService;
import com.divide.user.dto.request.SignupRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FollowServiceTest {
    @Autowired
    private FollowService followService;

    @Autowired
    private UserService userService;

    @Test
    @Transactional
    public void followingTest() throws Exception {
        // given
        userService.signup(new SignupRequest("email1@gmail.com", "password1", "url1", "nick1"));
        User user1 = userService.getUserByEmail("email1@gmail.com");

        userService.signup(new SignupRequest("email2@gmail.com", "password2", "url2", "nick2"));
        User user2 = userService.getUserByEmail("email2@gmail.com");

        userService.signup(new SignupRequest("email3@gmail.com", "password3", "url3", "nick3"));
        User user3 = userService.getUserByEmail("email3@gmail.com");

        // when
        followService.save(user1.getEmail(), user2.getId());
        followService.save(user1.getEmail(), user3.getId());

        // then
        assertEquals(2, followService.getFollowingList(user1.getEmail()).getFollowList().size());
        assertEquals(0, followService.getFollowingList(user2.getEmail()).getFollowList().size());
        assertEquals(0, followService.getFollowingList(user3.getEmail()).getFollowList().size());
    }

    @Test
    @Transactional
    public void followerTest() throws Exception {
        // given
        userService.signup(new SignupRequest("email1@gmail.com", "password1", "url1", "nick1"));
        User user1 = userService.getUserByEmail("email1@gmail.com");

        userService.signup(new SignupRequest("email2@gmail.com", "password2", "url2", "nick2"));
        User user2 = userService.getUserByEmail("email2@gmail.com");

        userService.signup(new SignupRequest("email3@gmail.com", "password3", "url3", "nick3"));
        User user3 = userService.getUserByEmail("email3@gmail.com");

        // when
        followService.save(user1.getEmail(), user2.getId());
        followService.save(user1.getEmail(), user3.getId());
        followService.save(user2.getEmail(), user3.getId());

        // then
        assertEquals(2, followService.getFollowerList(user3.getEmail()).getFollowList().size());
        assertEquals(1, followService.getFollowerList(user2.getEmail()).getFollowList().size());
        assertEquals(0, followService.getFollowerList(user1.getEmail()).getFollowList().size());
    }

    @Test
    @Transactional
    public void fffTest() throws Exception {
        // given
        userService.signup(new SignupRequest("email1@gmail.com", "password1", "url1", "nick1"));
        User user1 = userService.getUserByEmail("email1@gmail.com");

        userService.signup(new SignupRequest("email2@gmail.com", "password2", "url2", "nick2"));
        User user2 = userService.getUserByEmail("email2@gmail.com");

        userService.signup(new SignupRequest("email3@gmail.com", "password3", "url3", "nick3"));
        User user3 = userService.getUserByEmail("email3@gmail.com");

        // when
        followService.save(user1.getEmail(), user2.getId());
        followService.save(user2.getEmail(), user1.getId());
        followService.save(user1.getEmail(), user3.getId());
        followService.save(user3.getEmail(), user1.getId());
        followService.save(user2.getEmail(), user3.getId());
        followService.save(user3.getEmail(), user2.getId());

        // then
        assertEquals(2, followService.getFFFList(user3.getEmail()).getFollowList().size());
        assertEquals(2, followService.getFFFList(user2.getEmail()).getFollowList().size());
        assertEquals(2, followService.getFFFList(user1.getEmail()).getFollowList().size());
    }
}