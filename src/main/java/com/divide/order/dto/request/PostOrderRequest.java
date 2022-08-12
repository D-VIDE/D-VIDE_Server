package com.divide.order.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class PostOrderRequest {
    @NotNull
    private Long postId;
    @PositiveOrZero
    @NotNull
    private Integer orderPrice;
    @Size(min=1, max=3)
    @NotNull
    private List<MultipartFile> orderImg;
}
