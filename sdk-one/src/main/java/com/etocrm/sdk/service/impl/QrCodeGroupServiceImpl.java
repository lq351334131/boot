package com.etocrm.sdk.service.impl;

import com.etocrm.sdk.base.ResponseCode;
import com.etocrm.sdk.base.Result;
import com.etocrm.sdk.dao.QrCodeGroupMapper;
import com.etocrm.sdk.entity.Qr.*;
import com.etocrm.sdk.service.QrCodeGroupService;
import com.etocrm.sdk.util.OvalUtils;
import com.etocrm.sdk.util.PageUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class QrCodeGroupServiceImpl implements QrCodeGroupService {

    @Autowired
    private OvalUtils ovalUtils;

    @Autowired
    private QrCodeGroupMapper qrCodeGroupMapper;


    @Override
    public Result addQrGroup(AddQrGroup addQrGroup) {
        if (ovalUtils.validatorRequestParam(addQrGroup).size() > 0) return Result.error(ResponseCode.PARAMETERS_NULL);
        AddQrGroupType addQrGroupType=new AddQrGroupType();
        BeanUtils.copyProperties(addQrGroup,addQrGroupType);
        addQrGroupType.setType(1);
        addQrGroupType.setQrGroupId("0");
        qrCodeGroupMapper.add(addQrGroupType);
        return Result.success();
    }

    @Override
    public Result editQrGroup(EditQrGroup editQrGroup) {
        if (ovalUtils.validatorRequestParam(editQrGroup).size() > 0) return Result.error(ResponseCode.PARAMETERS_NULL);
        AddQrGroupType addQrGroupType=new AddQrGroupType();
        BeanUtils.copyProperties(editQrGroup,addQrGroupType);
        addQrGroupType.setType(1);
        addQrGroupType.setQrGroupId("0");
        qrCodeGroupMapper.editQrGroup(addQrGroupType);
        return Result.success();
    }

    @Override
    public Result deleteQrGroup(DeleteQrGroup deleteQrGroup) {
        if (ovalUtils.validatorRequestParam(deleteQrGroup).size() > 0) return Result.error(ResponseCode.PARAMETERS_NULL);
        qrCodeGroupMapper.deleteQrGroup(deleteQrGroup);
        return Result.success();
    }

    @Override
    public Result getQrGroupListPagging(QueryGroupPageVO queryGroupPageVO) {
        queryGroupPageVO.setType(1);
        Integer qrGroupCount = qrCodeGroupMapper.getQrGroupCount(queryGroupPageVO);
        PageUtils<QrGroupRepVO> pageUtils=new PageUtils<>(queryGroupPageVO.getPageSize(),queryGroupPageVO.getPageIndex(),qrGroupCount);
        queryGroupPageVO.setPageIndex(pageUtils.getPageIndex());
        List<QrGroupRepVO> qrGroupListPagging = qrCodeGroupMapper.getQrGroupListPagging(queryGroupPageVO);
        pageUtils.setLists(qrGroupListPagging);
        return Result.success(pageUtils);
    }

    @Override
    public List<QrGroupRepVO> downLoadQrGroupListPagging(QrGroupList qrGroupList) {
        if (ovalUtils.validatorRequestParam(qrGroupList).size() > 0) return Collections.emptyList();
        AddQrGroupType addQrGroupType=new AddQrGroupType();
        BeanUtils.copyProperties(qrGroupList,addQrGroupType);
        addQrGroupType.setType(1);
        addQrGroupType.setGroupName(qrGroupList.getValue());
        return qrCodeGroupMapper.downLoadQrGroupListPagging(addQrGroupType);
    }

    @Override
    public Result getQrCodeGroup(QrGroupList qrGroupList) {
        if (StringUtils.isEmpty(qrGroupList.getAppId())) return Result.error(ResponseCode.PARAMETERS_NULL);
        AddQrGroupType addQrGroupType=new AddQrGroupType();
        BeanUtils.copyProperties(qrGroupList,addQrGroupType);
        addQrGroupType.setType(1);
        addQrGroupType.setGroupName(qrGroupList.getValue());
        List<QrGroupRepVO> qrGroupRepVOS = qrCodeGroupMapper.downLoadQrGroupListPagging(addQrGroupType);
        return Result.success(qrGroupRepVOS);
    }
}
