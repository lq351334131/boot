package com.etocrm.sdk.service.impl;

import com.alibaba.excel.util.DateUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.etocrm.sdk.base.Result;
import com.etocrm.sdk.entity.DO.QrCodeGroupDO;
import com.etocrm.sdk.entity.qrcodegroup.DeleteQrGroupVO;
import com.etocrm.sdk.entity.qrcodegroup.GetQrCodeGroupVO;
import com.etocrm.sdk.entity.qrcodegroup.QrCodeGroupAddVO;
import com.etocrm.sdk.entity.qrcodegroup.QrCodeGroupEditVO;
import com.etocrm.sdk.service.QrCodeGroupService;
import com.etocrm.sdk.util.EsTable;
import com.etocrm.sdk.util.EsUtil;
import com.etocrm.sdk.util.PageUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Date 2020/10/13 11:29
 */
@Service
public class QrCodeGroupServiceImpl implements QrCodeGroupService {

    @Autowired
    private EsUtil esUtil;

    @Override
    public Result addQrGroup(QrCodeGroupAddVO qrCodeGroupAddVO) {
        //先查询出是否有重名的分组
        QrCodeGroupEditVO qrCodeGroupEditVO=new QrCodeGroupEditVO();
        BeanUtils.copyProperties(qrCodeGroupAddVO,qrCodeGroupEditVO);
        Result result=this.isReGroupName(qrCodeGroupEditVO,1);
        if(result.getCode()!=0){
            return result;
        }
        QrCodeGroupDO qrCodeGroupDO=new QrCodeGroupDO();
        BeanUtils.copyProperties(qrCodeGroupAddVO,qrCodeGroupDO);
        qrCodeGroupDO.setCreatedTime(new Date());
        String id=this.esUtil.insert(EsTable.QR_CODE_GROUP, JSON.toJSONString(qrCodeGroupDO));
        return StringUtils.isNotBlank(id)?Result.success(id):Result.error(201,"添加失败");
    }

    @Override
    public Result editQrGroup(QrCodeGroupEditVO qrCodeGroupEditVO) {
        Result result=this.isReGroupName(qrCodeGroupEditVO,2);
        if(result.getCode()!=0){
            return result;
        }
        boolean editInfo=this.esUtil.updateColum(EsTable.QR_CODE_GROUP,qrCodeGroupEditVO.getId(),JSON.toJSONString(qrCodeGroupEditVO));

        return editInfo?Result.success():Result.error(201,"修改失败");
    }

    @Override
    public Result deleteQrGroup(DeleteQrGroupVO deleteQrGroupVO) {
        this.esUtil.deleteId(EsTable.QR_CODE_GROUP,deleteQrGroupVO.getId());
        return Result.success();
    }

    @Override
    public Result getQrCodeGroup(GetQrCodeGroupVO getQrCodeGroupVO) {
        BoolQueryBuilder boolQueryBuilder=QueryBuilders.boolQuery();
        if(StringUtils.isNotBlank(getQrCodeGroupVO.getId())){
            boolQueryBuilder.must(QueryBuilders.matchQuery("_id.keyword",getQrCodeGroupVO.getId()));
        }
        if(StringUtils.isNotBlank(getQrCodeGroupVO.getGroupName())){
            boolQueryBuilder.must(QueryBuilders.matchQuery("groupName.keyword",getQrCodeGroupVO.getId()));
        }
        boolQueryBuilder.must(QueryBuilders.matchQuery("appId.keyword",getQrCodeGroupVO.getAppId()));
        JSONArray jsonArray=this.esUtil.getData(
                boolQueryBuilder,
                EsTable.QR_CODE_GROUP,
                new PageUtils(getQrCodeGroupVO.getPageSize(),getQrCodeGroupVO.getPageIndex(),null));
        return Result.success(jsonArray);
    }

    /**
     * 判断是否存在重名的分组
     *
     * */
    private Result isReGroupName(QrCodeGroupEditVO qrCodeGroupEditVO,int type){
        //先查询出是否有重名的分组
        BoolQueryBuilder  queryBuilder=QueryBuilders.boolQuery()
                .must(QueryBuilders.matchQuery("appId.keyword",qrCodeGroupEditVO.getAppId().trim()))
                .must(QueryBuilders.matchQuery("groupName.keyword",qrCodeGroupEditVO.getGroupName().trim()));
        if(type==2){
            queryBuilder.mustNot(QueryBuilders.matchQuery("_id",qrCodeGroupEditVO.getId().trim()));
        }
        JSONArray jsonArray=esUtil.getData(queryBuilder,EsTable.QR_CODE_GROUP,new PageUtils(10,1,null));
        return jsonArray!=null&&jsonArray.size()>0?Result.error(201,"二维码分组名称不可重复"):Result.success();
    }


}
