package com.example.simplelogin.auth.entity;

import com.example.simplelogin.auth.model.RefreshTokenResponse;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "refresh_token")
public class RefreshToken {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(nullable = false, unique = true)
    private String token;

    private Long expiredAt;

    public RefreshToken(User user, String token, Long expiredAt) {
        this.user = user;
        this.token = token;
        this.expiredAt = expiredAt;
    }

    public RefreshToken() {
    }

    public String getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getToken() {
        return token;
    }

    public Long getExpiredAt() {
        return expiredAt;
    }

    public RefreshTokenResponse toRefreshTokenResponse() {
        return new RefreshTokenResponse(id, user.toUserResponse(), token, expiredAt);
    }
}
