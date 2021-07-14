package com.etocrm.sdk.timedtask;

import com.etocrm.sdk.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @Description 电商事件分析,数据拆分到本地表
 * @author xing.liu
 * @date 2021/5/25
 **/
//@Component
//@Slf4j
public class OnlineEventTask {

    @Autowired
    private EventService eventService;

    @Scheduled(cron = "0/15 * * * * ? ")
    public  void batchShopInsert(){
        eventService.batchShopInsert();
    }

    @Scheduled(cron = "0/15 * * * * ? ")
    public  void batchAddCartInsert(){
        eventService.batchAddCartInsert();
    }

    @Scheduled(cron = "0/15 * * * * ? ")
    public  void batchCustInsert(){
        eventService.batchCustInsert();
    }
   @Scheduled(cron = "0/15 * * * * ? ")
    public  void batchSearchInsert(){
        eventService.batchSearchInsert();
    }



}
