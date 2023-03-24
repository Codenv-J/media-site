package com.coden.controller;

import com.coden.auth.Permission;
import com.coden.auth.PermissionEnum;
import com.coden.entity.dto.BasePageDTO;
import com.coden.entity.dto.BatchIdDTO;
import com.coden.entity.dto.RefuseBatchDTO;
import com.coden.entity.dto.RefuseDTO;
import com.coden.service.DocReviewService;
import com.coden.service.IFileService;
import com.coden.util.BaseApiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


@Api("文档评审模块")
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/docReview")
public class DocReviewController {

    @Autowired
    private DocReviewService docReviewService;

    @Resource
    private IFileService fileService;

    /**
     * 普通用户、管理员用户，列表查询
     * 该接口必须在登录条件下才能查询
     * 普通普通查询到自己上传的文档
     * 管理员查询到所有的文档评审信息
     * 必须是管理员才有资格进行评审
     *
     * @return BaseApiResult
     */
    @Permission({PermissionEnum.ADMIN})
    @ApiOperation(value = "查询需要评审的文档列表", notes = "查询需要评审的文档列表")
    @GetMapping("queryDocForReview")
    public BaseApiResult queryDocReviewList(@ModelAttribute("pageParams") @Valid BasePageDTO pageParams) {
        return fileService.queryFileDocumentResult(pageParams, true);
    }

    /**
     * 修改已读， 只有普通用户有此权限
     * 修改评审意见为通过
     * 用户必须是文档的上传人
     *
     * @return BaseApiResult
     */
    @Permission({PermissionEnum.ADMIN, PermissionEnum.USER})
    @ApiOperation(value = "修改已读", notes = "修改已读功能只有普通用户有此权限")
    @PutMapping("userRead")
    public BaseApiResult updateDocReview(@RequestBody @Valid BatchIdDTO batchIdDTO, HttpServletRequest request) {
        String userId = (String) request.getAttribute("id");
        return docReviewService.userRead(batchIdDTO.getIds(), userId);
    }


    @Permission({PermissionEnum.ADMIN})
    @ApiOperation(value = "管理员拒绝某个文档", notes = "管理员拒绝某个文档，只有管理员有操作该文档的权限")
    @PostMapping("refuse")
    public BaseApiResult refuse(@RequestBody @Validated RefuseDTO refuseDTO) {
        String docId = refuseDTO.getDocId();
        String reason = refuseDTO.getReason();
        return docReviewService.refuse(docId, reason);
    }


    @Permission({PermissionEnum.ADMIN})
    @ApiOperation(value = "管理员拒绝一批文档", notes = "管理员拒绝一批文档，并删除文档，只有管理员有操作该文档的权限")
    @PostMapping("refuseBatch")
    public BaseApiResult refuseBatch(@RequestBody @Valid RefuseBatchDTO refuseBatchDTO) {
        return docReviewService.refuseBatch(refuseBatchDTO.getIds(), refuseBatchDTO.getReason());
    }

    /**
     * @Description  缺少同意文档的信息
     * @Param [batchIdDTO]
     * @return BaseApiResult
     **/
    @Permission({PermissionEnum.ADMIN})
    @ApiOperation(value = "同意某一批文档", notes = "管理员同意某一批文档")
    @PostMapping("approve")
    public BaseApiResult approve(@RequestBody @Valid BatchIdDTO batchIdDTO) {
        return docReviewService.approveBatch(batchIdDTO.getIds());
    }


    @Permission({PermissionEnum.ADMIN})
    @ApiOperation(value = "管理员和普通用户分别查询数据", notes = "查询文档审批的列表")
    @GetMapping("queryReviewResultList")
    public BaseApiResult queryReviewResultList(@ModelAttribute("pageParams") @Valid BasePageDTO pageParams,
                                               HttpServletRequest request) {
        return docReviewService.queryReviewLog(pageParams, null, true);
    }

    /**
     * @return BaseApiResult
     * @Description 管理员和普通用户分别查询
     * @Param [pageParams, request]
     **/
    @Permission({PermissionEnum.USER, PermissionEnum.ADMIN})
    @ApiOperation(value = "管理员和普通用户分别查询数据", notes = "查询文档审批的列表")
    @GetMapping("queryMyReviewResultList")
    public BaseApiResult queryMyReviewResultList(@ModelAttribute("pageParams") @Valid BasePageDTO pageParams,
                                               HttpServletRequest request) {
        return docReviewService.queryReviewLog(pageParams, (String) request.getAttribute("id"), false);
    }


    /**
     * 普通用户删除，管理员删除，删除评审日志
     * @return BaseApiResult
     */
    @ApiOperation(value = "更新评论", notes = "更新评论")
    @DeleteMapping("removeDocReview")
    public BaseApiResult removeDocReview(@RequestBody @Valid BatchIdDTO batchIdDTO, HttpServletRequest request) {
        return docReviewService.deleteReviewsBatch(batchIdDTO.getIds(), (String) request.getAttribute("id"));
    }


}
