package com.beibei.utils;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Component
public class GenerateRandomKeyUtil {
    @Resource
    private StringRedisTemplate template;

    public String generateAndSaveKey() {
        String key = generateRandomKey();
        template.opsForValue().set(key, key, 30, TimeUnit.SECONDS);
        return key;
    }

    public boolean checkKey(String key) {
        boolean res = false;
        if (Boolean.TRUE.equals(template.hasKey(key))) {
            res = true;
            template.delete(key);
        }
        return res;
    }

    private String generateRandomKey() {
        Random random = new Random();
        int key = 100000 + random.nextInt(900000);
        return String.valueOf(key);
    }
}
