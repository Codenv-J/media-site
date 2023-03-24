package com.coden.service;

import com.coden.entity.FileDocument;

public interface TaskExecuteService {

    /**
     * 任务执行的入口
     * @param fileDocument 文档信息的实体
     */
    void execute(FileDocument fileDocument);

}
