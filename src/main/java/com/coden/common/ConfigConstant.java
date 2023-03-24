package com.coden.common;

/**
 * @ClassName ConfigConstant
 * @Description 整个系统需要的配置常数，统一配置避免出现不匹配的情况
 * @Version 1.0
 **/
public class ConfigConstant {

    private ConfigConstant() {
        throw new IllegalStateException("ConfigConstant class");
    }

    /**
     * 删除操作的最大数
     */
    public static final Integer MAX_DELETE_NUM = 100;
}
