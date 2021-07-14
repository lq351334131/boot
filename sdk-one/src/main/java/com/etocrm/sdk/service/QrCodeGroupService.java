package com.etocrm.sdk.service;

import com.etocrm.sdk.base.Result;
import com.etocrm.sdk.entity.Qr.*;

import java.util.List;

public interface QrCodeGroupService {

    Result addQrGroup(AddQrGroup addQrGroup);

    Result editQrGroup(EditQrGroup addQrGroup);

    Result deleteQrGroup(DeleteQrGroup deleteQrGroup);

    Result getQrGroupListPagging(QueryGroupPageVO queryGroupPageVO);

    List<QrGroupRepVO> downLoadQrGroupListPagging(QrGroupList qrGroupList);

    Result getQrCodeGroup(QrGroupList qrGroupList);
}
