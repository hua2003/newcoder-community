package com.newcoder.community.service.impl;

import com.newcoder.community.service.LikeService;
import com.newcoder.community.utils.RedisKeyUtil;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class LikeServiceImpl implements LikeService {
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void like(int userId, int entityType, int entityId) {
        String key = RedisKeyUtil.getLikeKey(entityType, entityId);
        Boolean ok = redisTemplate.opsForSet().isMember(key, userId);
        if (BooleanUtils.isTrue(ok)) {
            redisTemplate.opsForSet().remove(key, userId);
        } else {
            redisTemplate.opsForSet().add(key, userId);
        }
    }

    @Override
    public long findLikeCount(int entityType, int entityId) {
        String key = RedisKeyUtil.getLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().size(key);
    }

    @Override
    public int findLikeStatus(int userId, int entityType, int entityId) {
        String key = RedisKeyUtil.getLikeKey(entityType, entityId);
        Boolean ok = redisTemplate.opsForSet().isMember(key, userId);
        return BooleanUtils.isTrue(ok) ? 1 : 0;
    }
}
