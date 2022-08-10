package com.divide.post;

import com.divide.post.domain.Category;
import com.divide.post.domain.Post;

import java.util.List;

public interface PostRepository {
    void save(Post post);
    Post findByPostId(Long postId);
    List<Post> findByTitle(String title);
    List<Post> findNearByRestaurantsAll(int first, String pointFormat);
    List<Post> findNearByRestaurantsByCategory(int first, String pointFormat, Category category);
    List<Post> findAll();
}
