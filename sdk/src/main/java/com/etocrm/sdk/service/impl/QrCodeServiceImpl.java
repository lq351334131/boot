package com.etocrm.sdk.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.etocrm.sdk.base.Result;
import com.etocrm.sdk.entity.qrcode.DeleteQrCodeVO;
import com.etocrm.sdk.entity.qrcode.GetQrCodeListVO;
import com.etocrm.sdk.entity.qrcode.QrCodeAddVO;
import com.etocrm.sdk.entity.qrcode.QrCodeEditVO;
import com.etocrm.sdk.entity.qrcodegroup.QrCodeGroupEditVO;
import com.etocrm.sdk.service.QrCodeService;
import com.etocrm.sdk.util.EsTable;
import com.etocrm.sdk.util.EsUtil;
import com.etocrm.sdk.util.PageUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Date 2020/10/13 18:07
 */
@Service
public class QrCodeServiceImpl implements QrCodeService {

    @Autowired
    private EsUtil esUtil;
    @Override
    public Result addQrCode(QrCodeAddVO qrCodeAddVO) {
        //1、验证名称是否存在
        QrCodeEditVO qrCodeEditVO=new QrCodeEditVO();
        BeanUtils.copyProperties(qrCodeAddVO,qrCodeEditVO);
        Result result=this.isReQrName(qrCodeEditVO,1);
        if(result.getCode()!=0){
            return  result;
        }
        //Todo 2、从微信获取二维码图片
        String id=this.esUtil.insert(EsTable.QR_CODE, JSON.toJSONString(qrCodeAddVO));
        return StringUtils.isNotBlank(id)?Result.success(id):Result.error(201,"添加失败");

    }

    @Override
    public Result deleteQrCode(DeleteQrCodeVO deleteQrCodeVO) {
        this.esUtil.deleteId(EsTable.QR_CODE,deleteQrCodeVO.getQrId());
        return Result.success();
    }

    @Override
    public Result getQrListByPage(GetQrCodeListVO getQrCodeListVO) {

        BoolQueryBuilder boolQueryBuilder=QueryBuilders.boolQuery();
        if(StringUtils.isNotBlank(getQrCodeListVO.getQrGroupId())){
            boolQueryBuilder.must(QueryBuilders.matchQuery("qrGroupId.keyword",getQrCodeListVO.getQrGroupId()));
        }
        if(StringUtils.isNotBlank(getQrCodeListVO.getQrName())){
            boolQueryBuilder.must(QueryBuilders.matchQuery("qrName.keyword",getQrCodeListVO.getQrName()));
        }
        boolQueryBuilder.must(QueryBuilders.matchQuery("appId.keyword",getQrCodeListVO.getAppId()));
        JSONArray jsonArray=this.esUtil.getData(
                boolQueryBuilder,
                EsTable.QR_CODE,
                new PageUtils(getQrCodeListVO.getPageSize(),getQrCodeListVO.getPageIndex(),null));
        return Result.success(jsonArray);
    }

    @Override
    public Result editQrCode(QrCodeEditVO qrCodeEditVO) {
        Result result=this.isReQrName(qrCodeEditVO,2);
        if(result.getCode()!=0){
            return  result;
        }
        boolean editInfo=this.esUtil.updateColum(EsTable.QR_CODE,qrCodeEditVO.getQrId(),JSON.toJSONString(qrCodeEditVO));
        return editInfo?Result.success():Result.error(201,"修改失败");
    }

    /**
     * 判断是否存在重名的二维码
     * type：1添加，2编辑
     * */
    private Result isReQrName(QrCodeEditVO qrCodeEditVO, int type){
        //先查询出是否有重名的二维码
        BoolQueryBuilder queryBuilder= QueryBuilders.boolQuery()
                .must(QueryBuilders.matchQuery("appId.keyword",qrCodeEditVO.getAppId().trim()))
                .must(QueryBuilders.matchQuery("groupName.keyword",qrCodeEditVO.getQrName().trim()));
        if(type==2){
            queryBuilder.mustNot(QueryBuilders.matchQuery("_id",qrCodeEditVO.getQrId().trim()));
        }
        JSONArray jsonArray=esUtil.getData(queryBuilder, EsTable.QR_CODE,new PageUtils(10,1,null));
        return jsonArray!=null&&jsonArray.size()>0?Result.error(201,"二维码名称不可重复"):Result.success();
    }
}
