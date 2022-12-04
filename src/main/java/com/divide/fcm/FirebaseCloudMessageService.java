package com.divide.fcm;

import static com.divide.exception.code.FcmErrorCode.FCM_ERROR;
import static com.divide.exception.code.FcmErrorCode.FCM_PARSING_ERROR;

import com.divide.exception.RestApiException;
import com.divide.exception.code.FcmErrorCode;
import com.divide.user.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.net.HttpHeaders;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FirebaseCloudMessageService {
    private static final String API_URL = "https://fcm.googleapis.com/v1/projects/divide-de7a4/messages:send";
    private final ObjectMapper objectMapper;

    public void sendMessageTo(User user, String title, String body) {
        try {
            String fcmToken = user.getFcmToken()
                    .orElseThrow(() -> new RestApiException(FcmErrorCode.FCM_CODE_NOT_AVAILABLE));
            String message = makeMessage(fcmToken, title, body);

            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = RequestBody.create(message, MediaType.get("application/json; charset=utf-8"));
            Request request = new Request.Builder()
                    .url(API_URL)
                    .post(requestBody)
                    .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                    .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                    .build();

            Response response = client.newCall(request).execute();

            int code = response.code();
            if (code != 200) {
//                throw new RestApiException(FCM_ERROR);
            }
        } catch (Exception e) {
            // Do Nothing
            System.out.println(e);
        }
//        catch (IOException e) {
//            throw new RestApiException(FCM_PARSING_ERROR);
//        }
    }

    private String makeMessage(String targetToken, String title, String body)
            throws JsonProcessingException {
        FcmMessage fcmMessage = FcmMessage.builder()
                .message(FcmMessage.Message.builder()
                        .token(targetToken)
                        .notification(FcmMessage.Notification.builder()
                                .title(title)
                                .body(body)
                                .image(null)
                                .build()
                        )
                        .build()
                )
                .validate_only(false)
                .build();

        return objectMapper.writeValueAsString(fcmMessage);
    }

    private String getAccessToken() throws IOException {
        String firebaseConfigPath = "/config/fcm_key.json";

        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }

}

