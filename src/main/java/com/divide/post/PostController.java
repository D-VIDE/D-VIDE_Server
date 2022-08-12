package com.divide.post;

import com.divide.common.CommonPostResponse;
import com.divide.common.CommonUserResponse;
import com.divide.post.domain.Post;
import com.divide.post.dto.request.PostPostRequest;
import com.divide.post.dto.request.UpdatePostRequest;
import com.divide.post.dto.request.GetPostsRequest;
import com.divide.post.dto.response.*;
import com.divide.user.User;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.io.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Api(tags = "Post API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {
    private final PostService postService;

    /**
     * 게시물 생성 API
     * [POST] http://localhost:8080/api/v1/post
     *
     * @param request
     * @param userId
     * @param postImageFiles : 업로드할 파일 리스트
     * @return
     * @throws ParseException
     */
    @PostMapping(value = "/v1/post")
    public ResponseEntity<PostPostResponse> createPost(@RequestPart @Valid PostPostRequest request, @RequestParam Long userId, @RequestPart MultipartFile... postImageFiles) throws ParseException {

        Long newPostId = postService.createPost(userId, request, postImageFiles);

        return ResponseEntity.ok().body(new PostPostResponse(newPostId));
    }

    @GetMapping("/v1/posts")
    @Deprecated
    public Result getPostsV1(@Valid GetPostsRequest getPostsRequest){
        List<Post> findPosts = postService.findPostsAll(getPostsRequest.getFirst(), getPostsRequest.getLatitude(), getPostsRequest.getLongitude(), 0.5, getPostsRequest.getCategory());
        List<GetPostsResponseV1> collect = findPosts.stream()
                .map( p -> new GetPostsResponseV1(p))
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
                                    user.getId(),
                                    user.getNickname(),
                                    user.getProfileImgUrl()
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
