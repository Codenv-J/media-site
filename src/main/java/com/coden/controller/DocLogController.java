package com.coden.controller;

import com.coden.auth.Permission;
import com.coden.auth.PermissionEnum;
import com.coden.entity.dto.BasePageDTO;
import com.coden.entity.dto.BatchIdDTO;
import com.coden.service.IDocLogService;
import com.coden.util.BaseApiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;


@Api(tags = "文档日志模块")
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/docLog")
public class DocLogController {

    @Resource
    private IDocLogService docLogService;

    @Permission({PermissionEnum.ADMIN})
    @ApiOperation(value = "管理员查询系统日志信息", notes = "只有管理员有权限查询日志列表")
    @GetMapping("queryLogList")
    public BaseApiResult queryLogList(@ModelAttribute("pageParams") @Valid BasePageDTO pageParams, HttpServletRequest request) {
        return docLogService.queryDocLogs(pageParams, (String) request.getAttribute("id"));
    }


    @Permission(PermissionEnum.ADMIN)
    @ApiOperation(value = "管理员删除文档信息", notes = "只有管理员有权限删除文档的日志")
    @DeleteMapping("removeLog")
    public BaseApiResult removeLog(@RequestBody @Valid BatchIdDTO batchIdDTO, HttpServletRequest request) {
        List<String> logIds = batchIdDTO.getIds();
        return docLogService.deleteDocLogBatch(logIds,(String) request.getAttribute("id"));
    }

}
