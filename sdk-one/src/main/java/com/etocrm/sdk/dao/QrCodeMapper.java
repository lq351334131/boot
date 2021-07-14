package com.etocrm.sdk.dao;

import com.etocrm.sdk.entity.Qr.*;

import java.util.List;

public interface QrCodeMapper {
    
    Integer getQrGroupCount(QrCodePageVO queryCodePageVO);

    List<QrCodeRepVO> getCodeListPagging(QrCodePageVO queryCodePageVO);

    List<QrCodeRepVO> downloadPage(QrCodeVO qrCodeVO);

    List<QrCodeRepVOParVO> getCodeParam(QrCodeParam qrCodeParam);
}
