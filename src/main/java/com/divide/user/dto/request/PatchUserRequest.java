package com.divide.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PatchUserRequest {
    @Nullable
    private String nickname;
    @Nullable
    private String badgeName;
    @Nullable
    private Double latitude;
    @Nullable
    private Double longitude;
    @Nullable
    private String fcmToken;
    @Nullable
    private MultipartFile profileImgFile;
}
