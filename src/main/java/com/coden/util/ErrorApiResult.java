package com.coden.util;

import java.io.Serializable;

public final class ErrorApiResult extends BaseApiResult implements Serializable {

    private static final long serialVersionUID = 1L;

    public String message;

    public ErrorApiResult(Integer code, String message) {
        this.timestamp = System.currentTimeMillis();
        this.code = code;
        this.message = message;
    }

}
