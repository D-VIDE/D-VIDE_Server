package com.divide.post;

import com.divide.common.CommonPostDetailResponse;
import com.divide.common.CommonPostResponse;
import com.divide.common.CommonUserResponse;
import com.divide.order.OrderService;
import com.divide.post.domain.Post;
import com.divide.post.domain.PostImage;
import com.divide.post.dto.request.PostPostRequest;
import com.divide.post.dto.request.UpdatePostRequest;
import com.divide.post.dto.request.GetPostsRequest;
import com.divide.post.dto.response.*;
import com.divide.user.User;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.io.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Api(tags = "Post API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Validated
public class PostController {
    private final PostService postService;
    private final OrderService orderService;

    /**
     * 게시물 생성 API V1
     * [POST] http://localhost:8080/api/v1/post
     * @param userDetails
     * @param request
     * @param postImageFiles
     * @return
     * @throws ParseException
     */
    @PostMapping(value = "/v1/post")
    public ResponseEntity<PostPostResponse> createPost(@AuthenticationPrincipal UserDetails userDetails, @RequestPart @Valid PostPostRequest request , @RequestPart MultipartFile... postImageFiles) throws ParseException {
        Long newPostId = postService.createPost(userDetails.getUsername(), request, List.of(postImageFiles));

        return ResponseEntity.status(HttpStatus.CREATED).body(new PostPostResponse(newPostId));
    }

    /**
     * 게시물 생성 API V2
     * [POST] http://localhost:8080/api/v2/post
     * @param userDetails
     * @param request
     * @param postImgFiles
     * @return
     * @throws ParseException
     */
    @PostMapping( "/v2/post")
    public ResponseEntity<PostPostResponse> createPostV2(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestPart @Valid PostPostRequest request,
            @NotNull @Size(min= 1, max = 3) @RequestPart List<MultipartFile> postImgFiles) throws ParseException {
        Long newPostId = postService.createPost(userDetails.getUsername(), request, postImgFiles);

        return ResponseEntity.status(HttpStatus.CREATED).body(new PostPostResponse(newPostId));
    }

    @GetMapping("/v1/posts")
    @Deprecated
    public Result getPostsV1(@Valid GetPostsRequest getPostsRequest){
        List<Post> findPosts = postService.findPostsAll(getPostsRequest.getFirst(), getPostsRequest.getLatitude(), getPostsRequest.getLongitude(), 0.5, getPostsRequest.getCategory());
        List<GetPostsResponseV1> collect = findPosts.stream()
                .map(GetPostsResponseV1::new)
                .collect(toList());
        return new Result(collect);
    }

    /**
     * 게시물 조회 API
     * [GET] http://localhost:8080/api/v2/posts?longitude=&latitude=&category=&first=
     * category 종류: STREET_FOOD, KOREAN_FOOD, JAPANESE_FOOD, CHINESE_FOOD, DESSERT, WESTERN_FOOD
     * @param getPostsRequest
     * @return
     */
    @GetMapping("/v2/posts")
    public Result getPosts(@Valid GetPostsRequest getPostsRequest) {
        List<Post> findPosts = postService.findPostsAll(getPostsRequest.getFirst(), getPostsRequest.getLatitude(), getPostsRequest.getLongitude(), 0.5, getPostsRequest.getCategory());
        List<GetPostsResponse> collect = findPosts.stream()
                .map(post -> {
                    User user = post.getUser();
                    return new GetPostsResponse(
                            new CommonUserResponse(
                                    user != null ? user.getId() : null,
                                    user != null ? user.getNickname() : null,
                                    user != null ? user.getProfileImgUrl() : null
                            ),
                            new CommonPostResponse(
                                    post.getPostId(),
                                    post.getDeliveryLocation().getCoordinate().getX(),
                                    post.getDeliveryLocation().getCoordinate().getY(),
                                    post.getTitle(),
                                    post.getTargetTime(),
                                    post.getTargetPrice(),
                                    post.getOrderedPrice(),
                                    post.getPostStatus(),
                                    post.getPostImages().get(0).getPostImageUrl()
                            )
                    );
                })
                .collect(toList());
        return new Result(collect);
    }

    @GetMapping("/v1/posts/{postId}")
    @Deprecated
    public Result getPostV1(@PathVariable Long postId){
        Post post = postService.findOne(postId);
        List<PostImage> findPostImgs = post.getPostImages();
        List<String> postImgUrls = findPostImgs.stream()
                .map(postImage ->  postImage.getPostImageUrl())
                .collect(toList());

        User user = post.getUser();
        GetPostResponseV1 getPostResponseV1 = new GetPostResponseV1(
                new CommonUserResponse(
                        user != null ? user.getId() : null,
                        user != null ? user.getNickname() : null,
                        user != null ? user.getProfileImgUrl() : null
                ),
                new CommonPostDetailResponse(
                        post.getPostId(),
                        post.getDeliveryLocation().getCoordinate().getX(),
                        post.getDeliveryLocation().getCoordinate().getY(),
                        post.getTitle(),
                        post.getTargetTime(),
                        post.getTargetPrice(),
                        post.getDeliveryPrice(),
                        post.getOrderedPrice(),
                        post.getContent(),
                        post.getStoreName(),
                        postImgUrls
                )
        );
        return new Result(getPostResponseV1);
    }

    @GetMapping("/v2/posts/{postId}")
    public Result getPostV2(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long postId
    ){
        Post post = postService.findOne(postId);
        List<String> postImgUrls = post.getPostImages().stream()
                .map(PostImage::getPostImageUrl)
                .collect(toList());

        Boolean ordered = orderService.checkOrdered(userDetails.getUsername(), postId);

        User user = post.getUser();
        GetPostResponseV2 getPostResponseV2 = new GetPostResponseV2(
                new CommonUserResponse(
                        user != null ? user.getId() : null,
                        user != null ? user.getNickname() : null,
                        user != null ? user.getProfileImgUrl() : null
                ),
                new CommonPostDetailResponse(
                        post.getPostId(),
                        post.getDeliveryLocation().getCoordinate().getX(),
                        post.getDeliveryLocation().getCoordinate().getY(),
                        post.getTitle(),
                        post.getTargetTime(),
                        post.getTargetPrice(),
                        post.getDeliveryPrice(),
                        post.getOrderedPrice(),
                        post.getContent(),
                        post.getStoreName(),
                        postImgUrls
                ),
                ordered
                );
        return new Result(getPostResponseV2);
    }

    /**
     * 게시물 업데이트 API
     * [Patch] http://localhost:8080/posts/{postId}
     */
    @PatchMapping("/v1/posts/{postId}")
    public UpdatePostResponse updatePost(
            @PathVariable Long postId,
            @RequestBody UpdatePostRequest request) {
        postService.update(postId, request.getTitle(), request.getContent());
        Post findPost = postService.findOne(postId);
        return new UpdatePostResponse(findPost.getTitle(), findPost.getContent());
    }

}
