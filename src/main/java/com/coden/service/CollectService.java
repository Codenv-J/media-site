package com.coden.service;

import com.coden.entity.CollectDocRelationship;
import com.coden.util.BaseApiResult;

public interface CollectService {

    /**
     * 新增文档收藏
     * @param collect -> Collect Doc Relationship
     * @return -> ApiResult
     */
    BaseApiResult insert(CollectDocRelationship collect);

    /**
     * 移除文档收藏
     * @param collect -> CollectDocRelationship
     * @return -> ApiResult
     */
    BaseApiResult remove(CollectDocRelationship collect);
}
