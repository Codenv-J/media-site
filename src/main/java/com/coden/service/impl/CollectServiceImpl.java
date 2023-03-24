package com.coden.service.impl;

import com.coden.common.MessageConstant;
import com.coden.entity.CollectDocRelationship;
import com.coden.service.CollectService;
import com.coden.util.BaseApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Slf4j
@Service
public class CollectServiceImpl implements CollectService {

    private static final String COLLECTION_NAME = "collectCollection";

    private static final String DOC_ID = "docId";

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    UserServiceImpl userServiceImpl;

    @Autowired
    FileServiceImpl fileServiceImpl;

    /**
     * @return ApiResult
     * @Description 对某个文档进行关注
     * @Param [collect]
     **/
    @Override
    public BaseApiResult insert(CollectDocRelationship collect) {
        // 必须经过userId和docId的校验，否则不予关注
        if (!userServiceImpl.isExist(collect.getUserId()) || !fileServiceImpl.isExist(collect.getDocId())) {
            return BaseApiResult.error(MessageConstant.PROCESS_ERROR_CODE, MessageConstant.OPERATE_FAILED);
        }

        CollectDocRelationship collectDb = getExistRelationship(collect);
        if (collectDb != null) {
            return BaseApiResult.error(MessageConstant.PROCESS_ERROR_CODE, MessageConstant.OPERATE_FAILED);
        }
        mongoTemplate.save(collect, COLLECTION_NAME);
        return BaseApiResult.success(MessageConstant.SUCCESS);
    }

    /**
     * @return ApiResult
     * @Description 删除收藏关系
     * @Param [collect]
     **/
    @Override
    public BaseApiResult remove(CollectDocRelationship collect) {
        collect = getExistRelationship(collect);
        while (collect != null) {
            mongoTemplate.remove(collect, COLLECTION_NAME);
            collect = getExistRelationship(collect);
        }
        return BaseApiResult.success(MessageConstant.SUCCESS);
    }

    /**
     * @return CollectDocRelationship
     * @Description 查询已经存在的关系
     * @Param []
     **/
    private CollectDocRelationship getExistRelationship(CollectDocRelationship collect) {
        collect = Optional.ofNullable(collect).orElse(new CollectDocRelationship());

        Query query = new Query()
                .addCriteria(Criteria.where(DOC_ID).is(collect.getDocId())
                        .and("userId").is(collect.getUserId()));

        return mongoTemplate.findOne(
                query, CollectDocRelationship.class, COLLECTION_NAME
        );
    }

    /**
     * @return java.lang.Long
     * @Description 查询某个文档下面的点赞数量
     * @Param [docId]
     **/
    public Long collectNum(String docId) {
        Query query = new Query().addCriteria(Criteria.where(DOC_ID).is(docId));
        return mongoTemplate.count(query, CollectDocRelationship.class, COLLECTION_NAME);
    }

    /**
     * @Description 根据文档的id删除掉点赞的关系
     * @Param [docId]
     **/
    public void removeRelateByDocId(String docId) {
        Query query = new Query(Criteria.where(DOC_ID).is(docId));
        List<CollectDocRelationship> relationships = mongoTemplate.find(query, CollectDocRelationship.class,
                COLLECTION_NAME);
        relationships.forEach(this::remove);
    }


}
