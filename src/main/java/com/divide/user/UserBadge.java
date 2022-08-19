package com.divide.user;

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
        CHICKEN("가슴이 통닭통닭"),
        ZHANG_CHEN("장첸"),
        INSIDER_DIVIDER("인싸 디바이더"),
        LONELY_GOURMET("고독한 미식가"),
        SWEET_DIVIDER("서윗 드바이더"),
        CAFFEINE_VAMPIRE("카페인 뱀파이어"), // TODO: 뱃지 조건.... 어떻게 해요 ㅠㅜ
        ;

        private String krName;
    }

    public UserBadge(User user, BadgeName badgeName) {
        this.user = user;
        this.badgeName = badgeName;
    }
}
