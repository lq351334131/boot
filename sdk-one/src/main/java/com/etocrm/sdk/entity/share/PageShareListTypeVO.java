package com.etocrm.sdk.entity.share;

import lombok.Data;

import java.util.List;

@Data
public class PageShareListTypeVO extends PageShareListVO {

    private Integer  type;

    private List<String> list;


}
