package com.newcoder.community.utils;

public interface CommunityConstant {
    /**
     * 激活成功
     */
    int ACTIVATION_SUCCESS = 0;

    /**
     * 重复激活
     */
    int ACTIVATION_REPEAT = 1;

    /**
     * 激活失败
     */
    int ACTIVATION_FAILURE = 2;

    /**
     * ticket默认时间
     */
    long DEFAULT_EXPIRED_SECONDS = 3600L * 12;

    /**
     * ticket加长时间
     */
    long REMEMBER_EXPIRED_SECONDS = 3600L * 24 * 100;

    /**
     * 实体类型1：评论
     */
    int ENTITY_TYPE_POST = 1;

    /**
     * 实体类型2：回复
     */
    int ENTITY_TYPE_COMMENT = 2;

    /**
     * 实体类型3：用户
     */
    int ENTITY_TYPE_USER = 3;
}
