package com.divide.post;

import com.divide.post.dto.request.CreatePostRequest;
import com.divide.post.dto.request.CreatePostRequestEntity;
import com.divide.post.dto.request.UpdatePostRequest;
import com.divide.post.dto.response.CreatePostResponse;
import com.divide.post.dto.response.Result;
import com.divide.post.dto.response.UpdatePostResponse;
import com.divide.post.dto.response.postDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.bcel.classfile.Constant;
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
     * 게시물 생성 API
     * @API [POST] http://localhost:8080/api/v1/posts
     * @param request
     * @return
     */
    @ApiOperation(value="게시물 생성" , notes="title과 content만으로 게시글을 생성합니다.")
    @PostMapping("/posts")
//    @ResponseBody //http body부분에 객체를 JSON으로 반환해줌
    public ResponseEntity <CreatePostResponse> createPost(@RequestBody CreatePostRequest request){
        Post post = new Post();
//        post.setUserId(request.getUserId());
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());

        Long newPostId = postService.create(post);

        return ResponseEntity.ok().body(new CreatePostResponse(newPostId));
    }
//    /**
//     * 게시물 생성 API - Entity 매핑
//     * @param userId
//     * @return
//     */
//    @PostMapping(value = "/post")
//    public ResponseEntity<CreatePostResponse> post(@RequestBody CreatePostRequestEntity request, @RequestParam Long userId ) {
//
//        Long newPostId = postService.post(userId, request.getTitle(), request.getContent(), request.getDeliveryLocation());
//
//        return ResponseEntity.ok().body(new CreatePostResponse(newPostId));
//    }

    /**
     * 게시물 생성 API - Entity 매핑
     * @param userId
     * @return
     */
    @PostMapping(value = "/post")
    public ResponseEntity<CreatePostResponse> post(@RequestBody CreatePostRequestEntity request, @RequestParam Long userId ) {

        Long newPostId = postService.post(userId, request.getTitle(), request.getStoreName(), request.getContent(),
                request.getTargetPrice(), request.getDeliveryPrice(), request.getTargetUserCount(), request.getCategory(),
                request.getTargetTime(), request.getDeliveryLocation(), request.getPostStatus() );

        return ResponseEntity.ok().body(new CreatePostResponse(newPostId));
    }


    /**
     * 게시물 전체 조회 API
     * [GET] http://localhost:8080/api/v1/posts
     * @return
     */
    @GetMapping("/posts")
    public Result posts(){ //json 데이터 확장성을 위해 Result 사용
        List<Post> findPosts = postService.findPosts();
        List<postDto> collect = findPosts.stream()
                .map( p -> new postDto(p))
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
