package com.coden.common;

/**
 * @ClassName RegexConstant
 * @Description 正则表达式
 * @Version 1.0
 **/
public class RegexConstant {

    private RegexConstant() {
        throw new IllegalStateException("RegexConstant class error!");
    }

    /**
     * 中英文下划线横向，1-64位
     */
    public static final String CH_ENG_WORD = "^[\\u4E00-\\u9FA5A-Za-z0-9_-]{1,64}$";

    /**
     * @Description 只能是数字，大小字母，下划线组成
     * @Param
     **/
    public static final String NUM_WORD_REG = "^[A-Za-z0-9_]+$";

}
