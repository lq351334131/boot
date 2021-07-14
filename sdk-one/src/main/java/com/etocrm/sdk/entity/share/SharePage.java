package com.etocrm.sdk.entity.share;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import net.sf.oval.constraint.NotNull;

@Data
@ApiModel(value = "分享")
public class SharePage  extends PageShareListVO {

    @NotNull
    private Integer pageSize=10;

    @NotNull
    private Integer pageIndex;

}
