package com.etocrm.tag.serverapi.fallback;

import com.etocrm.sdk.base.ResponseCode;
import com.etocrm.sdk.base.Result;
import com.etocrm.tag.entity.VO.TestVO;
import com.etocrm.tag.serverapi.SdkService;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author qi.li
 * @create 2020/9/16 18:23
 */
@Slf4j
@Component
public class SdkServiceFallbackImpl implements FallbackFactory<SdkService> {
    @Override
    public SdkService create(Throwable throwable) {
        return new SdkService() {
            @Override
            public Result getIndex(TestVO testVO) {
                log.info(String.valueOf(ResponseCode.CONNECT_TIMEOUT));
                return Result.error(ResponseCode.CANT_FIND_USER);
            }
        };
    }
}
