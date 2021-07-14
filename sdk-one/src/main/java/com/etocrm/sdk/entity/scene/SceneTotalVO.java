package com.etocrm.sdk.entity.scene;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SceneTotalVO   {

    /**
     *
     * @Description分享人数总和
     * @author xing.liu
     * @date 2021/5/31
     **/
    @ApiModelProperty("分享人数")
    private Integer peopleNumTotal=0;

    @ApiModelProperty("分享次数")
    private Integer shareNumTotal=0;

    @ApiModelProperty("回流量")
    private Integer turnbackTotal=0;


    /**
     *
     * @Description分享回流比总和
     * @author xing.liu
     * @date 2021/5/31
     **/
    @ApiModelProperty("分享回流比总和")
    private String turnbackPropTotal="0.00";

   /**
    *
    * @Description 分享新增人数总和
    * @author xing.liu
    * @date 2021/5/31
    **/
   @ApiModelProperty("分享新增人数总和")
    private Integer userPlusNumTotal=0;

}
