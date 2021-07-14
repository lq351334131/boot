package com.etocrm.sdk.entity.scene;

import lombok.Data;

@Data
public class SceneTotalDetailDomain {

        private Integer scene;

        private String sceneName;

        private Integer newUseridCount=0;

        private Integer useridCount=0;

        private Integer useridVisitCount=0;

        /**
         *
         * @Description 打开
         * @author xing.liu
         * @date 2021/5/31
         **/
        private Integer totalSimpleBakCount=0;

        private String  avgDate="0.00";

        private String  bounceRate="0.00";





}
