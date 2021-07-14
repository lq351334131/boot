package com.etocrm.sdk.service;

import com.etocrm.sdk.base.Result;
import com.etocrm.sdk.entity.qrcode.DeleteQrCodeVO;
import com.etocrm.sdk.entity.qrcode.GetQrCodeListVO;
import com.etocrm.sdk.entity.qrcode.QrCodeAddVO;
import com.etocrm.sdk.entity.qrcode.QrCodeEditVO;

public interface QrCodeService {

    Result addQrCode(QrCodeAddVO qrCodeAddVO);

    Result deleteQrCode(DeleteQrCodeVO deleteQrCodeVO);

    Result getQrListByPage(GetQrCodeListVO getQrCodeVO);

    Result editQrCode(QrCodeEditVO qrCodeEditVO);
}
