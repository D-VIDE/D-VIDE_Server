package com.divide.controller;

import com.divide.dto.request.NewPostRequest;
import com.divide.dto.response.NewPostResponse;
import com.divide.entity.Post;
import com.divide.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/posts/new") //데이터를 form 같은곳에 넣어서 전달할 때 사용
//    @ResponseBody //http body부분에 객체를 JSON으로 반환해줌
    public ResponseEntity<NewPostResponse> createPost(@RequestBody NewPostRequest newPostRequest){
        Post post = new Post();
        post.setTitle(newPostRequest.getTitle());
        post.setContent(newPostRequest.getContent());

        Long result = postService.create(post);

        return ResponseEntity.ok().body(new NewPostResponse(result));
    }


    @GetMapping("posts")
    public String list(Model model){
        List<Post> postList = postService.findPosts();
        model.addAttribute("members", postList);
        return "members/memberList";
    }
}
