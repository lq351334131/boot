package com.etocrm.sdk.mysqldao;

import com.etocrm.sdk.entity.page.PageAccessVo;
import com.etocrm.sdk.entity.page.VisitTotalVO;

public interface PageDataMapper {

    VisitTotalVO getVisitPage(PageAccessVo pageAccessVo);


}
