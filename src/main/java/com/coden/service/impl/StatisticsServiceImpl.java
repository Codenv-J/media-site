package com.coden.service.impl;

import com.coden.entity.CateDocRelationship;
import com.coden.entity.Category;
import com.coden.entity.FileDocument;
import com.coden.entity.vo.DocVO;
import com.coden.entity.vo.StatsVO;
import com.coden.entity.vo.TrendVO;
import com.coden.service.StatisticsService;
import com.coden.util.BaseApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    CategoryServiceImpl categoryServiceImpl;

    @Autowired
    FileServiceImpl fileServiceImpl;

    @Autowired
    TagServiceImpl tagServiceImpl;

    @Autowired
    CommentServiceImpl commentServiceImpl;


    /**
     * @return ApiResult
     * @Description 统计随机的三个分类
     * @Param []
     **/
    @Override
    public BaseApiResult trend() {
        List<Category> categoryList = categoryServiceImpl.getRandom();
        List<TrendVO> trendVos = new ArrayList<>(3);

        for (Category category : categoryList) {
            category = Optional.ofNullable(category).orElse(new Category());
            TrendVO trendVO = new TrendVO();
            trendVO.setId(category.getId());
            trendVO.setName(category.getName());
            List<DocVO> docVos = new ArrayList<>();

            if (category.getId() != null) {
                List<FileDocument> documents;
                List<CateDocRelationship> relationships = categoryServiceImpl.getRelateByCateId(category.getId());
                List<String> ids = relationships.stream().map(CateDocRelationship::getFileId).collect(Collectors.toList());
                documents = fileServiceImpl.listAndFilterByPage(0, 4, ids);
                documents = Optional.ofNullable(documents).orElse(new ArrayList<>(8));
                for (FileDocument document : documents) {
                    document = Optional.ofNullable(document).orElse(new FileDocument());
                    DocVO docVO = new DocVO();
                    docVO.setId(document.getId());
                    docVO.setName(document.getName());
                    docVos.add(docVO);
                }
            }

            trendVO.setDocList(docVos);
            trendVos.add(trendVO);
        }
        return BaseApiResult.success(trendVos);
    }

    /**
     * @return ApiResult
     * @Description 统计数量
     * @Param []
     **/
    @Override
    public BaseApiResult all() {
        StatsVO statsVO = new StatsVO();
        statsVO.setDocNum(fileServiceImpl.countAllFile());
        statsVO.setCommentNum(commentServiceImpl.countAllFile());
        statsVO.setCategoryNum(categoryServiceImpl.countAllFile());
        statsVO.setTagNum(tagServiceImpl.countAllFile());
        return BaseApiResult.success(statsVO);
    }

}
