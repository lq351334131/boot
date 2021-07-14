package com.etocrm.sdk.entity.share;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "分享")
public class ShareUserVO {

    //分享人数总和
   @ApiModelProperty(value = "分享人数总和")
   private Integer peopleNumTotal=0;

   //分享次数总和
   @ApiModelProperty(value = "分享次数总和")
   private Integer shareNumTotal=0;

   //回流量总和
   @ApiModelProperty(value = "回流量总和")
   private Integer  turnbackTotal=0;

   //分享回流比总和
   @ApiModelProperty(value = "分享回流比总和")
   private String  turnbackPropTotal="0.00";

   //分享新增人数总和
   @ApiModelProperty(value = "分享新增人数总和" )
   private Integer  userPlusNumTotal;

}
