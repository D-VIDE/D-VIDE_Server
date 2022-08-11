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
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import java.util.Random;

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
                        "sample.jpg",
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

            Random random = new Random();
            random.setSeed(System.currentTimeMillis());

            Category[] categories = Category.values();
            PostStatus[] postStatuses = PostStatus.values();

            for (int i = 0; i < 30; ++i) {
                double longitude = 127.030767490957 + random.nextDouble() / 100;
                double latitude = 37.4901548250937 + random.nextDouble() / 100;
                String pointWKT = String.format("POINT(%s %s)", longitude, latitude);
                Point point = (Point) new WKTReader().read(pointWKT);
                Post post = Post.builder()
                        .user(user1)
                        .title("title" + i)
                        .storeName("storeName" + i)
                        .content("content" + i)
                        .targetPrice(random.nextInt(18000, 100001))
                        .deliveryPrice(random.nextInt(1000, 5001))
                        .category(categories[random.nextInt(categories.length)])
                        .targetTime(LocalDateTime.now().plusHours(1))
                        .deliveryLocation(point)
                        .postStatus(postStatuses[random.nextInt(postStatuses.length)])
                        .build();
                em.persist(post);
            }
        }
    }

}
