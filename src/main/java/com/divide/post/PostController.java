package com.divide.post;

import com.divide.post.dto.request.CreatePostRequest;
import com.divide.post.dto.response.CreatePostResponse;
import com.divide.post.dto.response.Result;
import com.divide.post.dto.response.postDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.stream.Collectors.toList;


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

}
