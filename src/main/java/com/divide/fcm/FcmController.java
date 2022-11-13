package com.divide.fcm;

import com.divide.fcm.dto.RequestDTO;
import com.divide.user.User;
import com.divide.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class FcmController {
    private final FirebaseCloudMessageService firebaseCloudMessageService;
    private final UserService userService;

    @PostMapping("/api/fcm")
    public ResponseEntity pushMessage(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody RequestDTO requestDTO
    ) {
        User targetUser = userService.getUserByEmail(userDetails.getUsername());

        firebaseCloudMessageService.sendMessageTo(
                targetUser,
                requestDTO.getTitle(),
                requestDTO.getBody());
        return ResponseEntity.ok().build();
    }
}
