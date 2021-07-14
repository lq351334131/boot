package com.etocrm.sdk.service;

import com.etocrm.sdk.base.Result;

public interface DataApiService {

    Result getVisit();

    Result getTotal();

    Result getEveryday();

    Result getReg();

    Result getScene();

    Result getQrCode();

    Result getEvent();

    Result getShop();

    Result getSearch();

    Result getShare();

    Result getShareTotal();

    Result getEntryTotal();

    Result getOpenTotal();

    Result getOpenDetail();
}
