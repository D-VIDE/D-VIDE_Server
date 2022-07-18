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



}
