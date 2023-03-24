package com.coden.service.impl;

import com.coden.entity.Thumbnail;
import com.coden.service.ThumbnailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


@Service
public class ThumbnailServiceImpl implements ThumbnailService {

    private static final String THUMB_COLLECTION_NAME = "thumbCollection";

    @Autowired
    MongoTemplate mongoTemplate;


    @Override
    public void save(Thumbnail thumbnail) {
        String objectId = thumbnail.getObjectId();
        if( !StringUtils.hasText(objectId) || !StringUtils.hasText(thumbnail.getGridfsId())) {
            return;
        }
        if( searchByObjectId(objectId) != null) {
            this.removeByObjectId(objectId);
        }
        mongoTemplate.save(thumbnail, THUMB_COLLECTION_NAME);
    }

    @Override
    public Thumbnail searchByObjectId(String objectId) {
        if( !StringUtils.hasText(objectId)) {
            return null;
        }
        return mongoTemplate.findById(objectId, Thumbnail.class, THUMB_COLLECTION_NAME);
    }

    @Override
    public void removeByObjectId(String objectId) {
        // 删除掉相关的分类关系
        Query query1 = new Query().addCriteria(Criteria.where("objectId").is(objectId));
        mongoTemplate.remove(query1, Thumbnail.class, THUMB_COLLECTION_NAME);
    }
}
