package com.coden.task.executor;

import com.coden.enums.DocType;

import java.util.EnumMap;
import java.util.Map;

public class TaskExecutorFactory {

    private TaskExecutorFactory() {

    }

    static Map<DocType, TaskExecutor> taskExecutorMap = new EnumMap<>(DocType.class);

    public static TaskExecutor getTaskExecutor(DocType docType) {
        TaskExecutor taskExecutor = taskExecutorMap.get(docType);
        if (null != taskExecutor) {
            return taskExecutor;
        }
        return createTaskExecutor(docType);
    }

    /**
     * 创建任务执行器
     * @param docType 文档类型
     * @return 任务执行器
     */
    private static TaskExecutor createTaskExecutor(DocType docType) {
        TaskExecutor taskExecutor = null;
        switch (docType) {
            case PDF:
            case pdf:
                taskExecutor = new PdfWordTaskExecutor();
                break;
            case DOCX:
            case DOC:
                taskExecutor = new DocxExecutor();
                break;
            default:
                break;
        }
        if (null != taskExecutor) {
            taskExecutorMap.put(docType, taskExecutor);
        }
        return taskExecutor;
    }
}
