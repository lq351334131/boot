package com.etocrm.sdk.service;

import com.etocrm.sdk.base.Result;
import com.etocrm.sdk.entity.qrcodegroup.DeleteQrGroupVO;
import com.etocrm.sdk.entity.qrcodegroup.GetQrCodeGroupVO;
import com.etocrm.sdk.entity.qrcodegroup.QrCodeGroupAddVO;
import com.etocrm.sdk.entity.qrcodegroup.QrCodeGroupEditVO;

public interface QrCodeGroupService {

    Result addQrGroup(QrCodeGroupAddVO qrCodeGroupAddVO);

    Result editQrGroup(QrCodeGroupEditVO qrCodeGroupEditVO);

    Result deleteQrGroup(DeleteQrGroupVO deleteQrGroupVO);

    Result getQrCodeGroup(GetQrCodeGroupVO getQrCodeGroupVO);
}
