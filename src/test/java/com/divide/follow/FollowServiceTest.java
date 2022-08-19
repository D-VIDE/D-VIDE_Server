package com.divide.follow;

import com.divide.follow.dto.request.GetFollowResponse;
import com.divide.user.User;
import com.divide.user.UserService;
import com.divide.user.dto.request.SignupRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class FollowServiceTest {
    @Autowired
    private FollowService followService;

    @Autowired
    private UserService userService;

    @BeforeEach
    @Transactional
    void setUp() throws IOException {
        MockMultipartFile mockMultipartFile = getMockMultipartFile("profile", "jpg", "src/main/resources/static/sample.jpg");
        userService.signup(new SignupRequest("email1@gmail.com", "password1", mockMultipartFile, "nick1"));
        userService.signup(new SignupRequest("email2@gmail.com", "password2", mockMultipartFile, "nick2"));
        userService.signup(new SignupRequest("email3@gmail.com", "password3", mockMultipartFile, "nick3"));
        userService.signup(new SignupRequest("email4@gmail.com", "password4", mockMultipartFile, "nick4"));
        userService.signup(new SignupRequest("email5@gmail.com", "password5", mockMultipartFile, "nick5"));
        userService.signup(new SignupRequest("email6@gmail.com", "password6", mockMultipartFile, "nick6"));
        userService.signup(new SignupRequest("email7@gmail.com", "password7", mockMultipartFile, "nick7"));
        userService.signup(new SignupRequest("email8@gmail.com", "password8", mockMultipartFile, "nick8"));
        userService.signup(new SignupRequest("email9@gmail.com", "password9", mockMultipartFile, "nick9"));
    }

    private MockMultipartFile getMockMultipartFile(String fileName, String contentType, String path) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(path);
        return new MockMultipartFile(fileName, fileName + "." + contentType, contentType, fileInputStream);
    }

    @Test
    public void followingTest() throws Exception {
        // given
        User user1 = userService.getUserByEmail("email1@gmail.com");
        User user2 = userService.getUserByEmail("email2@gmail.com");
        User user3 = userService.getUserByEmail("email3@gmail.com");
        User user4 = userService.getUserByEmail("email4@gmail.com");
        User user5 = userService.getUserByEmail("email5@gmail.com");
        User user6 = userService.getUserByEmail("email6@gmail.com");
        User user7 = userService.getUserByEmail("email7@gmail.com");
        User user8 = userService.getUserByEmail("email8@gmail.com");
        User user9 = userService.getUserByEmail("email9@gmail.com");

        // when
        followService.save(user1.getEmail(), user2.getId());
        followService.save(user1.getEmail(), user3.getId());
        followService.save(user3.getEmail(), user4.getId());
        followService.save(user3.getEmail(), user5.getId());
        followService.save(user3.getEmail(), user6.getId());
        followService.save(user3.getEmail(), user7.getId());
        followService.save(user3.getEmail(), user8.getId());
        followService.save(user3.getEmail(), user9.getId());

        // then
        assertEquals(List.of(user2.getId(), user3.getId()),
                followService.getFollowingList(user1.getEmail()).getFollowList().stream().map(GetFollowResponse.GetFollowResponseElement::getUserId).collect(Collectors.toList()));
        assertEquals(List.of(),
                followService.getFollowingList(user2.getEmail()).getFollowList().stream().map(GetFollowResponse.GetFollowResponseElement::getUserId).collect(Collectors.toList()));
        assertEquals(List.of(user4.getId(), user5.getId(), user6.getId(), user7.getId(), user8.getId(), user9.getId()),
                followService.getFollowingList(user3.getEmail()).getFollowList().stream().map(GetFollowResponse.GetFollowResponseElement::getUserId).collect(Collectors.toList()));
        assertEquals(List.of(),
                followService.getFollowingList(user8.getEmail()).getFollowList().stream().map(GetFollowResponse.GetFollowResponseElement::getUserId).collect(Collectors.toList()));
    }

    @Test
    public void followerTest() throws Exception {
        // given
        User user1 = userService.getUserByEmail("email1@gmail.com");
        User user2 = userService.getUserByEmail("email2@gmail.com");
        User user3 = userService.getUserByEmail("email3@gmail.com");
        User user4 = userService.getUserByEmail("email4@gmail.com");
        User user5 = userService.getUserByEmail("email5@gmail.com");
        User user6 = userService.getUserByEmail("email6@gmail.com");
        User user7 = userService.getUserByEmail("email7@gmail.com");
        User user8 = userService.getUserByEmail("email8@gmail.com");
        User user9 = userService.getUserByEmail("email9@gmail.com");

        // when
        followService.save(user1.getEmail(), user2.getId());
        followService.save(user1.getEmail(), user3.getId());
        followService.save(user3.getEmail(), user4.getId());
        followService.save(user3.getEmail(), user5.getId());
        followService.save(user3.getEmail(), user6.getId());
        followService.save(user3.getEmail(), user7.getId());
        followService.save(user3.getEmail(), user8.getId());
        followService.save(user3.getEmail(), user9.getId());
        followService.save(user4.getEmail(), user2.getId());
        followService.save(user5.getEmail(), user2.getId());

        // then
        assertEquals(List.of(user1.getId() ,user4.getId(), user5.getId()),
                followService.getFollowerList(user2.getEmail()).getFollowList().stream().map(GetFollowResponse.GetFollowResponseElement::getUserId).collect(Collectors.toList()));
        assertEquals(List.of(),
                followService.getFollowerList(user1.getEmail()).getFollowList().stream().map(GetFollowResponse.GetFollowResponseElement::getUserId).collect(Collectors.toList()));
        assertEquals(List.of(user3.getId()),
                followService.getFollowerList(user5.getEmail()).getFollowList().stream().map(GetFollowResponse.GetFollowResponseElement::getUserId).collect(Collectors.toList()));
        assertEquals(List.of(user1.getId()),
                followService.getFollowerList(user3.getEmail()).getFollowList().stream().map(GetFollowResponse.GetFollowResponseElement::getUserId).collect(Collectors.toList()));
    }
}