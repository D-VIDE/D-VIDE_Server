package com.divide.config;

import com.divide.repository.JpaPostRepository;
import com.divide.repository.PostRepository;
import com.divide.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
//
//@Configuration
//public class SpringConfig {
//    private EntityManager em;
//
//    @Autowired
//    public SpringConfig(EntityManager em) {
//        this.em = em;
//    }
//
//    @Bean
//    public PostService postService() {
//        return new PostService(postRepository());
//    }
//    @Bean
//    public PostRepository postRepository() {
//// return new MemoryMemberRepository();
//        return new JpaPostRepository(em);
//    }
//}
