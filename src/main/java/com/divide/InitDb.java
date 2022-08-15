package com.divide;

import com.divide.exception.RestApiException;
import com.divide.exception.code.FileIOErrorCode;
import com.divide.order.OrderService;
import com.divide.post.PostService;
import com.divide.post.domain.Category;
import com.divide.post.domain.Post;
import com.divide.post.domain.PostImage;
import com.divide.post.domain.PostStatus;
import com.divide.user.User;
import com.divide.user.UserService;
import com.divide.user.dto.request.SignupRequest;
import com.divide.utils.OCIUtil;
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

import java.util.*;

import javax.annotation.PostConstruct;


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
        public static final int USER_COUNT = 5;
        public static final int POST_COUNT = 30;
        private final UserService userService;
        private final PostService postService;
        private final OrderService orderService;

        Random random = new Random();

        @PostConstruct
        public void init() {
            random.setSeed(System.currentTimeMillis());
        }

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
            List<User> userList = new ArrayList<User>();
            for (int i = 0; i < USER_COUNT; ++i) {
                String email = "email" + (i == 0 ? "" : i) + "@gmail.com";
                userService.signup(new SignupRequest(email, "password" + (i == 0 ? 1 : i), getSampleMultipartFile(), "nickname" + (i == 0 ? 1 : i)));
                userList.add(userService.getUserByEmail(email));
            }


            List<Category> categories = List.of(Category.values());
            List<PostStatus> postStatuses = List.of(PostStatus.values());

            //게시글 이미지 2개 생성
            List<String> postImgUrls = new ArrayList<>();
            MultipartFile sampleMultipartFile = getSampleMultipartFile();
            String postImageUrl1 = OCIUtil.uploadFile(sampleMultipartFile, OCIUtil.FolderName.POST,  "sample" + "/" + UUID.randomUUID() + ".jpg");
            String postImageUrl2 = OCIUtil.uploadFile(sampleMultipartFile, OCIUtil.FolderName.POST,  "sample" + "/" + UUID.randomUUID() + ".jpg");
            String orderImgUrl = OCIUtil.uploadFile(sampleMultipartFile, OCIUtil.FolderName.ORDER, "sample" + "/" + UUID.randomUUID() + ".jpg");

            for (int i = 0; i < POST_COUNT; ++i) {
                double longitude = 127.030767490957 + random.nextDouble() / 100;
                double latitude = 37.4901548250937 + random.nextDouble() / 100;
                String pointWKT = String.format("POINT(%s %s)", longitude, latitude);
                Point point = (Point) new WKTReader().read(pointWKT);
                //게시글 이미지
                postImgUrls.add(postImageUrl1);
                postImgUrls.add(postImageUrl2);

                Post post = Post.builder()
                        .user(userList.get(0))
                        .title("title" + i)
                        .storeName("storeName" + i)
                        .content("content" + i)
                        .targetPrice(random.nextInt(18000, 100001))
                        .deliveryPrice(random.nextInt(1000, 5001))
                        .category(categories.get(random.nextInt(categories.size())))
                        .targetTime(LocalDateTime.now().plusMinutes(random.nextInt(30000)))
                        .deliveryLocation(point)
                        .postStatus(postStatuses.get(random.nextInt(postStatuses.size())))
                        .postImgUrls(postImgUrls)
                        .build();
                postService.create(post);

                orderService.saveOrderTest(userList.get(random.nextInt(USER_COUNT)).getEmail(), post.getPostId(), random.nextInt(3000, 100001), List.of(orderImgUrl));
                orderService.saveOrderTest(userList.get(random.nextInt(USER_COUNT)).getEmail(), post.getPostId(), random.nextInt(3000, 100001), List.of(orderImgUrl));
            }
        }
    }

}
