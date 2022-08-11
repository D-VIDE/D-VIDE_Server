package com.divide.post;

import com.divide.post.domain.Category;
import com.divide.post.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class JpaPostRepository implements PostRepository{
    private final EntityManager em; //JPA를 사용하려면 EntityManager를 주입받아야함

    @Override
    public void save(Post post) {
        em.persist(post); //JPA가 insert query만들어서 setId까지 해준다
    }

    @Override
    public Post findByPostId(Long postId) {
        //PK의 경우, 이런식으로 조회가능
        Post post = em.find(Post.class, postId); //postId는 PK
        return post;
    }

    @Override
    public List<Post> findByTitle(String title) { //JPQL 객체지향 쿼리를 사용해야함
        List<Post> result = em.createQuery("select p from Post p where p.title = :title", Post.class)
                .setParameter("title", title)
                .getResultList();
        return result;
    }

    @Override
    public List<Post> findPostsAll(Integer first, String pointFormat) {
        return em.createNativeQuery("SELECT * FROM post AS p WHERE MBRContains(ST_LINESTRINGFROMTEXT(:pointFormat), p.delivery_location)", Post.class)
                .setParameter("pointFormat", pointFormat)
                .setMaxResults(10)
                .setFirstResult(first)
                .getResultList();
    }

    @Override
    public List<Post> findPostsByCategory(Integer first, String pointFormat, Category category) {
        return em.createNativeQuery("SELECT * FROM post AS p WHERE MBRContains(ST_LINESTRINGFROMTEXT(:pointFormat), p.delivery_location) AND p.category = :category", Post.class)
                .setParameter("pointFormat", pointFormat)
                .setParameter("category", category.toString())
                .setMaxResults(10)
                .setFirstResult(first)
                .getResultList();
    }

    @Override
    public List<Post> findAll() {
        List<Post> result = em.createQuery("select p from Post p", Post.class)
                .getResultList();
        return result;
    }
}
