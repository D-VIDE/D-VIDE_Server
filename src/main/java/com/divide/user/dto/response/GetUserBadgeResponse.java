package com.divide.user.dto.response;

import com.divide.common.CommonBadgeResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class GetUserBadgeResponse {
    private List<CommonBadgeResponse> badges;
}
