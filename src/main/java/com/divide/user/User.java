package com.divide.user;

import com.divide.post.domain.Post;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

    @NotEmpty
    @JsonIgnore
    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<UserBadge> badges = new HashSet<>();

    @NotNull
    @PositiveOrZero
    private Integer savedMoney = 0;

    @CreatedDate
    private LocalDateTime createdAt;


    @JsonIgnore
    @OneToMany(mappedBy = "user") //읽기 전용, 매핑된 거울!
    private List<Post> posts = new ArrayList<>();

    public User(String email, String password, String profileImgUrl, String nickname, UserRole role) {
        this.email = email;
        this.password = password;
        this.profileImgUrl = profileImgUrl;
        this.nickname = nickname;
        this.role = role;
        this.badges.add(new UserBadge(this, UserBadge.BadgeName.DIVIDER));
    }

    public void addBadge(UserBadge.BadgeName badge) {
        badges.add(new UserBadge(this, badge));
    }
}
