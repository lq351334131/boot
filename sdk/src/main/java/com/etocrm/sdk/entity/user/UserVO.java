package com.etocrm.sdk.entity.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import net.sf.oval.constraint.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class UserVO {

    @NotNull
    private String appKey;

    @NotNull(message = "开始时间不能为空")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date beginDate;

    @NotNull(message = "结束时间不能为空")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date endDate;
}
