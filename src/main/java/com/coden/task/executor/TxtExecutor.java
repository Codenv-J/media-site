package com.coden.task.executor;

import com.coden.entity.FileDocument;
import com.coden.entity.FileObj;
import com.coden.task.data.TaskData;
import com.coden.task.exception.TaskRunException;

import java.io.IOException;
import java.io.InputStream;

/**
 * TODO
 **/
public class TxtExecutor extends TaskExecutor{

    @Override
    protected void readText(InputStream is, String textFilePath) throws IOException {

    }

    @Override
    protected void makeThumb(InputStream is, String picPath) throws IOException {

    }

    @Override
    protected void makePreviewFile(InputStream is, TaskData taskData) {

    }

    @Override
    public void uploadFileToEs(InputStream is, FileDocument fileDocument, TaskData taskData) {
        try {
            FileObj fileObj = new FileObj();
            fileObj.setId(fileDocument.getMd5());
            fileObj.setName(fileDocument.getName());
            fileObj.setType(fileDocument.getContentType());
            fileObj.readFile(is);
            this.upload(fileObj);
        } catch (IOException | TaskRunException e) {
            throw new TaskRunException("存入es的过程中报错了", e);
        }
    }
}
