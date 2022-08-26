package com.divide.push;

import com.divide.push.dto.RequestPushMessage;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("notification")
public class NotificationsController {

    @Autowired
    NotificationsService notificationsService;

//    @Autowired
//    CoreUserService coreUserService;


    @Value("${project.properties.firebase-multicast-message-size}")
    Long multicastMessageSize;


    @ApiOperation(value = "토픽푸쉬")
    @PostMapping(value = "/pushs/topics/{topic}")
    public void notificationTopics(@PathVariable("topic") String topic, @RequestBody RequestPushMessage data) throws FirebaseMessagingException {
        Notification notification = Notification.builder().setTitle(data.getTitle()).setBody(data.getBody()).build();
        Message.Builder builder = Message.builder();
        Message msg = builder.setTopic(topic).setNotification(notification).build();
        notificationsService.sendMessage(msg);
    }

//    @ApiOperation(value = "전고객푸쉬")
//    @PostMapping(value = "/pushs/users")
//    public void notificationUsers(@RequestBody RequestPushMessage data) throws IOException, FirebaseMessagingException {
//        List<CoreUser> targetUser = null == data.getUserNos() ? coreUserService.findAllByEnabledAndPushTokenIsNotNull(UseCd.USE001) : coreUserService.findAllByEnabledAndPushTokenIsNotNullAndNoIn(UseCd.USE001, data.getUserNos());
//        AtomicInteger counter = new AtomicInteger();
//        Collection<List<CoreUser>> sendUserGroups = targetUser.stream().collect(Collectors.groupingBy(it -> counter.getAndIncrement() / multicastMessageSize.longValue())).values();
//        for (List<CoreUser> it : sendUserGroups) {
//            Notification notification = Notification.builder().setTitle(data.getTitle()).setBody(data.getBody()).setImage(data.getImage()).build();
//            MulticastMessage.Builder builder = MulticastMessage.builder();
//            Optional.ofNullable(data.getData()).ifPresent(sit -> builder.putAllData(sit));
//            MulticastMessage message = builder
//                    .setNotification(notification)
//                    .addAllTokens(it.stream().map(sit -> sit.getPushToken()).collect(Collectors.toList()))
//                    .build();
//            this.fcmService.sendMessage(message);
//        }
//    }
//
//
//    @ApiOperation(value = "특정 고객푸쉬")
//    @PostMapping(value = "/pushs/users/{no}")
//    public void notificationUser(@PathVariable("no") Long no, @RequestBody RequestPushMessage data) throws FirebaseMessagingException {
//        Optional<CoreUser> user = coreUserService.findById(no);
//        if (user.isPresent()) {
//            CoreUser it = user.get();
//            Notification notification = Notification.builder().setTitle(data.getTitle()).setBody(data.getBody()).setImage(data.getImage()).build();
//            Message.Builder builder = Message.builder();
//            Optional.ofNullable(data.getData()).ifPresent(sit -> builder.putAllData(sit));
//            Message msg = builder.setToken(it.getPushToken()).setNotification(notification).build();
//            fcmService.sendMessage(msg);
//        }
//    }
}
