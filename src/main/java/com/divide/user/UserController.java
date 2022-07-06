package com.divide.user;

import com.divide.BaseException;
import com.divide.BaseResponse;
import com.divide.BaseResponseStatus;
import com.divide.user.dto.request.SignupRequest;
import com.divide.user.dto.response.GetUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;

    @GetMapping("/user/all")
    public BaseResponse getUsers() {
        List<User> users = userService.getUsers();
        List<GetUserResponse> res = users.stream().map(user -> new GetUserResponse(user.getId(), user.getNickname())).toList();
        return new BaseResponse(res);
    }

    @PostMapping("/signup")
    public BaseResponse signup(@RequestBody @Valid SignupRequest signupRequest) {
        try {
            userService.signup(signupRequest);
        } catch (BaseException e) {
            return new BaseResponse(e.getStatus());
        }
        return new BaseResponse(BaseResponseStatus.SUCCESS);
    }

    /**
     * 현재 자신의 정보
     */
    @GetMapping("/user")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public GetUserResponse getUser() {
        User user = userService.getMyUser();
        return null;
    }
}
