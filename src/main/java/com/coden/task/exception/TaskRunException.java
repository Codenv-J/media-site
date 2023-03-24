package com.coden.task.exception;

public class TaskRunException extends RuntimeException {


    static final long serialVersionUID = 781837582814609045L;


    public TaskRunException(){
        super();
    }

    public TaskRunException(String message) {
        super(message);
    }

    public TaskRunException(Throwable cause) {
        super(cause);
    }

    public TaskRunException(String message, Throwable cause) {
        super(message, cause);
    }
}
