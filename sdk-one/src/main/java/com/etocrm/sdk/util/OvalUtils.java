package com.etocrm.sdk.util;

import lombok.extern.slf4j.Slf4j;
import net.sf.oval.ConstraintViolation;
import net.sf.oval.Validator;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description: 参数校验
 * @author xing.liu
 * @date ${DATE} ${TIME}
 */
@Slf4j
@Component
public class OvalUtils {

    public   List<ConstraintViolation> validatorRequestParam(Object obj) {
        boolean flag = false;
        Validator validator = new Validator();
        List<ConstraintViolation> ret = validator.validate(obj);
        return  ret;
    }


}
