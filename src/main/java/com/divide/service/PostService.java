package com.divide.service;

import com.divide.entity.Post;
import com.divide.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional //JPA를 사용하여 데이터 저장및 변경하기 위해서
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }
    /**
     * 게시글 생성
     */
    public Long create(Post post){
        //TODO: 비즈니스 로직 추가
        postRepository.save(post);
        return post.getPostIdx();
    }


    /**
     *전체 게시글 조회
     */
    public List<Post> findPosts(){
        return postRepository.findAll();
    }

    /**
     *게시글 id로 조회
     */
    public Optional<Post> findOne(Long postId){
        return postRepository.findByPostId(postId);
    }

    public Optional<Post> findTitle(String title){
        return postRepository.findByTitle(title);
    }

}
