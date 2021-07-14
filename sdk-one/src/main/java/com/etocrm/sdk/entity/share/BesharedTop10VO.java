package com.etocrm.sdk.entity.share;

import com.etocrm.sdk.entity.databroad.DataBroadVO;
import lombok.Data;

import java.util.List;

@Data
public class BesharedTop10VO extends DataBroadVO {

    private List<String> list;

    private Integer type=0;


}
