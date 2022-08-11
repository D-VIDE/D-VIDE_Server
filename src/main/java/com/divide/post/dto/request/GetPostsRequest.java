package com.divide.post.dto.request;

import com.divide.post.domain.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class GetPostsRequest {
    @NotNull
    private Double latitude;
    @NotNull
    private Double longitude;
    @Nullable
    private Category category;
    @Nullable
    private Integer first = 0;
}
