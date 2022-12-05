package com.example.toyneworkproject.domain;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.UUID;

public class UserLoginInfo extends Entity<UUID>{
    private String userEncryptedPassword;
    private String userSalt;

    private UUID userUUID;

    public UserLoginInfo(String userEncryptedPassword, String userSalt,UUID userUUID) {
        this.userEncryptedPassword = userEncryptedPassword;
        this.userSalt = userSalt;
        this.userUUID = userUUID;
    }

    public String getUserEncryptedPassword() {
        return userEncryptedPassword;
    }

    public void setUserEncryptedPassword(String userEncryptedPassword) {
        this.userEncryptedPassword = userEncryptedPassword;
    }

    public String getUserSalt() {
        return userSalt;
    }

    public void setUserSalt(String userSalt) {
        this.userSalt = userSalt;
    }

    public UUID getUserUUID() {
        return userUUID;
    }

    public void setUserUUID(UUID userUUID) {
        this.userUUID = userUUID;
    }
}
