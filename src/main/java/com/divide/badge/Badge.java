package com.divide.badge;

import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
public class Badge {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "badge_id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private String BadgeName;

    @NotNull
    private String iconUrl;

    public enum BadgeName {
        CHICKEN_KILLER, // TODO: 뱃지 이름 정의
    }
}
