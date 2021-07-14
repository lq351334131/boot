package com.etocrm.sdk.service;

import com.etocrm.sdk.base.Result;
import com.etocrm.sdk.entity.Qr.*;

import java.util.List;

public interface QrCodeService {

    Result getQrCodeListPagging(QrCodePageVO queryCodePageVO);

    Result addQrCode(AddQrCodeVO addQrCodeVO);

    Result editQrCode(EditQrCodeVO editQrCodeVO);

    Result deleteQrCode(DeleteQrCodeVO deleteQrCodeVO);

    List<QrCodeRepVO> downLoadQrListPagging(QrCodeVO qrCodeVO);

    Result getCodeParam(QrCodeParam qrCodeParam);
}
