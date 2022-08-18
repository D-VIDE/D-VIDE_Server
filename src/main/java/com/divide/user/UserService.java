package com.divide.user;

import com.divide.exception.RestApiException;
import com.divide.exception.code.UserErrorCode;
import com.divide.user.dto.request.SignupRequest;
import com.divide.utils.OCIUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(""));
    }

    public Long signup(SignupRequest signupRequest) {
        String email = signupRequest.getEmail();
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RestApiException(UserErrorCode.DUPLICATED_USER);
        }
        MultipartFile profileImg = signupRequest.getProfileImg();
        String extension = StringUtils.getFilenameExtension(profileImg.getOriginalFilename()).toLowerCase();
        String profileImgUrl = OCIUtil.uploadFile(profileImg, OCIUtil.FolderName.PROFILE,  email + "/" + UUID.randomUUID() + "." + extension);

        User newUser = new User(
                email,
                passwordEncoder.encode(signupRequest.getPassword()),
                profileImgUrl,
                signupRequest.getNickname(),
                UserRole.USER
        );
        userRepository.save(newUser);

        return newUser.getId();
    }
}
