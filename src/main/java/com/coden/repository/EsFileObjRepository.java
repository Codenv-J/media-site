package com.coden.repository;

import com.coden.entity.FileObj;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * // 另外一种方式
 * //    @Query("{\"match\":{\"attachment.content\":\"?0\"}}")
 * //    SearchHits<FileObj> find(String keyword);
 */
public interface EsFileObjRepository extends ElasticsearchRepository<FileObj, String> {


}
