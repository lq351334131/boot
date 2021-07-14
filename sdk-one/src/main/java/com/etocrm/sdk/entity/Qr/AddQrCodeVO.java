package com.etocrm.sdk.entity.Qr;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.sf.oval.constraint.NotNull;

import java.util.List;

@Data
@ApiModel(value = "二维码添加")
public class AddQrCodeVO {

        //@NotNull
        @ApiModelProperty(value = "维码名称不能为空",required = true )
        private String appId;

       // @NotNull
      //  private String appKey;

        @NotNull
        @ApiModelProperty(value = "维码名称不能为空",required = true )
        private String qrName;

        @NotNull
        @ApiModelProperty(value = "appSecret不能为空",required = true )
        private String appSecret;

        @NotNull
        @ApiModelProperty(value = "二维码组ID",required = true )
        private String qrGroupId;

        @NotNull
        @ApiModelProperty(value = "url",required = true )
        private String page;

       // @NotNull
        //private String imgUrl;

        @ApiModelProperty(value = "样式" )
        private String codeWidth;

        @ApiModelProperty(value = "尺寸" )
        private String codeType;

        @NotNull
        @ApiModelProperty(value = "scene",required = true )
        private List<QrCodeSceneVO> scene;

        @JsonIgnore
        private Integer  type;




}
