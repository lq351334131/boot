package com.etocrm.sdk.api.fallback;

import com.etocrm.sdk.api.TagService;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author qi.li
 * @create 2020/9/14 19:49
 */
@Slf4j
@Component
public class TagServiceFallBackFactory implements FallbackFactory<TagService> {


    @Override
    public TagService create(Throwable throwable) {
        return new TagService() {
        };
    }
}
