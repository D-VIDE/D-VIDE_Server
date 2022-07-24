package com.divide.post;

import java.util.List;

public interface PostRepository {
    void save(Post post);
    Post findByPostId(Long postId);
    List<Post> findByTitle(String title);
    List<Post> findAll();
}
