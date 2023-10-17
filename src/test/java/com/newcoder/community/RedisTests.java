package com.newcoder.community;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;

@SpringBootTest
public class RedisTests {
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    void testSetAndGet() {
        redisTemplate.delete("id");
        redisTemplate.opsForValue().set("id", 1);
        System.out.println(redisTemplate.opsForValue().get("id"));
        redisTemplate.opsForValue().increment("id");
        redisTemplate.opsForValue().increment("id");
        redisTemplate.opsForValue().increment("id");
        redisTemplate.opsForValue().decrement("id");
        System.out.println(redisTemplate.opsForValue().get("id"));
    }

    @Test
    void testTransactional() {
        Object obj = redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String key = "test:tran";
                // 开启事务
                operations.multi();
                operations.opsForValue().set(key, 1);
                // 提交事务
                return operations.exec();
            }
        });
        System.out.println(obj);
    }
}
