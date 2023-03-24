package com.coden.service;

import com.coden.entity.Thumbnail;

public interface ThumbnailService {

    /**
     * 保存缩略图信息
     * @param thumbnail 缩略图
     */
    void save(Thumbnail thumbnail);

    /**
     * 通过对象的id进行查询
     * @param objectId 对象id
     * @return Thumbnail 缩略图
     */
    Thumbnail searchByObjectId(String objectId);

    /**
     * 通过对象的id进行删除
     * @param objectId 对象id
     */
    void removeByObjectId(String objectId);

}
