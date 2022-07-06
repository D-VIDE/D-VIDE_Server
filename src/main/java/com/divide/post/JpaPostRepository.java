package com.divide.post;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
public class JpaPostRepository implements PostRepository{
    private final EntityManager em; //JPA를 사용하려면 EntityManager를 주입받아야함
    public JpaPostRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Post save(Post post) {
        em.persist(post); //JPA가 insert query만들어서 setId까지 해준다
        return post;
    }

    @Override
    public Optional<Post> findByPostId(Long postId) {
        //PK의 경우, 이런식으로 조회가능
        Post post = em.find(Post.class, postId); //postId는 PK
        return Optional.ofNullable(post);
    }

    @Override
    public Optional<Post> findByTitle(String title) { //JPQL 객체지향 쿼리를 사용해야함
        List<Post> result = em.createQuery("select p from Post p where p.title = :title", Post.class)
                .setParameter("title", title)
                .getResultList();
        return result.stream().findAny();
    }

    @Override
    public List<Post> findAll() {
        List<Post> result = em.createQuery("select p from Post p", Post.class)
                .getResultList();
        return result;
    }
}
