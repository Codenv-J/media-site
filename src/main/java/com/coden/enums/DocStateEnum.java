package com.coden.enums;

/**
 * 文档建立索引时候的状态
 **/
public enum DocStateEnum {

    /**
     * 建立索引时的等待状态，默认都是等待状态
     */
    WAITE(),
    /**
     * 进行中的状态
     */
    ON_PROCESS(),
    /**
     * 成功状态
     */
    SUCCESS(),
    /**
     * 失败状态
     */
    FAIL();

}
