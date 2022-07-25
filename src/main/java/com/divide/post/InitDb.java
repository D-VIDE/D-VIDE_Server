package com.divide.post;

import com.divide.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;


import java.time.LocalDateTime;

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
            Post post1 = Post.builder()
                    .user(user1)
                    .title("title1")
                    .storeName("storeName1")
                    .content("content1")
                    .targetPrice(10000)
                    .deliveryPrice(3000)
                    .targetUserCount(3)
                    .category(Category.CHINESE_FOOD)
                    .targetTime(LocalDateTime.now().plusHours(1))
//                    .deliveryLocation(deliveryLocation1)
                    .postStatus(PostStatus.RECRUIT_FAIL)
                    .build();

            Point deliveryLocation2 = new Point(6,2);
            Post post2 = Post.builder()
                    .user(user1)
                    .title("title2")
                    .storeName("storeName2")
                    .content("content2")
                    .targetPrice(20000)
                    .deliveryPrice(4000)
                    .targetUserCount(4)
                    .category(Category.KOREAN_FOOD)
                    .targetTime(LocalDateTime.now().plusHours(2))
//                    .deliveryLocation(deliveryLocation2)
                    .postStatus(PostStatus.RECRUITING)
                    .build();

            em.persist(post1);
            em.persist(post2);

        }
    }

}
