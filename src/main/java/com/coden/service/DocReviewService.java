package com.coden.service;


import com.coden.entity.dto.BasePageDTO;
import com.coden.util.BaseApiResult;

import java.util.List;

public interface DocReviewService {


    /**
     * @Description 用户修改为已读状态
     * @Param [reviewId]
     * @return BaseApiResult
     **/
    BaseApiResult userRead(List<String> ids, String userId);

    /**
     * @Description 拒绝文档
     * @Param [docId, reason] 文档的id 和 拒绝的原因
     * @return BaseApiResult
     **/
    BaseApiResult refuse(String docId, String reason);

    /**
     * @Description 批量拒绝文档
     * @Param [docId] 文档列表的id
     * @return BaseApiResult
     **/
    BaseApiResult refuseBatch(List<String> docIds, String reason);

    /**
     * @Description 管理员同意一批文档
     * @Param [docIds]
     * @return BaseApiResult
     **/
    BaseApiResult approveBatch(List<String> docIds);


    /**
     * @Description 管理员或者普通用户删除评审
     * @Param []
     * @return BaseApiResult
     **/
    BaseApiResult deleteReviewsBatch(List<String> docIds, String userId);

    /**
     * @Description 查询评审的日志
     * 区分管理员和普通用户
     * @Param [page, user]
     * @return BaseApiResult
     **/
    BaseApiResult queryReviewLog(BasePageDTO page, String userId, Boolean isAdmin);
}
