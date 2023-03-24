package com.coden.controller;

import com.coden.common.MessageConstant;
import com.coden.common.RegexConstant;
import com.coden.entity.CateDocRelationship;
import com.coden.entity.Category;
import com.coden.entity.Tag;
import com.coden.entity.TagDocRelationship;
import com.coden.entity.dto.CategoryDTO;
import com.coden.entity.dto.QueryDocByTagCateDTO;
import com.coden.entity.dto.RelationDTO;
import com.coden.enums.FilterTypeEnum;
import com.coden.service.CategoryService;
import com.coden.service.TagService;
import com.coden.util.BaseApiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;


@Api(tags = "文档分类和标签模块")
@RestController
@Slf4j
@RequestMapping("/category")
@CrossOrigin
public class CategoryController {

    // 一个文章只能有一个分类项目
    // 一个文章下可能有多个列表

    @Resource
    CategoryService categoryService;

    @Resource
    TagService tagService;

    @ApiOperation(value = "新增单个分类，可选分类或者标签", notes = "新增单个分类")
    @PostMapping(value = "/insert")
    public BaseApiResult insert(@RequestBody CategoryDTO categoryDTO) {
        // 插入进来的参数必需经过清洗
        categoryDTO.setId(null);
        String name = categoryDTO.getName();
        if (!name.matches(RegexConstant.CH_ENG_WORD)) {
            return BaseApiResult.error(MessageConstant.PARAMS_ERROR_CODE, MessageConstant.PARAMS_FORMAT_ERROR);
        }
        switch (categoryDTO.getType()) {
            case CATEGORY:
                Category category = new Category();
                category.setName(categoryDTO.getName());
                category.setCreateDate(new Date());
                category.setUpdateDate(new Date());
                return categoryService.insert(category);
            case TAG:
                Tag tag = new Tag();
                tag.setName(categoryDTO.getName());
                tag.setCreateDate(new Date());
                tag.setUpdateDate(new Date());
                return tagService.insert(tag);
            default:
                return BaseApiResult.error(MessageConstant.PARAMS_ERROR_CODE, MessageConstant.PARAMS_FORMAT_ERROR);
        }
    }

    @ApiOperation(value = "更新分类", notes = "更新分类")
    @PutMapping(value = "/update")
    public BaseApiResult update(@RequestBody CategoryDTO categoryDTO) {
        String name = categoryDTO.getName();
        if (!name.matches(RegexConstant.CH_ENG_WORD)) {
            return BaseApiResult.error(MessageConstant.PARAMS_ERROR_CODE, MessageConstant.PARAMS_FORMAT_ERROR);
        }
        switch (categoryDTO.getType()) {
            case CATEGORY:
                Category category = new Category();
                category.setName(categoryDTO.getName());
                category.setId(categoryDTO.getId());
                category.setUpdateDate(new Date());
                return categoryService.update(category);
            case TAG:
                Tag tag = new Tag();
                tag.setName(categoryDTO.getName());
                tag.setId(categoryDTO.getId());
                tag.setUpdateDate(new Date());
                return tagService.update(tag);
            default:
                return BaseApiResult.error(MessageConstant.PARAMS_ERROR_CODE, MessageConstant.PARAMS_FORMAT_ERROR);
        }
    }

    @ApiOperation(value = "根据id移除某个分类", notes = "根据id移除某个分类")
    @DeleteMapping(value = "/remove")
    public BaseApiResult remove(@RequestBody CategoryDTO categoryDTO) {
        switch (categoryDTO.getType()) {
            case CATEGORY:
                Category category = new Category();
                category.setId(categoryDTO.getId());
                return categoryService.remove(category);
            case TAG:
                Tag tag = new Tag();
                tag.setId(categoryDTO.getId());
                return tagService.remove(tag);
            default:
                return BaseApiResult.error(MessageConstant.PARAMS_ERROR_CODE, MessageConstant.PARAMS_FORMAT_ERROR);
        }
    }

    @ApiOperation(value = "查询所有的分类或者是标签", notes = "查询列表")
    @GetMapping(value = "/all")
    public BaseApiResult list(@RequestParam FilterTypeEnum type) {
        switch (type) {
            case CATEGORY:
                return categoryService.list();
            case TAG:
                return tagService.list();
            default:
                return BaseApiResult.error(MessageConstant.PARAMS_ERROR_CODE, MessageConstant.PARAMS_FORMAT_ERROR);
        }
    }

    /**
     * 同步动作，一个文档只能有一个分类关系，不能出现一对多
     *
     * @param relationDTO RelationDTO
     * @return BaseApiResult
     */
    @ApiOperation(value = "增加关系", notes = "检索分类")
    @PostMapping(value = "/addRelationship")
    public synchronized BaseApiResult addRelationship(@RequestBody RelationDTO relationDTO) {
        switch (relationDTO.getType()) {
            case CATEGORY:
                CateDocRelationship category = new CateDocRelationship();
                category.setCategoryId(relationDTO.getId());
                category.setFileId(relationDTO.getDocId());
                return categoryService.addRelationShip(category);
            case TAG:
                TagDocRelationship tag = new TagDocRelationship();
                tag.setTagId(relationDTO.getId());
                tag.setFileId(relationDTO.getDocId());
                tag.setCreateDate(new Date());
                tag.setUpdateDate(new Date());
                return tagService.addRelationShip(tag);
            default:
                return BaseApiResult.error(MessageConstant.PARAMS_ERROR_CODE, MessageConstant.PARAMS_FORMAT_ERROR);
        }
    }

    @ApiOperation(value = "断开连接关系", notes = "检索分类")
    @DeleteMapping(value = "/removeRelationship")
    public BaseApiResult removeRelationship(@RequestBody RelationDTO relationDTO) {
        switch (relationDTO.getType()) {
            case CATEGORY:
                CateDocRelationship category = new CateDocRelationship();
                category.setCategoryId(relationDTO.getId());
                category.setFileId(relationDTO.getDocId());
                return categoryService.cancelCategoryRelationship(category);
            case TAG:
                TagDocRelationship tag = new TagDocRelationship();
                tag.setTagId(relationDTO.getId());
                tag.setFileId(relationDTO.getDocId());
                return tagService.cancelTagRelationship(tag);
            default:
                return BaseApiResult.error(MessageConstant.PARAMS_ERROR_CODE, MessageConstant.PARAMS_FORMAT_ERROR);
        }
    }

    @ApiOperation(value = "根据分类、标签查询", notes = "查询文档列表信息")
    @GetMapping(value = "getDocByTagCateKeyWord")
    public BaseApiResult getDocByTagCateKeyWord(@ModelAttribute("pageDTO") QueryDocByTagCateDTO pageDTO) {
        return categoryService.getDocByTagAndCate(pageDTO.getCateId(), pageDTO.getTagId(), pageDTO.getKeyword(),
                Integer.toUnsignedLong(pageDTO.getPage() - 1), Integer.toUnsignedLong(pageDTO.getRows()));
    }


}
