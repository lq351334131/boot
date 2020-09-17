package com.etocrm.sdk.base;

/**
 * @Author qi.li
 * @create 2020/8/31 19:49
 */
public enum ResponseCode {
    /*------Base Code------*/
    SUCCESS(0,"ok"),
    Fail(-1, "fail"),
    CONNECT_TIMEOUT(-100,"connect service timeout"),

    PARAMETERS_NULL(1, "parameter is null"),
    PARSE_PARAMETERS_ERROR(2, "parse parameters error"),
    PARAMETERS_ERROR(3, "parameters error"),
    CANT_FIND_RECORD(4, "cant find record"),
    CANT_FIND_USER(5,"cant find user"),
    CANT_FIND_ROUTER(6, "cant find router"),
    CANT_FIND_PRODUCT(7,"cant find product"),
    CANT_FIND_WALLET(8,"cant find wallet"),
    CANT_FIND_ORDERS(9,"cant find orders"),
    ES_EXCEPTION(10,"es exception");

    private int code;
    private String msg;

    ResponseCode(int code, String msg){
        this.code = code;
        this.msg = msg;
    }


    public int getCode() {
        return code;
    }

    public String getMsg(){
        return msg;
    }
}
