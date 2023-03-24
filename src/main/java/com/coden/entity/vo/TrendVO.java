package com.coden.entity.vo;

import lombok.Data;

import java.util.List;


@Data
public class TrendVO {


    private String id;

    private String name;

    private List<DocVO> docList;

}
