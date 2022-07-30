package com.divide.post;

import com.divide.post.domain.Post;

import java.util.List;

public interface PostRepository {
    void save(Post post);
    Post findByPostId(Long postId);
    List<Post> findByTitle(String title);
    List<Post> findAll();
}
