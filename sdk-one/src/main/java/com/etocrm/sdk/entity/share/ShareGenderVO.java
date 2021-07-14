package com.etocrm.sdk.entity.share;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "分享性别")
public class ShareGenderVO {

    private String gender;

    private String rate;

    @JsonIgnore
    private Integer num;

}
