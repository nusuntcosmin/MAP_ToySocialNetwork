package com.example.toyneworkproject.service;

import com.example.toyneworkproject.domain.UserLoginInfo;
import com.example.toyneworkproject.repository.database.RepositoryDatabaseUserLoginInfo;
import com.example.toyneworkproject.repository.factory.RepositoryFactory;
import com.example.toyneworkproject.repository.factory.UserLoginInfoRepositoryTypes;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.UUID;

public class SecurePasswordController {

    private RepositoryDatabaseUserLoginInfo repoUserLoginInfo;

    public SecurePasswordController() {
        repoUserLoginInfo = (RepositoryDatabaseUserLoginInfo) RepositoryFactory.getUserLoginInfoRepo(UserLoginInfoRepositoryTypes.DATABASE_USER_LOGIN_INFO_REPOSITORY);
    }

    public String getUserEncryptedPassword(String password, String salt) throws Exception{
        String usedAlgorithm = "PBKDF2WithHmacSHA1";
        int derivedKeyLenght = 160;
        int iterations = 20000;

        byte[] saltBytes = Base64.getDecoder().decode(salt);
        KeySpec spec = new PBEKeySpec(password.toCharArray(),saltBytes,iterations,derivedKeyLenght);
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(usedAlgorithm);

        byte[] encryptedBytes = secretKeyFactory.generateSecret(spec).getEncoded();
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public String getNewSalt() throws Exception {
        // Don't use Random!
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        // NIST recommends minimum 4 bytes. We use 8.
        byte[] salt = new byte[8];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
    public void registerUser(UUID userUUID,String inputPassword) throws Exception {
        String userSalt = getNewSalt();
        String encryptedPassword = getUserEncryptedPassword(inputPassword,userSalt);
        UserLoginInfo  userInfo = new UserLoginInfo(encryptedPassword,userSalt,userUUID);
        repoUserLoginInfo.save(userInfo);
    }

    public boolean loginUser(UUID userUUID, String inputPassword) throws Exception {
        UserLoginInfo userLoginInfo = repoUserLoginInfo.findOne(userUUID);

        String userSalt = userLoginInfo.getUserSalt();
        String hashedPassword = getUserEncryptedPassword(inputPassword,userSalt);

        String encryptedPass = userLoginInfo.getUserEncryptedPassword();
        if(hashedPassword.equals(userLoginInfo.getUserEncryptedPassword()))
            return true;

        throw new Exception("Wrong password/username");
    }

    public void updatePasswordUser(UUID userUUID, String inputPassword, String inputNewPassword) throws Exception{
        if(inputPassword.equals(inputNewPassword))
            throw new Exception("The new password is the same as the current one");

        UserLoginInfo userLoginInfo = repoUserLoginInfo.findOne(userUUID);

        String userSalt = userLoginInfo.getUserSalt();
        String hashedPassword = getUserEncryptedPassword(inputPassword,userSalt);

        if(hashedPassword.equals(userLoginInfo.getUserEncryptedPassword()))
        {
            String newSalt = getNewSalt();
            String newEncryptedPassword = getUserEncryptedPassword(inputNewPassword,newSalt);
            repoUserLoginInfo.update(new UserLoginInfo(newEncryptedPassword,newSalt,userUUID));
        }

        throw new Exception("Wrong password");
    }
}
