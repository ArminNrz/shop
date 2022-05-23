package com.shop.repository;

import com.shop.entity.OtpEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class OtpRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private final HashOperations<String, String, OtpEntity> hashOps;

    private static final String REDIS_OTP_LOGIN_HASH_KEY = "otpLoginHash";
    private static final String REDIS_OTP_FORGET_PASSWORD_HASH_KEY = "otpForgetPasswordHash";

    public void addLoginOtp(OtpEntity entity) {
        hashOps.put(REDIS_OTP_LOGIN_HASH_KEY, entity.getPhoneNumber(), entity);
        redisTemplate.expire(REDIS_OTP_LOGIN_HASH_KEY, 1, TimeUnit.MINUTES);
    }

    public OtpEntity findByPhoneNumberOtpLogin(String phoneNumber) {
        return hashOps.get(REDIS_OTP_LOGIN_HASH_KEY, phoneNumber);
    }

    public void addForgetPasswordOtp(OtpEntity entity) {
        hashOps.put(REDIS_OTP_FORGET_PASSWORD_HASH_KEY, entity.getPhoneNumber(), entity);
        redisTemplate.expire(REDIS_OTP_FORGET_PASSWORD_HASH_KEY, 5, TimeUnit.MINUTES);
    }

    public OtpEntity findByPhoneNumberOtpForgetPassword(String phoneNumber) {
        return hashOps.get(REDIS_OTP_FORGET_PASSWORD_HASH_KEY, phoneNumber);
    }
}
