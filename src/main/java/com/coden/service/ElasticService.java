package com.coden.service;

import com.coden.entity.FileDocument;

import java.io.IOException;
import java.util.List;

public interface ElasticService {

    /**
     * search
     * @param keyword String
     * @return result
     * @throws IOException
     */
    List<FileDocument> search(String keyword) throws IOException;

}
