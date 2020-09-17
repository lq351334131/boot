package com.etocrm.sdk.api;

import com.etocrm.sdk.api.fallback.TagServiceFallBackFactory;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Author qi.li
 * @create 2020/9/14 19:45
 */
@FeignClient(name="etocrm-tag-server" ,fallbackFactory = TagServiceFallBackFactory.class)
public interface TagService {


}
