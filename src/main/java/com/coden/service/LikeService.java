package com.coden.service;


public interface LikeService {

    /**
     * 增加点赞，取消点赞；增加收藏，取消收藏
     * @param userId 用户id
     * @param entityType 实体类型：1：点赞；2：收藏
     * @param entityId 实体的id
     */
    void like(String userId, Integer entityType, String entityId);

    /**
     * 获取点赞的数量
     * @param entityType 实体类型
     * @param entityId 实体id
     * @return 返回点赞的数量
     */
    Long findEntityLikeCount(Integer entityType, String entityId);

    /**
     * 获取当前用户点赞的状态
     * @param userId 用户id
     * @param entityType 实体类型：1：点赞；2：收藏
     * @param entityId 实体的id
     * @return 返回该用户点赞的状态
     */
    int findEntityLikeStatus(String userId, Integer entityType, String entityId);


}
