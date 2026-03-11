package com.dekk.auth.presentation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisTestController {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @GetMapping("/redis-test")
    public String test() {
        redisTemplate.opsForValue().set("mykey", "success");
        return redisTemplate.opsForValue().get("mykey");
    }
}
