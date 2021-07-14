package com.etocrm.sdk.controller;

import com.etocrm.sdk.base.Result;
import com.etocrm.sdk.entity.campaign.GetPvUvByParamsVO;
import com.etocrm.sdk.service.CampaignService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Date 2021/6/8 11:09
 */
@Api(tags = "活动管理")
@ApiSort(100)
@Slf4j
@RestController
@RequestMapping("/api/campaign")
public class CampaignController {

    @Autowired
    private CampaignService campaignService;

    @ApiOperation(value = "获取pv/uv-不调用")
    @ApiOperationSupport(order = 10)
    @GetMapping("/getPvUvByParams")
    public Result getPvUvByParams(GetPvUvByParamsVO getPvUvByParamsVO) {
        return Result.success(campaignService.getPvUvByParams(getPvUvByParamsVO));
    }
}
