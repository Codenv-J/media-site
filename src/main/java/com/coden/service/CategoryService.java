package com.coden.service;

import com.coden.entity.CateDocRelationship;
import com.coden.entity.Category;

import com.coden.util.BaseApiResult;

public interface CategoryService {

    /**
     * 新增分类
     * @param category -> Category 实体
     * @return -> ApiResult
     */
    BaseApiResult insert(Category category);

    /**
     * 更新分类信息
     * @param category -> Category 实体
     * @return -> ApiResult
     */
    BaseApiResult update(Category category);

    /**
     * 移除现有的分类
     * @param category -> Category 实体
     * @return -> ApiResult
     */
    BaseApiResult remove(Category category);


    /**
     * 根据分类的各种信息进行查询
     * @param category -> Category 实体
     * @return -> ApiResult
     */
    BaseApiResult search(Category category);

    /**
     * 查询分类的列表信息
     * @return BaseApiResult
     */
    BaseApiResult list();

    /**
     * 增加分类和文档的信息
     * @param relationship CateDocRelationship
     * @return BaseApiResult
     */
    BaseApiResult addRelationShip(CateDocRelationship relationship);

    /**
     * 取消分类和文档的关联
     * @param relationship CateDocRelationship
     * @return BaseApiResult
     */
    BaseApiResult cancelCategoryRelationship(CateDocRelationship relationship);

    /**
     * @Description 排查某个分类和文档是否存在关系
     * @Param [categoryId, fileId]
     * @return boolean
     **/
    boolean relateExist(String categoryId, String fileId);

    /**
     * @Description 更具文档的分类和标签、关键字进行联合查询
     * @Param []
     * @return BaseApiResult
     **/
    BaseApiResult getDocByTagAndCate(String cateId, String tagId, String keyword, Long pageNum, Long pageSize);

}
