package com.zgz.base.redis.impl;

import com.zgz.base.redis.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class RedisCacheImpl implements RedisCache {

    private RedisTemplate<String, String> redisTemplate;

    @Override
    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void setExpire(String key, Integer time, TimeUnit timeUnit) {
        redisTemplate.expire(key, time, timeUnit);
    }

    @Override
    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void setWithExpire(String key, Integer second, String value) {
        redisTemplate.opsForValue().set(key, value, second, TimeUnit.SECONDS);
    }

    @Override
    public void setRange(String key, Integer start, String value) {
        redisTemplate.opsForValue().set(key, value, start);
    }
    
    public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

}
