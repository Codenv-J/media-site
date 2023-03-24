package com.coden.controller;

import com.coden.common.MessageConstant;
import com.coden.entity.FileDocument;
import com.coden.entity.User;
import com.coden.entity.dto.DocumentDTO;
import com.coden.entity.dto.RemoveObjectDTO;
import com.coden.enums.FilterTypeEnum;
import com.coden.intercepter.SensitiveFilter;
import com.coden.service.IDocLogService;
import com.coden.service.IFileService;
import com.coden.service.RedisService;
import com.coden.service.impl.DocLogServiceImpl;
import com.coden.service.impl.RedisServiceImpl;
import com.coden.util.BaseApiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * @ClassName DocumentController
 * @Version 1.0
 **/

@Api(tags = "文档查询删除模块")
@RestController
@Slf4j
@CrossOrigin
@RequestMapping("/document")
public class DocumentController {

    @Resource
    IFileService iFileService;

    @Resource
    RedisService redisService;

    @Resource
    IDocLogService docLogService;


    @ApiOperation(value = "查询文档的分页列表页", notes = "根据参数查询文档列表")
    @PostMapping(value = "/list")
    public BaseApiResult list(@RequestBody DocumentDTO documentDTO)
            throws IOException {
        String userId = documentDTO.getUserId();
        if (StringUtils.hasText(documentDTO.getFilterWord()) &&
                documentDTO.getType() == FilterTypeEnum.FILTER) {
            String filterWord = documentDTO.getFilterWord();
            //非法敏感词汇判断
            SensitiveFilter filter = SensitiveFilter.getInstance();
            int n = filter.checkSensitiveWord(filterWord, 0, 1);
            //存在非法字符
            if (n > 0) {
                log.error("这个人输入了非法字符--> {},不知道他到底要查什么~", filterWord);
            } else {
                redisService.incrementScoreByUserId(filterWord, RedisServiceImpl.SEARCH_KEY);
                if (StringUtils.hasText(userId)) {
                    redisService.addSearchHistoryByUserId(userId, filterWord);
                }
            }
        }
        return iFileService.list(documentDTO);
    }

    @ApiOperation(value = "查询文档的详细信息", notes = "查询文档的详细信息")
    @GetMapping(value = "/detail")
    public BaseApiResult detail(@RequestParam(value = "docId") String id) {
        return iFileService.detail(id);
    }

    @ApiOperation(value = "删除某个文档", notes = "删除某个文档")
    @DeleteMapping(value = "/auth/remove")
    public BaseApiResult remove(@RequestBody RemoveObjectDTO removeObjectDTO, HttpServletRequest request) {
        FileDocument fileDocument = iFileService.queryById(removeObjectDTO.getId());
        if (fileDocument == null){
            return BaseApiResult.error(MessageConstant.PARAMS_ERROR_CODE, MessageConstant.PARAMS_FORMAT_ERROR);
        }
        String username = (String) request.getAttribute("username");
        String userId = (String) request.getAttribute("id");
        User user = new User();
        user.setUsername(username);
        user.setId(userId);
        docLogService.addLog(user, fileDocument, DocLogServiceImpl.Action.DELETE);
        return iFileService.remove(removeObjectDTO.getId());
    }


    @ApiOperation(value = "指定分类时，查询文档的分页列表页", notes = "根据参数查询文档列表")
    @GetMapping(value = "/listWithCategory")
    public BaseApiResult listWithCategory(@ModelAttribute("documentDTO") DocumentDTO documentDTO) {
        FilterTypeEnum filterType = documentDTO.getType();
        if (filterType.equals(FilterTypeEnum.CATEGORY) || filterType.equals(FilterTypeEnum.TAG) ) {
            return iFileService.listWithCategory(documentDTO);
        } else {
            return BaseApiResult.error(MessageConstant.PARAMS_ERROR_CODE, MessageConstant.PARAMS_FORMAT_ERROR);
        }
    }

    @GetMapping("/addKey")
    public BaseApiResult addKey(@RequestParam("key") String key) {

        final int userId = redisService.addSearchHistoryByUserId("userId", key);
        log.info(String.valueOf(userId));
        redisService.incrementScoreByUserId(key, RedisServiceImpl.SEARCH_KEY);
        return BaseApiResult.success(key);
    }

    @GetMapping("/keyList")
    public BaseApiResult keyList() {
        List<String> keyList = redisService.getSearchHistoryByUserId("userId");
        return BaseApiResult.success(keyList);
    }


    @GetMapping("/hot")
    public BaseApiResult hot() {
        List<String> keyList = redisService.getHotList(null, RedisServiceImpl.SEARCH_KEY);
        return BaseApiResult.success(keyList);
    }
}
