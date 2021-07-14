package com.etocrm.sdk.entity.share;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class UserCityRepVO {

        @ExcelProperty(value = "昵称", index = 0)
        private  String nickName;

        @ExcelProperty(value = "城市", index = 1)
        private String city;

        @ExcelProperty(value = "性别", index = 2)
        private Integer gender;

        //分享次数总和
        @ExcelProperty(value = "分享次数", index = 3)
        private Integer shareNum;

        @ExcelProperty(value = "回流量", index = 4)
        private Integer turnback;

        //分享回流比
        @ExcelProperty(value = "分享回流比", index = 5)
        private String turnbackProp;

        @ExcelProperty(value = "分享新增人数", index = 6)
        private Integer userPlusNum;

}
