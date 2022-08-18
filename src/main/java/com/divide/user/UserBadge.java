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
        DIVIDER("디바이더"), CHICKEN_KILLER("치킨 킬러"), // TODO: 뱃지 이름 정의
        ;

        private String krName;
    }

    public UserBadge(User user, BadgeName badgeName) {
        this.user = user;
        this.badgeName = badgeName;
    }
}
