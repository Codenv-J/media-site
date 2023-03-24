package com.coden.entity.vo;

import com.coden.enums.DocStateEnum;
import lombok.Data;

import java.util.Date;
import java.util.List;


@Data
public class DocumentVO {

    private String id;

    private String title;

    private String description;

    private Long size;

    private Long collectNum;

    private Long commentNum;

    private CategoryVO categoryVO;

    private List<TagVO> tagVOList;

    private String userName;

    private String thumbId;

    private DocStateEnum docState;

    private String errorMsg;

    private String txtId;

    private String previewFileId;

    private Date createTime;


    private String score;

}
