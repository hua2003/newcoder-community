package com.newcoder.community;

import com.newcoder.community.service.SimpleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TransactionalTest {
    @Autowired
    private SimpleService service;

    @Test
    void testSave() {
        service.save();
    }

    @Test
    void testSave2() {
        service.save2();
    }
}
