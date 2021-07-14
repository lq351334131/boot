package com.etocrm.sdk.entity.event;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class EventUserRepVO {

    @ExcelProperty(value = "userId", index = 0)
    private String userId;

    @ExcelProperty(value = "unionId", index = 0)
    private String unionId;

    @ExcelProperty(value = "nickName", index = 0)
    private String nickName;

    @ExcelProperty(value = "openId", index = 0)
    private String openId;

    /**
     * 
     * @Description uid  userId一样 张锋userId是注解id
     * @author xing.liu
     * @date 2020/11/24 
     **/

   // private String uid;

}
