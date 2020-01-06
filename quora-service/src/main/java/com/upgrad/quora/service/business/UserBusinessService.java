package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.common.GenericErrorCode;
import com.upgrad.quora.service.common.UnexpectedException;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import com.upgrad.quora.service.util.QuoraUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.time.ZonedDateTime;
import java.util.Base64;

@Service
public class UserBusinessService {

    @Autowired
    private UserDao userDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity signup(UserEntity userEntity) throws SignUpRestrictedException {
        if (userDao.getUserByUserName(userEntity.getUserName()) != null) {
            throw new SignUpRestrictedException("SGR-001", "Try any other Username, this Username has already been taken");
        }
        if (userDao.getUserByEmail(userEntity.getEmail()) != null) {
            throw new SignUpRestrictedException("SGR-002", "This user has already been registered, try with any other emailId");
        }
        String password = userEntity.getPassword();
        if (password != null) {
            String[] encryptedText = PasswordCryptographyProvider.encrypt(password);
            userEntity.setSalt(encryptedText[0]);
            userEntity.setPassword(encryptedText[1]);
            return userDao.createUser(userEntity);
        }
        return userDao.createUser(userEntity);

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public UserAuthEntity signIn(String authorization) throws AuthenticationFailedException {
        //this will be used to decode the request header authorization

        try {
            byte[] decode = Base64.getDecoder().decode(authorization.split(QuoraUtil.BASIC_TOKEN)[0]);
            String decodedText = new String(decode);
            String[] decodedArray = decodedText.split(QuoraUtil.COLON);
            String username = decodedArray[0];
            String password = decodedArray[1];
            UserEntity user = userDao.getUserByUserName(username);
            if (user == null) {
                throw new AuthenticationFailedException("ATH-001", "This username does not exist");
            }

            final String encryptedPassword = PasswordCryptographyProvider.encrypt(password, user.getSalt());
            if (encryptedPassword.equals(user.getPassword())) {

                JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(encryptedPassword);
                UserAuthEntity userAuthTokenEntity = new UserAuthEntity();
                userAuthTokenEntity.setUser(user);
                final ZonedDateTime now = ZonedDateTime.now();
                final ZonedDateTime expiresAt = now.plusHours(8);
                userAuthTokenEntity.setAccessToken(jwtTokenProvider.generateToken(user.getUuid(), now, expiresAt));
                userAuthTokenEntity.setLoginAt(now);
                userAuthTokenEntity.setExpiresAt(expiresAt);
                userAuthTokenEntity.setUuid(user.getUuid());
                return userDao.createAuthToken(userAuthTokenEntity);

            } else {
                throw new AuthenticationFailedException("ATH-002", "Password failed");
            }

        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException ex) {
            GenericErrorCode genericErrorCode = GenericErrorCode.GEN_001;
            throw new UnexpectedException(genericErrorCode, ex);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public String getUserUUID(String authorization) throws SignOutRestrictedException {
        String[] bearerToken = authorization.split(QuoraUtil.BEARER_TOKEN);
        // If Bearer Token prefix is missed, ignore and just use the authorization text
        if (bearerToken != null && bearerToken.length > 1) {
            authorization = bearerToken[1];
        }
        UserAuthEntity userAuthEntity = userDao.getUserAuthToken(authorization);
        if (isUserSessionValid(userAuthEntity)) {
            userAuthEntity.setLogoutAt(ZonedDateTime.now());
            userDao.updateUserAuthEntity(userAuthEntity);
            return userAuthEntity.getUuid();
        }
        throw new SignOutRestrictedException("SGR-001", "User is not Signed in");
    }

    public Boolean isUserSessionValid(UserAuthEntity userAuthEntity) {
        // userAuthEntity will be non null only if token exists in DB, and logoutAt null indicates user has not logged out yet
        return (userAuthEntity != null && userAuthEntity.getLogoutAt() == null);
    }

    public UserAuthEntity validateUserAuthentication(String authorization, String athr002Message)
            throws AuthorizationFailedException {
        String[] bearerToken = authorization.split("Bearer");
        // If Bearer Token prefix is missed, ignore and just use the authorization text
        if (bearerToken != null && bearerToken.length > 1) {
            authorization = bearerToken[1];
        }
        UserAuthEntity userAuthEntity = userDao.getUserAuthToken(authorization);
        // Token is not matched with the database records
        if (userAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }
        // Token matches, but the user has already logged out
        if (userAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", athr002Message);
        }
        return userAuthEntity;
    }
}
