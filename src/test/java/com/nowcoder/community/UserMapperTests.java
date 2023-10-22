package com.nowcoder.community;

import com.nowcoder.community.entity.User;
import com.nowcoder.community.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
public class UserMapperTests {
    @Autowired
    private UserMapper userMapper;

    @Test
    void testSelect() {
        User user = userMapper.selectById(1);
        System.out.println(user);

        User system = userMapper.selectByName("SYSTEM");
        System.out.println(system);

        User user1 = userMapper.selectByEmail("nowcoder1@sina.com");
        System.out.println(user1);
    }

    @Test
    void testInsert() {
        User user = new User();
        user.setUsername("吕德华");
        user.setPassword("123456");
        user.setSalt("dwadd");
        user.setEmail("123123123@qq.com");
        user.setType(0);
        user.setStatus(1);
        user.setHeaderUrl("http://static.nowcoder.com/images/head/notify.png");
        user.setCreateTime(new Date());
        int i = userMapper.insertUser(user);
        System.out.println(i);
        System.out.println(user.getId());
    }

    @Test
    void testUpdate() {
        userMapper.updateStatus(150, 999);
        userMapper.updateHeader(150, "999");
        userMapper.updatePassword(150, "999");
    }

    @Test
    void name() {
        Logger logger = LoggerFactory.getLogger(UserMapperTests.class);
        logger.debug("debug !!!");
        logger.info("info !!!");
        logger.warn("warn !!!");
        logger.error("error !!!");
    }
}
