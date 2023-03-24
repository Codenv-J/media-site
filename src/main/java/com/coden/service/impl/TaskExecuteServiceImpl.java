package com.coden.service.impl;

import com.coden.entity.FileDocument;
import com.coden.service.TaskExecuteService;
import com.coden.task.thread.MainTask;
import com.coden.task.thread.TaskThreadPool;
import org.springframework.stereotype.Service;


@Service
public class TaskExecuteServiceImpl implements TaskExecuteService {

    @Override
    public void execute(FileDocument fileDocument) {
        MainTask mainTask = new MainTask(fileDocument);
        TaskThreadPool.getInstance().submit(mainTask);
    }
}
