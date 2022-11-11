package com.divide.user;

import com.divide.location.Location;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.annotation.Nullable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    @Email @NotNull
    @Column(unique = true)
    private String email;
    @NotNull
    private String password;
    @NotNull
    private String profileImgUrl;
    @NotNull
    private String nickname;

    @NotNull
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private UserBadge selectedBadge;

    @NotNull
    @PositiveOrZero
    private Integer savedMoney = 0;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @Nullable
    private Location location;

    @CreatedDate
    private LocalDateTime createdAt;

    public User(String email, String password, String profileImgUrl, String nickname, UserRole role) {
        this.email = email;
        this.password = password;
        this.profileImgUrl = profileImgUrl;
        this.nickname = nickname;
        this.role = role;
        this.selectedBadge = new UserBadge(this, UserBadge.BadgeName.BEGINNER);
    }

    public void addSavedMoney(int value) {
        savedMoney += value;
    }

    public void updateSelectedBadge(UserBadge userBadge) {
        this.selectedBadge = userBadge;
    }

    public void updateLocation(Location location) {
        if (this.location == null) {
            this.location = location;
        } else {
            this.location.update(location.getLatitude(), location.getLongitude());
        }
    }
}
