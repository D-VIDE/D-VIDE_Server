package com.divide.user;

import com.divide.exception.RestApiException;
import com.divide.exception.code.UserErrorCode;
import com.divide.location.Location;
import com.divide.user.UserBadge.BadgeName;
import com.divide.user.dto.request.PatchUserRequest;
import com.divide.user.dto.request.SignupRequest;
import com.divide.utils.OCIUtil;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final UserBadgeRepository userBadgeRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public User getUserById(Long userId) {
        return userRepository.findById(userId);
    }

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

    @Transactional(readOnly = true)
    public List<UserBadge> getBadgeList(User user) {
        return userBadgeRepository.findByUser(user);
    }

    public void updateSelectedBadge(User user, UserBadge.BadgeName badgeName) {
        UserBadge userBadge = userBadgeRepository.findByUserAndBadgeName(user, badgeName);
        if (user.getSelectedBadge().equals(userBadge)) {
            throw new RestApiException(UserErrorCode.ALREADY_SET_BADGE_NAME);
        }
        user.updateSelectedBadge(userBadge);
    }

    public Long saveUserBadge(User user, UserBadge.BadgeName badgeName) {
        boolean badgeExists = userBadgeRepository.findByUser(user).stream()
                .map(UserBadge::getBadgeName)
                .anyMatch(bn -> bn.equals(badgeName));
        if (badgeExists) {
            throw new RestApiException(UserErrorCode.ALREADY_REGISTERED_BADGE);
        }
        UserBadge userBadge = new UserBadge(user, badgeName);
        userBadgeRepository.save(userBadge);
        return userBadge.getId();
    }

    public void updateLocation(User user, Double latitude, Double longitude) {
        Location newLocation = new Location(latitude, longitude);
        user.updateLocation(newLocation);
    }

    public void updateFcmToken(User user, String fcmToken) {
        user.updateToken(fcmToken);
    }

    public void updateNickname(User user, String nickname) {
        user.updateNickname(nickname);
    }

    public void updateProfileImg(User user, MultipartFile profileImgFile) {
        String extension = StringUtils.getFilenameExtension(profileImgFile.getOriginalFilename()).toLowerCase();
        String profileImgUrl = OCIUtil.uploadFile(profileImgFile, OCIUtil.FolderName.PROFILE,  user.getEmail() + "/" + UUID.randomUUID() + "." + extension);

        user.updateProfileImgUrl(profileImgUrl);
    }

    public void updateUserInformation(User user, PatchUserRequest patchUserRequest) {
        if (patchUserRequest.getNickname() != null) {
            updateNickname(user, patchUserRequest.getNickname());
        }
        if (patchUserRequest.getBadgeName() != null) {
            List<UserBadge> badgeList = getBadgeList(user);
            Optional<BadgeName> badgeName = badgeList.stream()
                    .map(UserBadge::getBadgeName)
                    .filter(name -> name.getKrName().equals(patchUserRequest.getBadgeName()))
                    .findFirst();
            updateSelectedBadge(user, badgeName.orElseThrow(() -> new RestApiException(UserErrorCode.INVALID_BADGE_NAME)));
        }
        if (patchUserRequest.getFcmToken() != null) {
            updateFcmToken(user, patchUserRequest.getFcmToken());
        }
        if (patchUserRequest.getLatitude() != null && patchUserRequest.getLongitude() != null) {
            updateLocation(user, patchUserRequest.getLatitude(), patchUserRequest.getLongitude());
        }
        if (patchUserRequest.getProfileImgFile() != null) {
            updateProfileImg(user, patchUserRequest.getProfileImgFile());
        }
    }
}
