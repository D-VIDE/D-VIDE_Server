package com.divide.post;

import com.divide.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;


import java.time.LocalDateTime;

import static com.divide.post.Post.createPost;
import static com.divide.user.UserRole.USER;

@Component
@RequiredArgsConstructor
public class InitDb {
    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit1();
    }
    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final EntityManager em;
        public void dbInit1() {
            //String email, String password, String profileImgUrl, String nickname, UserRole role
            User user1 = new User("email@gmail.com", "password1", "profileImgUrl1", "nickname1", USER );
            em.persist(user1);

            Point deliveryLocation1 = new Point(2,4);
            Post post1 = createPost(user1, "title1", "storeName1", "content1", 10000, 3000, 3,
                    Category.CHINESE_FOOD, LocalDateTime.now(), deliveryLocation1, PostStatus.RECRUIT_FAIL);
            Point deliveryLocation2 = new Point(6,2);
            Post post2 = createPost(user1, "title2", "storeName2", "content2", 20000, 4000, 4,
                    Category.KOREAN_FOOD, LocalDateTime.now(), deliveryLocation2, PostStatus.RECRUITING);

            em.persist(post1);
            em.persist(post2);

        }
    }

    private static Post createPost(User user1, String title, String storeName, String content,
                                   int targetPrice, int deliveryPrice, int targetUSerCount, Category category,
                                   LocalDateTime targetTime, Point deliveryLocation, PostStatus postStatus) {
        Post post = new Post();
        post.setUser(user1);
        post.setTitle(title);
        post.setStoreName(storeName);
        post.setContent(content);
        post.setTargetPrice(targetPrice);
        post.setDeliveryPrice(deliveryPrice);
        post.setTargetUserCount(targetUSerCount);
        post.setCategory(category);
        post.setTargetTime(targetTime);
        post.setDeliveryLocation(deliveryLocation);
        post.setPostStatus(postStatus);
        return post;
    }
}
