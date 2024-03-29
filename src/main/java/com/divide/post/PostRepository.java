package com.divide.post;

import com.divide.post.domain.Category;
import com.divide.post.domain.Post;

import com.divide.user.User;
import java.util.List;

public interface PostRepository {
    void save(Post post);
    Post findByPostId(Long postId);
    List<Post> findByTitle(String title);
    List<Post> findPostsAll(Integer first, String pointFormat);
    List<Post> findPostsByCategory(Integer first, String pointFormat, Category category);
    List<Post> findAll();

    void setNullAllByUser(User user);
}
