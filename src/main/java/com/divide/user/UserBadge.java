package com.divide.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Getter
public class UserBadge {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_badge_id")
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    @Enumerated(EnumType.STRING)
    private BadgeName badgeName;

    @CreatedDate
    private LocalDateTime createdAt;

    @AllArgsConstructor
    @Getter
    public enum BadgeName {
        BEGINNER("디바이드비기너", "디바이드에 가입함"),
        CHICKEN("가슴이 통닭통닭", "치킨 총 주문횟수 3회 이상"),
        ZHANG_CHEN("장첸", "마라탕 총 주문횟수 5회 이상"),
        INSIDER_DIVIDER("인싸 디바이더", "팔로워를 10명이상 달성해 봄"),
        LONELY_GOURMET("고독한 미식가", "주문인원이 2명인 게시글"),
        SWEET_DIVIDER("서윗 드바이더", "하트를 단 리뷰가 5개 이상"),
        CAFFEINE_VAMPIRE("카페인 뱀파이어", "카페 총 주문횟수 10회 이상"),
        ;

        private String krName;
        private String description;
    }

    public UserBadge(User user, BadgeName badgeName) {
        this.user = user;
        this.badgeName = badgeName;
    }
}
