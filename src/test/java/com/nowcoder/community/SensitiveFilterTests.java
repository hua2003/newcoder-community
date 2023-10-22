package com.nowcoder.community;

import com.nowcoder.community.utils.SensitiveFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SensitiveFilterTests {
    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Test
    void testFilter() {
        String s = "yytyeuureyrueyureuryfabc";
        String filter = sensitiveFilter.filter(s);
        System.out.println(filter);
    }
}
