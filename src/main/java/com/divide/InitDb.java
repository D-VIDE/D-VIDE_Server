package com.divide;

import com.divide.exception.RestApiException;
import com.divide.exception.code.FileIOErrorCode;
import com.divide.follow.FollowService;
import com.divide.order.OrderService;
import com.divide.post.PostService;
import com.divide.post.domain.Category;
import com.divide.post.domain.Post;
import com.divide.post.domain.PostStatus;
import com.divide.review.ReviewService;
import com.divide.review.dto.request.PostReviewRequest;
import com.divide.review.dto.request.PostReviewRequestV2;
import com.divide.user.User;
import com.divide.user.UserBadge;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
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
@Profile("!dev")
@RequiredArgsConstructor
public class InitDb {
    private final InitService initService;

    @PostConstruct
    public void init() throws ParseException {
        initService.dbInit1();
    }
    @Component
    @Profile("!dev")
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        @Value("${test.user-count}")
        public int USER_COUNT;
        @Value("${test.post-count}")
        public int POST_COUNT;
        private final UserService userService;
        private final PostService postService;
        private final OrderService orderService;
        private final FollowService followService;
        private final ReviewService reviewService;

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
                return new CommonsMultipartFile(fileItem);
            } catch (IOException e) {
                throw new RestApiException(FileIOErrorCode.FILE_IO_ERROR);
            }
        }
        public void dbInit1() throws ParseException {
            /* User 등록 */
            List<User> userList = new ArrayList<>();
            List<UserBadge.BadgeName> badgeNameList = new ArrayList<>(List.of(UserBadge.BadgeName.values()));
            badgeNameList.remove(0);
            for (int i = 0; i < USER_COUNT; ++i) {
                String email = "email" + (i == 0 ? "" : i) + "@gmail.com";
                userService.signup(new SignupRequest(email, "password" + (i == 0 ? 1 : i), getSampleMultipartFile(), "nickname" + (i == 0 ? 1 : i)));
                User user = userService.getUserByEmail(email);
                userList.add(user);

                int badgeSize = new Random().nextInt(1, badgeNameList.size() - 1);
                Collections.shuffle(badgeNameList);
                badgeNameList.stream()
                        .limit(badgeSize)
                        .forEach(badgeName -> userService.saveUserBadge(user, badgeName));
            }

            /* Follow 등록 */
            for (int i = 0; i < USER_COUNT; ++i) {
                if (i == 5) continue;
                for (int j = i + 1; j < USER_COUNT; ++j) {
                    if (j == 5) continue;
                    int nextInt = random.nextInt(0, 3);
                    if (nextInt == 0) {
                        followService.save(userList.get(i), userList.get(j));
                    } else if (nextInt == 1) {
                        followService.save(userList.get(j), userList.get(i));
                    } else {
                        followService.save(userList.get(i), userList.get(j));
                        followService.save(userList.get(j), userList.get(i));
                    }
                }

            }

            List<Category> categories = List.of(Category.values());
            List<PostStatus> postStatuses = List.of(PostStatus.values());

            /* 게시글 이미지 2개 생성 */
            MultipartFile sampleMultipartFile = getSampleMultipartFile();
            String postImageUrl1 = OCIUtil.uploadFile(sampleMultipartFile, OCIUtil.FolderName.POST,  "sample" + "/" + UUID.randomUUID() + ".jpg");
            String postImageUrl2 = OCIUtil.uploadFile(sampleMultipartFile, OCIUtil.FolderName.POST,  "sample" + "/" + UUID.randomUUID() + ".jpg");
            String orderImgUrl = OCIUtil.uploadFile(sampleMultipartFile, OCIUtil.FolderName.ORDER, "sample" + "/" + UUID.randomUUID() + ".jpg");

            /* 리뷰 이미지 생성 */
            String reviewImgUrl1 = OCIUtil.uploadFile(sampleMultipartFile, OCIUtil.FolderName.REVIEW, "sample" + "/" + UUID.randomUUID() + ".jpg");
            String reviewImgUrl2 = OCIUtil.uploadFile(sampleMultipartFile, OCIUtil.FolderName.REVIEW, "sample" + "/" + UUID.randomUUID() + ".jpg");

            for (int i = 0; i < POST_COUNT; ++i) {
                double longitude = 127.030767490957 + random.nextDouble() / 100;
                double latitude = 37.4901548250937 + random.nextDouble() / 100;
                String pointWKT = String.format("POINT(%s %s)", longitude, latitude);
                Point point = (Point) new WKTReader().read(pointWKT);

                /* 게시글 생성 */
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
                        .postImgUrls(List.of(postImageUrl1, postImageUrl2))
                        .build();
                postService.create(post);

                /* 주문 생성 */
                orderService.saveOrderTest(userList.get(random.nextInt(USER_COUNT)).getEmail(), post.getPostId(), random.nextInt(3000, 100001), List.of(orderImgUrl));
                orderService.saveOrderTest(userList.get(random.nextInt(USER_COUNT)).getEmail(), post.getPostId(), random.nextInt(3000, 100001), List.of(orderImgUrl));

                /* 리뷰 생성 */
                reviewService.createReviewTest(userList.get(random.nextInt(USER_COUNT)).getEmail(), 5 * random.nextDouble(),"content"+i, post.getPostId(), List.of(reviewImgUrl1, reviewImgUrl2));
            }
        }
    }

}
