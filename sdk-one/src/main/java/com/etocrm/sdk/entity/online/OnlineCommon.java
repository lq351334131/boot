package com.etocrm.sdk.entity.online;

import lombok.Data;

@Data
public class OnlineCommon {

    private int year;                     //年份
    private int quarter;                  //季度
    private int month;
    private String unionId;  //// 公众号unionid ;
    private String openId;
    private String mobiePhone;  //手机号
    private String nickname;
    private String visitpath;
    private String appkey;
    //@param {String} uu 32位uuid
    private String uu;
    private  Long t;           //获取时间戳
    private  String dt;
    private String time;

}
