package com.nowcoder.community.utils;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Component
public class SensitiveFilter {
    private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);

    private static final String REPLACE_STRING = "***";

    private TrieNode root = new TrieNode();

    /**
     * 读取敏感词文本
     * 初始化Trie树
     */
    @PostConstruct
    public void buildTree() {
        try (
                InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-text.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        ) {
            String str;
            while ((str = reader.readLine()) != null) {
                TrieNode u = root;
                for (int i = 0; i < str.length(); i++) {
                    char c = str.charAt(i);
                    if (u.getSon(c) == null) {
                        u.setSon(c, new TrieNode());
                    }
                    u = u.getSon(c);
                    if (i == str.length() - 1) {
                        u.setIsEnd(true);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("加载敏感词文本失败:" + e.getMessage());
        }
    }

    public String filter(String text) {
        if (StringUtils.isBlank(text)) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0, j = 0; i < text.length(); j = ++i) {
            if (isSymbol(text.charAt(i))) {
                sb.append(text.charAt(i));
                continue;
            }

            TrieNode u = root;
            while (j < text.length()) {
                if (isSymbol(text.charAt(j))) {
                    j++;
                    continue;
                }
                if (u.getSon(text.charAt(j)) != null) {
                    u = u.getSon(text.charAt(j));
                } else {
                    sb.append(text.charAt(i));
                    break;
                }
                if (u.getIsEnd()) {
                    sb.append(REPLACE_STRING);
                    i = j;
                    break;
                }
                j++;
            }
        }
        return sb.toString();
    }

    private boolean isSymbol(Character c) {
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
    }

    private class TrieNode {
        private boolean isEnd;

        public boolean getIsEnd() {
            return isEnd;
        }

        public void setIsEnd(boolean isEnd) {
            this.isEnd = isEnd;
        }

        private Map<Character, TrieNode> son = new HashMap<>();

        public TrieNode getSon(Character c) {
            return son.get(c);
        }

        public void setSon(Character c, TrieNode node) {
            son.put(c, new TrieNode());
        }
    }
}
