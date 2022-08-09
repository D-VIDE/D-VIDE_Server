package com.divide;

import com.divide.exception.RestApiException;
import com.divide.exception.code.FileIOErrorCode;
import com.divide.post.domain.Category;
import com.divide.post.domain.Post;
import com.divide.post.domain.PostStatus;
import com.divide.user.User;
import com.divide.user.UserService;
import com.divide.user.dto.request.SignupRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;


import java.io.*;
import java.nio.file.Files;
import java.time.LocalDateTime;

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
        private final UserService userService;

        private MultipartFile getSampleMultipartFile() {
            try {
                ClassPathResource classPathResource = new ClassPathResource("static/sample.jpg");
                InputStream is = classPathResource.getInputStream();
                File tempFile = File.createTempFile(String.valueOf(is.hashCode()), ".tmp");
                tempFile.deleteOnExit();

                FileItem fileItem = new DiskFileItem(
                        "mainFile",
                        Files.probeContentType(tempFile.toPath()),
                        false,
                        "sample.tmp",
                        is.available(),
                        tempFile.getParentFile()
                );

                OutputStream os = fileItem.getOutputStream();
                IOUtils.copy(is, os);
                CommonsMultipartFile commonsMultipartFile = new CommonsMultipartFile(fileItem);
                return commonsMultipartFile;
            } catch (IOException e) {
                throw new RestApiException(FileIOErrorCode.FILE_IO_ERROR);
            }
        }

        public void dbInit1() throws ParseException {
            //String email, String password, String profileImgUrl, String nickname, UserRole role
            userService.signup(new SignupRequest("email@gmail.com", "password1", getSampleMultipartFile(), "nickname1"));
            User user1 = userService.getUserByEmail("email@gmail.com");

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

            Point point2 = (Point) new WKTReader().read("POINT(37.4895303786052 127.030436319555)");

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

            Point point3 = (Point) new WKTReader().read("POINT(37.4895773750866 127.03067318021)");

            Post post3 = Post.builder()
                    .user(user1)
                    .title("title3")
                    .storeName("storeName3")
                    .content("content3")
                    .targetPrice(30000)
                    .deliveryPrice(2000)
                    .category(Category.JAPANESE_FOOD)
                    .targetTime(LocalDateTime.now().plusHours(1))
                    .deliveryLocation(point3)
                    .postStatus(PostStatus.RECRUITING)
                    .build();

            Point point4 = (Point) new WKTReader().read("POINT(37.4895303786052 127.030436319555)");

            Post post4 = Post.builder()
                    .user(user1)
                    .title("title4")
                    .storeName("storeName4")
                    .content("content4")
                    .targetPrice(60000)
                    .deliveryPrice(3000)
                    .category(Category.KOREAN_FOOD)
                    .targetTime(LocalDateTime.now().plusHours(1))
                    .deliveryLocation(point4)
                    .postStatus(PostStatus.RECRUITING)
                    .build();

            Point point5 = (Point) new WKTReader().read("POINT(37.4895773750866 127.030673180212)");

            Post post5 = Post.builder()
                    .user(user1)
                    .title("title5")
                    .storeName("storeName5")
                    .content("content5")
                    .targetPrice(50000)
                    .deliveryPrice(5000)
                    .category(Category.DESSERT)
                    .targetTime(LocalDateTime.now().plusHours(3))
                    .deliveryLocation(point5)
                    .postStatus(PostStatus.RECRUITING)
                    .build();

            Point point6 = (Point) new WKTReader().read("POINT(37.4895773750866 127.030673180212)");

            Post post6 = Post.builder()
                    .user(user1)
                    .title("title6")
                    .storeName("storeName6")
                    .content("content6")
                    .targetPrice(60000)
                    .deliveryPrice(42000)
                    .category(Category.KOREAN_FOOD)
                    .targetTime(LocalDateTime.now().plusHours(6))
                    .deliveryLocation(point6)
                    .postStatus(PostStatus.RECRUITING)
                    .build();

            Point point7 = (Point) new WKTReader().read("POINT(37.4895773750866 127.03067318021)");

            Post post7 = Post.builder()
                    .user(user1)
                    .title("title7")
                    .storeName("storeName7")
                    .content("content7")
                    .targetPrice(70000)
                    .deliveryPrice(2000)
                    .category(Category.JAPANESE_FOOD)
                    .targetTime(LocalDateTime.now().plusHours(1))
                    .deliveryLocation(point7)
                    .postStatus(PostStatus.RECRUITING)
                    .build();

            Point point8 = (Point) new WKTReader().read("POINT(37.4901548250937 127.030767490957)");

            Post post8 = Post.builder()
                    .user(user1)
                    .title("title8")
                    .storeName("storeName8")
                    .content("content8")
                    .targetPrice(30000)
                    .deliveryPrice(4000)
                    .category(Category.KOREAN_FOOD)
                    .targetTime(LocalDateTime.now().plusHours(1))
                    .deliveryLocation(point8)
                    .postStatus(PostStatus.RECRUITING)
                    .build();

            Point point9 = (Point) new WKTReader().read("POINT(37.4901548250937 127.030767490957)");

            Post post9 = Post.builder()
                    .user(user1)
                    .title("title9")
                    .storeName("storeName9")
                    .content("content9")
                    .targetPrice(55000)
                    .deliveryPrice(25000)
                    .category(Category.DESSERT)
                    .targetTime(LocalDateTime.now().plusHours(3))
                    .deliveryLocation(point9)
                    .postStatus(PostStatus.RECRUITING)
                    .build();

            Point point10 = (Point) new WKTReader().read("POINT(37.4901548250937 127.030767490957)");

            Post post10 = Post.builder()
                    .user(user1)
                    .title("title10")
                    .storeName("storeName10")
                    .content("content10")
                    .targetPrice(55000)
                    .deliveryPrice(25000)
                    .category(Category.DESSERT)
                    .targetTime(LocalDateTime.now().plusHours(3))
                    .deliveryLocation(point10)
                    .postStatus(PostStatus.RECRUITING)
                    .build();

            Point point11 = (Point) new WKTReader().read("POINT(85.4901548250937 17.03076749095)");

            Post post11 = Post.builder()
                    .user(user1)
                    .title("title11")
                    .storeName("storeName11")
                    .content("content11")
                    .targetPrice(50000)
                    .deliveryPrice(5000)
                    .category(Category.DESSERT)
                    .targetTime(LocalDateTime.now().plusHours(3))
                    .deliveryLocation(point11)
                    .postStatus(PostStatus.RECRUITING)
                    .build();

            Point point12 = (Point) new WKTReader().read("POINT(97.4895303786052 27.030436355)");

            Post post12 = Post.builder()
                    .user(user1)
                    .title("title12")
                    .storeName("storeName12")
                    .content("content12")
                    .targetPrice(60000)
                    .deliveryPrice(42000)
                    .category(Category.KOREAN_FOOD)
                    .targetTime(LocalDateTime.now().plusHours(2))
                    .deliveryLocation(point12)
                    .postStatus(PostStatus.RECRUITING)
                    .build();

            Point point13 = (Point) new WKTReader().read("POINT(14.4812233750866 12.03067318021)");

            Post post13 = Post.builder()
                    .user(user1)
                    .title("title13")
                    .storeName("storeName13")
                    .content("content13")
                    .targetPrice(70000)
                    .deliveryPrice(2000)
                    .category(Category.JAPANESE_FOOD)
                    .targetTime(LocalDateTime.now().plusHours(1))
                    .deliveryLocation(point13)
                    .postStatus(PostStatus.RECRUITING)
                    .build();

            Point point14 = (Point) new WKTReader().read("POINT(7.4895773340866 72.030673180212)");

            Post post14 = Post.builder()
                    .user(user1)
                    .title("title14")
                    .storeName("storeName14")
                    .content("content14")
                    .targetPrice(30000)
                    .deliveryPrice(4000)
                    .category(Category.KOREAN_FOOD)
                    .targetTime(LocalDateTime.now().plusHours(1))
                    .deliveryLocation(point14)
                    .postStatus(PostStatus.RECRUITING)
                    .build();

            em.persist(post1);
            em.persist(post2);
            em.persist(post3);
            em.persist(post4);
            em.persist(post5);
            em.persist(post6);
            em.persist(post7);
            em.persist(post8);
            em.persist(post9);
            em.persist(post10);
            em.persist(post11);
            em.persist(post12);
            em.persist(post13);
            em.persist(post14);

        }
    }

}
