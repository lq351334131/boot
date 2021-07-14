package com.etocrm.sdk.entity.report;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

@Data
public class ReportVO {

    @ExcelProperty(value = "Weekly Member Status", index = 0)
    @ColumnWidth(12)
    private String name;

    @ExcelProperty(value = "", index = 1)
    private String value;



}
