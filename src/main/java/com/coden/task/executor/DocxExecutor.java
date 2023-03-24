package com.coden.task.executor;

import com.coden.task.data.TaskData;
import com.coden.util.MsExcelParse;

import java.io.IOException;
import java.io.InputStream;


public class DocxExecutor extends TaskExecutor{

    @Override
    protected void readText(InputStream is, String textFilePath) throws IOException {
        MsExcelParse.readPdfText(is, textFilePath);
    }

    @Override
    protected void makeThumb(InputStream is, String picPath) throws IOException {
        // TODO document why this method is empty

    }

    @Override
    protected void makePreviewFile(InputStream is, TaskData taskData) {

    }
}
