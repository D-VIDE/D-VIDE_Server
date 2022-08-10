package com.divide.post;

import com.divide.post.domain.Category;
import com.divide.post.domain.Post;
import com.divide.post.dto.request.PostPostRequest;
import com.divide.post.dto.request.UpdatePostRequest;
import com.divide.post.dto.request.GetNearbyPostsRequest;
import com.divide.post.dto.response.*;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.io.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    public ResponseEntity<PostPostResponse> post(@RequestBody @Valid PostPostRequest request, @RequestParam Long userId ) throws ParseException {

        Long newPostId = postService.post(userId, request);

        return ResponseEntity.ok().body(new PostPostResponse(newPostId));
    }

    /**
     * 카테고리별 500m이내 게시글 10개 조회 API
     *  [GET] http://localhost:8080/api/v1/posts/nearby/"KOREAN_FOOD"?longitude=37.49015482509&latitude=127.030767490
     *  category 종류: STREET_FOOD, KOREAN_FOOD, JAPANESE_FOOD, CHINESE_FOOD, DESSERT, WESTERN_FOOD
     *
     */
    @GetMapping("/posts")
    public Result findNearbyCategoryPosts(@RequestParam("latitude") Double latitude, @RequestParam("longitude") Double longitude, @RequestParam(value = "category", required = false) Category category){
        System.out.println("latitude = " + latitude + ", longitude = " + longitude + ", category = " + category);
        List<Post> findPosts = postService.getNearByRestaurants(latitude, longitude, 2.5, category, 0);

        List<GetNearbyPostsResponse> collect = findPosts.stream()
                .map( p -> new GetNearbyPostsResponse(p))
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
