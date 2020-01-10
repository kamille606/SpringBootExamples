package com.aaa.es.status;

/**
 * @author ：pyt
 * @date ：Created in 2019/12/16 18:50
 * @version: ${version}
 * @description：
 */
public enum  StatusEnum {

    EXIST("101","数据存在"),
    NOT_EXIST("401","数据不存在"),
    OPRATION_SUCCESS("200","操作成功"),
    OPRATION_FAILED("402","操作失败");

    StatusEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private String code;
    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
