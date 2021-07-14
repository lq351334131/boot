package com.etocrm.sdk.entity.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "数据总览-列表")
public class UserDataRepVO {

     //新增用户
     @ApiModelProperty(value = "新增用户" )
     private List<SubItemsRepVO> item1;

    //访问次数
    @ApiModelProperty(value = "访问次数" )
    private List<SubItemsRepVO> item2;

    //访问人数
    @ApiModelProperty(value = "访问人数" )
    private List<SubItemsRepVO> item3;

    //打开次数
    @ApiModelProperty(value = "打开次数" )
    private List<SubItemsRepVO> item4;

    //private List<SubItemsRepVO> item5;

    /*private List<SubItemsRepVO> item6;

    private List<SubItemsRepVO> item7;*/

}
