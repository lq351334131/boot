package com.etocrm.sdk.entity.scene;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.sf.oval.constraint.NotNull;

@Data
public class SceneDataVO {

        /*@NotNull(message = "开始时间不能为空")
        @DateTimeFormat(pattern="yyyy-MM-dd")
        @JsonFormat(pattern = "yyyy-MM-dd")
        private Date beginDate;

        @NotNull(message = "结束时间不能为空")
        @DateTimeFormat(pattern="yyyy-MM-dd")
        private Date endDate;*/

        @NotNull
        @ApiModelProperty(required = true)
        private String appKey;

        @NotNull
        @ApiModelProperty(value = "条数",required = true)
        private Integer pageSize=10;

        @NotNull
        @ApiModelProperty(value = "页码",required = true)
        private Integer pageIndex=1;

        @NotNull
        @ApiModelProperty(value = "开始时间",required = true)
        private String beginDate;

        @NotNull
        @ApiModelProperty(value = "结束时间",required = true)
        private String endDate;

}
