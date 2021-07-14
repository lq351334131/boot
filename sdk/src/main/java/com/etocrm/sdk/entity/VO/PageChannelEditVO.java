package com.etocrm.sdk.entity.VO;

import lombok.Data;
import net.sf.oval.constraint.NotNull;

import java.util.List;

@Data
public class PageChannelEditVO {

    private  String  appKey;

    private String  id;

    @NotNull
    private  String  name;

    @NotNull
    private List<ChannelVO> list;

    @NotNull
    private  String pathId;
}
