package com.divide.order.dto.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@NoArgsConstructor
public class PostOrderRequest {
    @NotNull
    private Long postId;
    @PositiveOrZero
    @NotNull
    private Integer orderPrice;
}
