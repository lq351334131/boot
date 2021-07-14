package com.etocrm.sdk.entity.DO;

import cn.hutool.core.date.DateUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @Date 2020/10/14 14:36
 */
@Data
public class BaseDO {

    private Integer createdBy;

    private Date createdTime;

    private Integer updatedBy;

    private Date updatedTime;

    private Integer deleted;

    private Integer deleteBy;

    private Date deleteTime;
}
