package com.divide.post;

import com.divide.post.domain.Post;
import com.divide.post.dto.request.postPostRequest;
import com.divide.post.dto.request.UpdatePostRequest;
import com.divide.post.dto.request.getNearByPostsRequest;
import com.divide.post.dto.response.*;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.io.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Api(tags= "Post API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class PostController {
    private final PostService postService;

    /**
     * 게시물 생성 API - Entity 매핑
     * [POST] http://localhost:8080/api/v1/post?userId=1
     * @param
     * @return
     */
    @PostMapping(value = "/post")
    public ResponseEntity<postPostResponse> post(@RequestBody postPostRequest request, @RequestParam Long userId ) throws ParseException {

        Long newPostId = postService.post(userId, request );

        return ResponseEntity.ok().body(new postPostResponse(newPostId));
    }


    /**
     * 게시물 전체 조회 API
     * [GET] http://localhost:8080/api/v1/posts
     * @return
     */
    @GetMapping("/posts")
    public Result posts(){ //json 데이터 확장성을 위해 Result 사용
        List<Post> findPosts = postService.findPosts();
        List<getPostsResponse> collect = findPosts.stream()
                .map( p -> new getPostsResponse(p))
                .collect(toList());
        return new Result(collect);
    }

    /**
     * 게시물 가까운 식당(500m안) 게시글 조회 API
     *  [GET] http://localhost:8080/api/v1/nearByPosts
     *
     */
    @GetMapping("/nearByPosts")
    public Result findNearestPosts(@RequestBody getNearByPostsRequest request){ //json 데이터 확장성을 위해 Result 사용
        List<Post> findPosts = postService.getNearByRestaurants(request.getLongitude(), request.getLatitude(), 0.5);

        List<getNearByPostsResponse> collect = findPosts.stream()
                .map( p -> new getNearByPostsResponse(p))
                .collect(toList());
        return new Result(collect);
    }

    /**
     * 게시물 업데이트 API
     * [Patch] http://localhost:8080/posts/{postId}
     */
    @PatchMapping("/posts/{postId}")
    public UpdatePostResponse updatePost(
            @PathVariable Long postId,
            @RequestBody UpdatePostRequest request) {
        postService.update(postId, request.getTitle(), request.getContent());
        Post findPost = postService.findOne(postId);
        return new UpdatePostResponse(findPost.getTitle(), findPost.getContent() ) ;
    }

}
