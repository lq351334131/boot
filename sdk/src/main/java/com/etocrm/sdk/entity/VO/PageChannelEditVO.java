package com.etocrm.sdk.entity.VO;

import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
public class PageChannelEditVO {

    private  String  appKey;

    private String  id;

    @NonNull
    private  String  name;

    @NonNull
    private List<ChannelVO> list;

    @NonNull
    private  String pathId;
}
