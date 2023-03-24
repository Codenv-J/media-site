package com.coden.service;

import com.coden.entity.FileDocument;
import com.coden.entity.User;
import com.coden.entity.dto.BasePageDTO;
import com.coden.service.impl.DocLogServiceImpl;
import com.coden.util.BaseApiResult;

import java.util.List;

/**
 * @ClassName IDocLogService
 * @Description 文档日志的信息
 * @Author luojiarui
 * @Date 2022/12/10 11:04
 * @Version 1.0
 **/
public interface IDocLogService {

    /**
     * add logs
     * @Description add logs
     * @Param [user, document, action]
     **/
    void addLog(User user, FileDocument document, DocLogServiceImpl.Action action);

    /**
     * query Doc Logs
     * @Description 分页查询文档日志
     * @Param page BasePageDTO
     * @Param user String
     * @return BaseApiResult
     **/
    BaseApiResult queryDocLogs(BasePageDTO page, String userId);

    /**
     * delete doc logs in batches
     * @Description 批量删除文档的日志
     * @Param ids 文档的id列表
     * @Param userId user index
     * @return BaseApiResult
     **/
    BaseApiResult deleteDocLogBatch(List<String> logIds, String userId);

}
