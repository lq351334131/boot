package com.etocrm.sdk.entity.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import net.sf.oval.constraint.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class ParamSearchVO {

    @NotNull
    private String appKey;

    @NotNull
    private  String  eventId;

    @NotNull(message = "开始时间不能为空",profiles = "profile_4")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date beginDate;

    @NotNull(message = "结束时间不能为空")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    @NotNull
    private Integer pageSize;

    @NotNull
    private Integer pageIndex;

    @NotNull
    private Integer type;

    @JsonIgnore
    private String tv;

    @JsonIgnore
    private String tl;



}
