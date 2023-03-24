package com.coden.task.data;

import com.coden.entity.FileDocument;
import com.coden.enums.DocType;
import lombok.Data;

@Data
public class TaskData {

    FileDocument fileDocument;

    String txtFilePath;

    String thumbFilePath;

    String previewFilePath;

    DocType docType;

}
