package com.etocrm.sdk.entity.user;

import lombok.Data;

@Data
public class UserTypeVO  extends UserVO {

    private String groupName;

    private String value;
}
