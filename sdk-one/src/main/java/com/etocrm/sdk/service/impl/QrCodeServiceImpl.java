package com.etocrm.sdk.service.impl;

import com.alibaba.fastjson.JSON;
import com.etocrm.sdk.base.ResponseCode;
import com.etocrm.sdk.base.Result;
import com.etocrm.sdk.dao.QrCodeGroupMapper;
import com.etocrm.sdk.dao.QrCodeMapper;
import com.etocrm.sdk.entity.Qr.*;
import com.etocrm.sdk.service.QrCodeService;
import com.etocrm.sdk.util.OvalUtils;
import com.etocrm.sdk.util.PageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class QrCodeServiceImpl implements QrCodeService {

    @Autowired
    private OvalUtils ovalUtils;

    @Autowired
    private QrCodeMapper qrCodeMapper;
    @Autowired
    private QrCodeGroupMapper qrCodeGroupMapper;



    @Override
    public Result getQrCodeListPagging(QrCodePageVO queryCodePageVO) {
        if (ovalUtils.validatorRequestParam(queryCodePageVO).size() > 0) return Result.error(ResponseCode.PARAMETERS_NULL);
        queryCodePageVO.setType(2);
        Integer qrGroupCount = qrCodeMapper.getQrGroupCount(queryCodePageVO);
        PageUtils<QrCodeRepVO> pageUtils=new PageUtils<>(queryCodePageVO.getPageSize(),queryCodePageVO.getPageIndex(),qrGroupCount);
        queryCodePageVO.setPageIndex(pageUtils.getPageIndex());
        List<QrCodeRepVO> qrGroupListPagging = qrCodeMapper.getCodeListPagging(queryCodePageVO);
        pageUtils.setLists(qrGroupListPagging);
        return Result.success(pageUtils);
    }

    @Override
    public Result addQrCode(AddQrCodeVO addQrCodeVO) {
        if (ovalUtils.validatorRequestParam(addQrCodeVO).size() > 0) return Result.error(ResponseCode.PARAMETERS_NULL);
        AddQrGroupType addQrGroupType=new AddQrGroupType();
        BeanUtils.copyProperties(addQrCodeVO,addQrGroupType);
        addQrGroupType.setType(2);
        List<QrCodeSceneVO> scene = addQrCodeVO.getScene();
        if(scene.size()>6){
            return  Result.error(201,"场景值列表最多六条");
        }
        addQrGroupType.setScene(JSON.toJSONString(scene));
        qrCodeGroupMapper.add(addQrGroupType);
        return Result.success();
    }

    @Override
    public Result editQrCode(EditQrCodeVO editQrGroup) {
        if (ovalUtils.validatorRequestParam(editQrGroup).size() > 0) return Result.error(ResponseCode.PARAMETERS_NULL);
        AddQrGroupType addQrGroupType=new AddQrGroupType();
        BeanUtils.copyProperties(editQrGroup,addQrGroupType);
        addQrGroupType.setType(2);
        if(editQrGroup.getScene().size()>6){
            return  Result.error(201,"场景值列表最多六条");
        }
        qrCodeGroupMapper.editQrGroup(addQrGroupType);
        return Result.success();
    }

    @Override
    public Result deleteQrCode(DeleteQrCodeVO deleteQrCodeVO) {
        if (ovalUtils.validatorRequestParam(deleteQrCodeVO).size() > 0) return Result.error(ResponseCode.PARAMETERS_NULL);
        DeleteQrGroup deleteQrGroup=new DeleteQrGroup();
        deleteQrGroup.setAppId(deleteQrCodeVO.getAppId());
        deleteQrGroup.setId(deleteQrCodeVO.getQrId());
        qrCodeGroupMapper.deleteQrGroup(deleteQrGroup);
        return Result.success();
    }

    @Override
    public List<QrCodeRepVO> downLoadQrListPagging(QrCodeVO qrCodeVO) {
        if (ovalUtils.validatorRequestParam(qrCodeVO).size() > 0) return Collections.emptyList();
        return qrCodeMapper.downloadPage(qrCodeVO);
    }

    @Override
    public Result getCodeParam(QrCodeParam qrCodeParam) {
        List<QrCodeRepVOParVO> codeParam = qrCodeMapper.getCodeParam(qrCodeParam);
        return Result.success(codeParam);
    }

}
