package com.coden.entity;

import com.coden.enums.ThumbSizeEnum;
import com.coden.enums.ThumbnailEnum;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 缩略图相关的类
 **/
@Document
@Data
public class Thumbnail {

    /**
     * 缩略图id
     */
    @Id
    private String id;

    /**
     * 对象的id
     */
    private String objectId;

    /**
     * 不同种类型
     */
    private ThumbnailEnum thumbnailEnum;

    /**
     * 大文件管理GridFS的ID
     */
    private String gridfsId;

    /**
     * 缩略图的尺寸大小
     **/
    private ThumbSizeEnum thumbSizeEnum;


}
