package com.etocrm.sdk.entity.eventVO;

import lombok.Data;
import net.sf.oval.constraint.NotNull;

import java.util.Date;

@Data
public class EventEsVO {

    @NotNull
    private String  appKey;

    @NotNull
    private String tv;

    @NotNull
    private String tl;

    private Date createTime;

    private  String  id;



}
