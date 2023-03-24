package com.coden.task.executor;

import com.coden.task.data.TaskData;
import com.coden.util.PdfUtil;

import java.io.IOException;
import java.io.InputStream;


public class PdfWordTaskExecutor extends TaskExecutor {


    @Override
    protected void readText(InputStream is, String textFilePath) throws IOException {
        PdfUtil.readPdfText(is, textFilePath);
    }

    @Override
    protected void makeThumb(InputStream is, String picPath) {
        PdfUtil.pdfThumbnail(is, picPath);
    }

    @Override
    protected void makePreviewFile(InputStream is, TaskData taskData) {

    }
}
