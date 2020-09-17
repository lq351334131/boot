package com.etocrm.sdk.entity.VO;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

@Data
public class PageListDownloadVO  extends BaseRowModel {
    @ExcelProperty(value = "所属功能模块", index = 2)
    private String moduleName;

    @ExcelProperty(value = "页面名称", index = 1)
    private String pathName;

    @ExcelProperty(value = "所属统计模块", index = 3)
    private Integer pathTypeId;

    @ExcelProperty(value = "页面URL", index = 0)
    private String visitPath;




}