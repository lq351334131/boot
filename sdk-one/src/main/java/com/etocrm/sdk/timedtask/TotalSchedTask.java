package com.etocrm.sdk.timedtask;

import com.etocrm.sdk.service.TotalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@Slf4j
/**
 * @Description 指标存入mysql
 * @author xing.liu
 * @date 2021/4/14
 **/
public class TotalSchedTask {

    @Autowired
    private TotalService totalService;


    //@Scheduled(cron = "0/23 * * * * ? ") //0 5 1 * * ?
    @Scheduled(cron = "0 0 6 * * ?")
    public void getSed() {
        totalService.insertTotal();
    }


}
