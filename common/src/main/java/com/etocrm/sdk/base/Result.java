package com.etocrm.sdk.base;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author qi.li
 * @create 2020/8/31 19:49
 */
@Data
public class Result implements Serializable {

    private static final long serialVersionUID = -5749406925526426452L;

    private int code;

    private String msg;

    private Object data;

    public Result() {}

    public Result(int code,String msg) {
        this.setCode(code);
        this.setMsg(msg);
    }

    public Result(int code,String msg,Object data) {
       this.setCode(code);
       this.setMsg(msg);
       this.setData(data);
    }

    public Result(ResponseCode responseCode) {
       this.code=responseCode.getCode();
       this.msg=responseCode.getMsg();
    }
    public Result(ResponseCode responseCode,Object data) {
        this.code=responseCode.getCode();
        this.msg=responseCode.getMsg();
        this.data=data;
    }
    public static Result success(){
        return new Result(ResponseCode.SUCCESS);
    }

    public static  Result success(Object data){
        return new Result(ResponseCode.SUCCESS, data);
    }

    public static  Result success(int code, String msg){
        return new Result(code, msg);
    }
    public static Result error(int code, String msg){
        return new Result(code,msg);
    }

    public static Result error(ResponseCode responseEnum){
        return new Result(responseEnum);
    }

    public static Result error(ResponseCode responseEnum, Object data){
        return new Result(responseEnum, data);
    }
}
