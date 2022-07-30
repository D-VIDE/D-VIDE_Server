package com.divide.post;

import com.divide.user.User;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;


import java.sql.ResultSet;
import java.time.LocalDateTime;

import static com.divide.user.UserRole.USER;

@Component
@RequiredArgsConstructor
public class InitDb {
    private final InitService initService;

    @PostConstruct
    public void init() throws ParseException {
        initService.dbInit1();
    }
    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final EntityManager em;
        public void dbInit1() throws ParseException {
            //String email, String password, String profileImgUrl, String nickname, UserRole role
            User user1 = new User("email@gmail.com", "password1", "profileImgUrl1", "nickname1", USER );
            em.persist(user1);

            //deliveryLocation
            double latitude = 127.030767490957;
            double longitude = 37.4901548250937;
            String pointWKT = String.format("POINT(%s %s)", longitude, latitude);
            Point point1 = (Point) new WKTReader().read(pointWKT);

            Post post1 = Post.builder()
                    .user(user1)
                    .title("title1")
                    .storeName("storeName1")
                    .content("content1")
                    .targetPrice(10000)
                    .deliveryPrice(3000)
                    .category(Category.CHINESE_FOOD)
                    .targetTime(LocalDateTime.now().plusHours(1))
                    .deliveryLocation(point1)
                    .postStatus(PostStatus.RECRUIT_FAIL)
                    .build();

            Point point2 = (Point) new WKTReader().read("POINT(127.030 37.490154)");

            Post post2 = Post.builder()
                    .user(user1)
                    .title("title2")
                    .storeName("storeName2")
                    .content("content2")
                    .targetPrice(20000)
                    .deliveryPrice(4000)
                    .category(Category.KOREAN_FOOD)
                    .targetTime(LocalDateTime.now().plusHours(2))
                    .deliveryLocation(point2)
                    .postStatus(PostStatus.RECRUITING)
                    .build();

            em.persist(post1);
            em.persist(post2);

        }
    }

}
