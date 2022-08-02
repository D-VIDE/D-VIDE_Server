package com.divide.follow.dto.request;

import com.divide.follow.Follow;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class GetFollowResponse {
    List<Follow> followList;
}
