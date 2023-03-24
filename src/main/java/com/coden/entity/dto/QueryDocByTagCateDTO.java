package com.coden.entity.dto;

import lombok.Data;

/**
 * TODO
 **/
@Data
public class QueryDocByTagCateDTO extends BasePageDTO{

    String cateId;

    String tagId;

    String keyword;

}
