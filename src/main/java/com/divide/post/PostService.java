package com.divide.post;

import com.divide.post.domain.Category;
import com.divide.post.domain.Direction;
import com.divide.utils.GeometryUtil;
import com.divide.post.domain.Location;
import com.divide.post.domain.Post;
import com.divide.post.dto.request.PostPostRequest;
import com.divide.user.User;
import com.divide.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.divide.post.domain.PostStatus.RECRUITING;

@Service
@Transactional(readOnly = true) //JPA를 사용하여 데이터 저장및 변경하기 위해서
@RequiredArgsConstructor
public class PostService {
    private final EntityManager em;
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
        post.updateInfo(title, content);
    }

    /**
     *게시글 생성: user가 작성한 게시글
     */
    @Transactional
    public Long post( Long userId, PostPostRequest request) throws ParseException {
        //엔티티 조회
        User user = userRepository.findById(userId);

        //deliveryLocation: String -> point로 변환
        String pointWKT = String.format("POINT(%s %s)", request.getLongitude(), request.getLatitude());
        Point point = (Point) new WKTReader().read(pointWKT);

        //주문 생성
        Post post = Post.builder()
                .user(user)
                .title(request.getTitle())
                .storeName(request.getStoreName())
                .content(request.getContent())
                .targetPrice(request.getTargetPrice())
                .deliveryPrice(request.getDeliveryPrice())
                .category(request.getCategory())
                .targetTime(request.getTargetTime())
                .deliveryLocation(point)
                .postStatus(RECRUITING)
                .build();
        postRepository.save(post);

        return post.getPostId();
    }

    /**
     * 게시글 거리기반 조회
     * @param longitude : 기준좌표 x
     * @param latitude  : 기준좌표 y
     * @param distance  : 기준 좌표 x,y로 부터 distanceKM 떨어진 모든 범위
     * @param category  : 사용자가 선택한 카테고리
     *
     */
    public List<Post> getNearByRestaurants(Double latitude, Double longitude, Double distance, Category category, int first) {
        String pointFormat = getPointFormat(latitude, longitude, distance);
        if (category == null) return postRepository.findNearByRestaurantsAll(first, pointFormat);
        else return postRepository.findNearByRestaurantsByCategory(first, pointFormat, category);
    }

    private String getPointFormat(Double latitude, Double longitude, Double distance) {
        //일정 거리 범위 내에있는 좌표들을 비교하기 위해서 MBR이 필요
        //MBR을 구하기 위해 북동쪽, 남서쪽 좌표 구하기
        Location northEast = GeometryUtil
                .calculate(latitude, longitude, distance, Direction.NORTHEAST.getBearing());
        Location southWest = GeometryUtil
                .calculate(latitude, longitude, distance, Direction.SOUTHWEST.getBearing());
        // 기준 좌표의 북동쪽으로 nKM에 위치한 좌표 : x1, y1
        double x1 = northEast.getLongitude();
        double y1 = northEast.getLatitude();
        // 기준 좌표의 남서쪽으로 nKM에 위치한 좌표 : x2, y2
        double x2 = southWest.getLongitude();
        double y2 = southWest.getLatitude();

        //기준 좌표 x,y로 부터 distanceKM 떨어진 모든 범위의 delivery_location 데이터를 조회하는 쿼리
        String pointFormat = String.format("LINESTRING(%f %f, %f %f)", x1, y1, x2, y2);
        return pointFormat;
    }

}
