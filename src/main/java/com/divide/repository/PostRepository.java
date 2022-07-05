package com.divide.repository;

import com.divide.entity.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository {
    Post save(Post post);
    Optional<Post> findByPostId(Long postId);
    Optional<Post> findByTitle(String title);
    List<Post> findAll();
}
