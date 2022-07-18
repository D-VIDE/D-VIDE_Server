package com.divide.post;

import com.divide.user.User;
import com.divide.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) //JPA를 사용하여 데이터 저장및 변경하기 위해서
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    /**
     * 게시글 생성
     */
    @Transactional
    public Long create(Post post){
        //TODO: 비즈니스 로직 추가
        postRepository.save(post);
        return post.getPostId();
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
    public Post findOne(Long postId){
        return postRepository.findByPostId(postId);
    }


    /**
     *게시글 title로 조회
     */
    public List<Post> findTitle(String title){
        return postRepository.findByTitle(title);
    }

    @Transactional
    public void update(Long postId, String title, String content) {
        Post post = postRepository.findByPostId(postId);
        post.setTitle(title);
        post.setContent(content);
    }
}
