package com.coden.controller;

import com.google.common.collect.Maps;
import com.coden.common.MessageConstant;
import com.coden.entity.FileDocument;
import com.coden.entity.Tag;
import com.coden.entity.TagDocRelationship;
import com.coden.entity.dto.SearchKeyDTO;
import com.coden.entity.vo.DocumentVO;
import com.coden.service.IFileService;
import com.coden.service.RedisService;
import com.coden.service.StatisticsService;
import com.coden.service.TagService;
import com.coden.service.impl.FileServiceImpl;
import com.coden.service.impl.RedisServiceImpl;
import com.coden.util.BaseApiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Api(tags = "热点与搜索记录模块")
@RestController
@Slf4j
@CrossOrigin
@RequestMapping("/statistics")
public class StatisticsController {

    @Autowired
    StatisticsService statisticsService;

    @Autowired
    RedisService redisService;

    @Autowired
    IFileService fileService;

    @Autowired
    FileServiceImpl fileServiceImpl;

    @Autowired
    TagService tagService;

    /*
    @ApiOperation(value = "4.1 查询热度榜", notes = "查询列表")
    @GetMapping(value = "/trend")
    public BaseApiResult trend() {
        System.out.println("trend");
        return statisticsService.trend();
    }
    */

    @ApiOperation(value = "查询统计数据", notes = "查询列表")
    @GetMapping(value = "/all")
    public BaseApiResult all() {
        return statisticsService.all();
    }

    /**
     * @return ApiResult
     * @Description 查询推荐的搜索记录
     * @Param []
     **/
    @ApiOperation(value = "查询推荐的搜索记录", notes = "查询列表")
    @GetMapping("getSearchResult")
    public BaseApiResult getSearchResult(@RequestHeader HttpHeaders headers) {
        List<String> userSearchList = Lists.newArrayList();
        List<String> stringList = headers.get("id");
        if (!CollectionUtils.isEmpty(stringList)) {
            String userId = stringList.get(0);
            if (StringUtils.hasText(userId)) {
                userSearchList = redisService.getSearchHistoryByUserId(userId);
            }
        }
        List<String> hotSearchList = redisService.getHotList(null, RedisServiceImpl.SEARCH_KEY);
        Map<String, List<String>> result = Maps.newHashMap();
        result.put("userSearch", userSearchList);
        result.put("hotSearch", hotSearchList);
        return BaseApiResult.success(result);
    }

    @PutMapping(value = "removeKey")
    public BaseApiResult removeKey(@RequestBody SearchKeyDTO searchKeyDTO) {
        redisService.delSearchHistoryByUserId(searchKeyDTO.getUserId(), searchKeyDTO.getSearchWord());
        return BaseApiResult.success();
    }

    @ApiOperation(value = "查询热度榜", notes = "查询列表")
    @GetMapping("getHotTrend")
    public BaseApiResult getHotTrend() {
        List<String> docIdList = redisService.getHotList(null, RedisServiceImpl.DOC_KEY);

        if (CollectionUtils.isEmpty(docIdList)) {
            return BaseApiResult.error(MessageConstant.PROCESS_ERROR_CODE, MessageConstant.OPERATE_FAILED);
        }


        List<FileDocument> fileDocumentList = Lists.newArrayList();
        for (String s : docIdList) {
            FileDocument fileDocument = fileService.queryById(s);
            if (fileDocument != null) {
                fileDocumentList.add(fileDocument);
            } else {
                redisService.delKey(s, RedisServiceImpl.DOC_KEY);
            }
        }
        // 从redis中删除无效id
        if (CollectionUtils.isEmpty(fileDocumentList)) {
            return BaseApiResult.error(MessageConstant.PROCESS_ERROR_CODE, MessageConstant.OPERATE_FAILED);
        }
        FileDocument topFileDocument = fileDocumentList.remove(0);
        DocumentVO documentVO = fileServiceImpl.convertDocument(null, topFileDocument);
        Map<String, Object> top1 = Maps.newHashMap();
        top1.put("name", topFileDocument.getName());
        top1.put("id", topFileDocument.getId());
        top1.put("commentNum", documentVO.getCommentNum());
        top1.put("collectNum", documentVO.getCollectNum());
        top1.put("likeNum", (int) Math.round(redisService.score(RedisServiceImpl.DOC_KEY, topFileDocument.getId())));
        List<Object> others = new ArrayList<>();
        int count = 10;
        for (FileDocument fileDocument : fileDocumentList) {
            Map<String, Object> otherInfo = Maps.newHashMap();
            otherInfo.put("hit", count);
            otherInfo.put("name", fileDocument.getName());
            otherInfo.put("id", fileDocument.getId());
            count--;
            others.add(otherInfo);
        }
        Map<String, Object> result = Maps.newHashMap();
        result.put("top1", top1);
        result.put("others", others);
        return BaseApiResult.success(result);
    }


    @ApiOperation(value = "获取用户近期数据", notes = "展示1、最近新提交并审核通过的12篇文章；2、获取最近新连接关系的文档")
    @GetMapping("/recentDocs")
    public BaseApiResult getRecentDocs() {
        List<Map<String, Object>> result = Lists.newArrayList();

        List<FileDocument> recentFileDocuments = fileService.listFilesByPage(0, 12);
        List<Map<String, Object>> recentMap = doc2Map(recentFileDocuments);
        result.add(getTagMap("最近的文档", "tagId", recentMap));

        Map<Tag, List<TagDocRelationship>> tagDocMap = tagService.getRecentTagRelationship();

        for (Map.Entry<Tag, List<TagDocRelationship>> tagListEntry : tagDocMap.entrySet()) {
            Tag tag = tagListEntry.getKey();
            List<String> docIdList = tagListEntry.getValue()
                    .stream().map(TagDocRelationship::getFileId).collect(Collectors.toList());
            List<FileDocument> tagFileDocument = fileService.listAndFilterByPage(0, 12, docIdList);
            List<Map<String, Object>> map = doc2Map(tagFileDocument);
            result.add(getTagMap(tag.getName(), tag.getId(), map));
        }

        return BaseApiResult.success(result);
    }

    /**
     * @return List<Map < String, Object>>
     * @Description 文档列表转向为map
     * @Param [fileDocuments]
     **/
    private List<Map<String, Object>> doc2Map(List<FileDocument> fileDocuments) {
        List<Map<String, Object>> result = new ArrayList<>();
        if (CollectionUtils.isEmpty(fileDocuments)) {
            return result;
        }

        for (FileDocument fileDocument : fileDocuments) {
            Map<String, Object> map = Maps.newHashMap();
            map.put("name", fileDocument.getName());
            map.put("id", fileDocument.getId());
            map.put("thumbId", fileDocument.getThumbId());
            result.add(map);
        }
        return result;
    }

    /**
     * @return Map<String, Object>
     * @Description 生成返回的数据
     * @Param [name, tagId, docList]
     **/
    private Map<String, Object> getTagMap(String name, String tagId, Object docList) {
        Map<String, Object> tagMap = Maps.newHashMap();
        if (name == null || tagId == null || docList == null) {
            return tagMap;
        }
        tagMap.put("name", name);
        tagMap.put("tagId", tagId);
        tagMap.put("docList", docList);
        return tagMap;
    }
}
