package com.etocrm.sdk.dao;

import com.etocrm.sdk.entity.Qr.*;

import java.util.List;

public interface QrCodeGroupMapper {

    void add(AddQrGroupType addQrGroup);

    void editQrGroup(AddQrGroupType addQrGroupType);

    void deleteQrGroup(DeleteQrGroup deleteQrGroup);

    List<QrGroupRepVO> getQrGroupListPagging(QueryGroupPageVO queryGroupPageVO);

    Integer getQrGroupCount(QueryGroupPageVO queryGroupPageVO);

    List<QrGroupRepVO> downLoadQrGroupListPagging(AddQrGroupType qrGroupList);
}
