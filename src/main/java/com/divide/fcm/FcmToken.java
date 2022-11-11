package com.divide.fcm;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Getter
public class FcmToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fcmtoken_id")
    private Long id;

    @NotEmpty
    private String token;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public void updateToken(String token) {
        this.token = token;
    }
}
