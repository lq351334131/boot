package com.etocrm.tag.serverapi;

import com.etocrm.sdk.base.Result;
import com.etocrm.tag.entity.VO.TestVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Author qi.li
 * @create 2020/9/16 18:14
 */

@FeignClient(name="etocrm-sdk-server")
public interface SdkService {
    @PostMapping("/sdk/test")
    Result  getIndex(@RequestBody TestVO testVO);
}
