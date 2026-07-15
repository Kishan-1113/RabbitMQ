package com.example.rabbit_config.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.rabbit_config.Exceptions.ResourceNotFoundException;
import com.example.rabbit_config.Model.DB_Tables.UserToken;
import com.example.rabbit_config.Repository.UserTokenRepo;

@Service
public class UserTokenService {

    private Logger log = LoggerFactory.getLogger(UserTokenService.class);

    private UserTokenRepo userTokenRepo;

    UserTokenService(UserTokenRepo userTokenRepo) {
        this.userTokenRepo = userTokenRepo;
    }

    public String getLastHistoryId(String email) {
        UserToken user = userTokenRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("No such user with the email Id"));

        return user.getLastHistoryId();
    }

    public void saveHistoryId(String email, String historyId) {
        try {
            UserToken user = userTokenRepo.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("No such user with the email Id"));
            user.setLastHistoryId(historyId);
            userTokenRepo.save(user);
        } catch (ResourceNotFoundException e) {
            log.error("\nError occured with Database UserToken table\n");
            e.printStackTrace();
        }
    }

    public void saveAccessRefreshToken(String email, String accessToken, String refreshToken) {
        try {
            UserToken user = userTokenRepo.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("No such user with the email Id"));
            user.setAccessToken(accessToken);
            user.setRefreshToken(refreshToken);

            userTokenRepo.save(user);
        } catch (ResourceNotFoundException e) {
            log.error("\nError occured with Database UserToken table\n");
            e.printStackTrace();
        }
    }

    public void saveAccessRefreshHistoryToken(String email, String accessToken, String refreshToken,
            String lastHistoryId) {
        try {
            UserToken user = userTokenRepo.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("No such user with the email Id"));
            user.setAccessToken(accessToken);
            user.setRefreshToken(refreshToken);
            user.setLastHistoryId(lastHistoryId);

            userTokenRepo.save(user);
        } catch (ResourceNotFoundException e) {
            log.error("\nError occured with Database UserToken table\n");
            e.printStackTrace();
        }
    }

    public void saveUserTokenData(
            String email, String accessToken,
            String refreshToken,
            String lastHistoryId, long expirationMillis) {
        try {
            UserToken user = userTokenRepo.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("No such user with the email Id"));
            user.setAccessToken(accessToken);
            user.setRefreshToken(refreshToken);
            user.setLastHistoryId(lastHistoryId);
            user.setExpirationTimeMillis(expirationMillis);

            userTokenRepo.save(user);
        } catch (ResourceNotFoundException e) {
            log.error("\nError occured with Database UserToken table\n");
            e.printStackTrace();
        }
    }

}
