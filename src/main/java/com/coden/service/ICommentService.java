package com.coden.service;

import com.coden.entity.Comment;
import com.coden.entity.dto.BasePageDTO;
import com.coden.entity.dto.CommentListDTO;
import com.coden.util.BaseApiResult;

import java.util.List;

public interface ICommentService {

    /**
     * insert
     * @param comment Comment
     * @return result
     */
    BaseApiResult insert(Comment comment);

    /**
     * update
     * @param comment Comment
     * @return result
     */
    BaseApiResult update(Comment comment);

    /**
     * remove
     * @param comment Comment
     * @param userId userId
     * @return result
     */
    BaseApiResult remove(Comment comment, String userId);

    /**
     * @Description 批量删除评论列表
     * @Param [commentIdList]
     * @return BaseApiResult
     **/
    BaseApiResult removeBatch(List<String> commentIdList);

    /**
     * queryById
     * @param comment CommentListDTO
     * @return result
     */
    BaseApiResult queryById(CommentListDTO comment);

    /**
     * search
     * @param comment Comment
     * @return result
     */
    BaseApiResult search(Comment comment);

    /**
     * @Description 查询评论列表
     * @Param [pageDTO, userId]
     * @return BaseApiResult
     **/
    BaseApiResult queryAllComments(BasePageDTO pageDTO, String userId, Boolean isAdmin);

}
