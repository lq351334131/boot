package com.etocrm.sdk.entity.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import net.sf.oval.constraint.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class EventAnalysisExcelVO {

        @NotNull(message = "appKey不能为空")
        private String  appKey;

        private String eventName;

        private String eventKey;

        @NotNull(message = "开始时间不能为空",profiles = "profile_4")
        @DateTimeFormat(pattern="yyyy-MM-dd")
        @JsonFormat(pattern = "yyyy-MM-dd")
        private Date beginDate;

        @NotNull(message = "结束时间不能为空")
        @DateTimeFormat(pattern="yyyy-MM-dd")
        private Date endDate;
}
