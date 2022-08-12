package com.divide.post.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter @Setter
@Entity
@Table(name = "POST_IMAGE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postImageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
//    @NotNull
    private Post post;

    @NotNull
    private String postImageUrl;

    //==생성메서드==
    public static PostImage create(String postImageUrl){
        PostImage postImage = new PostImage();
        postImage.setPostImageUrl(postImageUrl);
        return postImage;
    }

}
