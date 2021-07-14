package com.etocrm.sdk.entity.Qr;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class QrGroupRepVO {

    @ExcelProperty(index = 0 ,value = "id")
    private String id;

    @ExcelProperty(index = 1 ,value = "二维码组名称")
    private String groupName;

    @ExcelProperty(index = 2 ,value = "创建时间")
    private String createTime;

}
