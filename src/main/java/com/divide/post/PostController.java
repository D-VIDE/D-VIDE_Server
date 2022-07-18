package com.divide.post;

import com.divide.post.dto.request.CreatePostRequest;
import com.divide.post.dto.response.CreatePostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;


    /**
     * 게시물 생성 API
     * @API [POST] http://localhost:8080/posts
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

}
