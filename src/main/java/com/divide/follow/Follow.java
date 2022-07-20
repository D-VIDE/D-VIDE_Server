package com.divide.follow;

import com.divide.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Entity
@NoArgsConstructor
public class Follow {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follow_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private User followee;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private User follower;
}
