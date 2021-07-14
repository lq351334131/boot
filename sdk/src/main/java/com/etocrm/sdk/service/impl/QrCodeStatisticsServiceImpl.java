package com.etocrm.sdk.service.impl;

import com.alibaba.excel.util.DateUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.etocrm.sdk.base.Result;
import com.etocrm.sdk.entity.qrcode.QrCodeStatisticsVO;
import com.etocrm.sdk.entity.qrcode.StatisticsChartOfQrCodeVO;
import com.etocrm.sdk.entity.qrcode.StatisticsTableOfQrCodeVO;
import com.etocrm.sdk.entity.qrcodegroup.DownLoadStatisticsTableOfQrGroupVO;
import com.etocrm.sdk.entity.qrcodegroup.GetQrGroupChartVO;
import com.etocrm.sdk.entity.qrcodegroup.GetQrGroupStatisticsVO;
import com.etocrm.sdk.entity.qrcodegroup.StatisticsTableOfQrGroupVO;
import com.etocrm.sdk.service.QrCodeStatisticsService;
import com.etocrm.sdk.util.EsTable;
import com.etocrm.sdk.util.EsUtil;
import com.etocrm.sdk.util.PageUtils;
import com.etocrm.sdk.util.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.metrics.CardinalityAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.ParsedCardinality;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * @Date 2020/10/14 16:29
 */
@Service
@Slf4j
public class QrCodeStatisticsServiceImpl implements QrCodeStatisticsService {

    @Autowired
    private EsUtil esUtil;

    @Autowired
    private RestHighLevelClient client;

    @Override
    //获取二维码统计概览
    public Result getQrCodeStatistics(QrCodeStatisticsVO qrCodeStatisticsVO) {
        //a、二维码个数
        long ScanQrNum=0;
        //b、获取扫码总次数
        long VisitNum=0;
        //c、获取扫码人数
        long UserNum=0;
        //d、获取扫码新人数
        long NewVisitNum=0;
        JSONObject result=new JSONObject();
        try {
            //1、通过appId查询所有的二维码
            JSONArray pathArray=this.getAllQrCodePathArray(qrCodeStatisticsVO.getAppId());
            //2、通过二维码路径统计数据
            ScanQrNum=pathArray.size();
            if(ScanQrNum!=0){
                result=getAllCounts(pathArray,qrCodeStatisticsVO.getBeginDate().getTime(),qrCodeStatisticsVO.getEndDate().getTime());
                result.put("ScanQrNum",ScanQrNum);
                return Result.success(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage(),e);
        }

        result.put("ScanQrNum",ScanQrNum);
        result.put("VisitNum",VisitNum);
        result.put("UserNum",UserNum);
        result.put("NewVisitNum",NewVisitNum);
        return Result.success(result);
    }

    //二维码统计图显示(通过聚合查询时间9s多)
    public Result statisticsChartOfQrCode(StatisticsChartOfQrCodeVO statisticsChartOfQrCodeVO) {
        //1、获取所有二维码
        JSONArray pathArray=this.getAllQrCodePathArray(statisticsChartOfQrCodeVO.getAppId());
        if(pathArray.size()==0){
            return Result.success();
        }
        //2、获取每天的扫码次数，扫码人数，扫码新增人数
        List<Map<String,Object>>list=this.getQrCodeChart(pathArray,statisticsChartOfQrCodeVO.getBeginDate(),statisticsChartOfQrCodeVO.getEndDate());
        return Result.success(list);
    }
    //二维码统计图显示（通过遍历每天查询26s）
    public Result statisticsChartOfQrCodeNew(StatisticsChartOfQrCodeVO statisticsChartOfQrCodeVO) {
        //1、获取所有二维码
        JSONArray pathArray=this.getAllQrCodePathArray(statisticsChartOfQrCodeVO.getAppId());
        if(pathArray.size()==0){
            log.info("暂无二维码信息");
            return Result.success();
        }
        //2、获取每天的扫码次数，扫码人数，扫码新增人数
        //获取总共有多少天list
        List<Date>list=TimeUtils.findDate(statisticsChartOfQrCodeVO.getBeginDate(),statisticsChartOfQrCodeVO.getEndDate());
        if(list.size()==0){
            log.info("日期格式不正确");
            Result.success();
        }
        List<Map<String,Object>>result=new ArrayList<>();
        for (int i=0;i<list.size();i++){
            //每天开始日期
            Date begDate=TimeUtils.dateStringToDate(TimeUtils.getDateToString(list.get(i))+" 00:00:00", DateUtils.DATE_FORMAT_19);
            Date endDate=TimeUtils.dateStringToDate(TimeUtils.getDateToString(list.get(i))+" 23:59:59", DateUtils.DATE_FORMAT_19);
            JSONObject thisCount=getAllCounts(pathArray,begDate.getTime(),endDate.getTime());
            //b、获取扫码总次数
            long VisitNum=0;
            //c、获取扫码人数
            long UserNum=0;
            //d、获取扫码新人数
            long NewVisitNum=0;
            if(thisCount!=null){
                VisitNum= thisCount.getLongValue("VisitNum");
                UserNum= thisCount.getLongValue("UserNum");
                NewVisitNum= thisCount.getLongValue("NewVisitNum");
            }
            Map<String,Object>map =new HashMap<>();
            map.put("Date",TimeUtils.getDateToString(list.get(i)));
            map.put("VisitNum",VisitNum);
            map.put("UserNum",UserNum);
            map.put("NewVisitNum",NewVisitNum);
            result.add(map);
        }
        return Result.success(result);
    }
    @Override
    //获取二维码统计列表显示
    public Result StatisticsTableOfQrCode(StatisticsTableOfQrCodeVO statisticsTableOfQrCodeVO) {
        JSONArray result=new JSONArray();
        try {
            //1、先分页查询所有二维码
            BoolQueryBuilder queryBuilder= QueryBuilders.boolQuery();
            queryBuilder.must(QueryBuilders.matchQuery("appId.keyword",statisticsTableOfQrCodeVO.getAppId()));
            JSONArray jSONArray=esUtil.getData(queryBuilder
                    , EsTable.QR_CODE
                    ,new PageUtils(statisticsTableOfQrCodeVO.getPageSize()
                            ,statisticsTableOfQrCodeVO.getPageIndex()
                            ,null));
            if(jSONArray.size()==0){
                return Result.success();
            }
            //2、再遍历所有将扫码人数，扫码次数，扫码新增人数添加上
            for (int i=0;i<jSONArray.size();i++){
                JSONObject jsonObject=jSONArray.getJSONObject(i);
                String page=jsonObject.getString("page");
                JSONArray pageArray=new JSONArray();
                pageArray.add(page);
                //统计每个二维码数据
                JSONObject objectCounts=this.getAllCounts(pageArray,statisticsTableOfQrCodeVO.getBeginDate().getTime(),statisticsTableOfQrCodeVO.getEndDate().getTime());
                jsonObject.put("VisitNum",objectCounts.get("VisitNum"));
                jsonObject.put("UserNum",objectCounts.get("UserNum"));
                jsonObject.put("NewVisitNum",objectCounts.get("NewVisitNum"));
                //TODO 获取二维码组的信息
                result.add(jsonObject);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.success(result);
    }

    @Override
    //获取二维码组分页查询
    public Result statisticsTableOfQrGroup(StatisticsTableOfQrGroupVO statisticsTableOfQrGroupVO) {
        //获取所有二维码组分页信息
        BoolQueryBuilder queryBuilder= QueryBuilders.boolQuery();
        queryBuilder.must(QueryBuilders.matchQuery("appId.keyword",statisticsTableOfQrGroupVO.getAppId()));
        JSONArray jSONArray=esUtil.getData(queryBuilder
                , EsTable.QR_CODE_GROUP
                ,new PageUtils(statisticsTableOfQrGroupVO.getPageSize()
                        ,statisticsTableOfQrGroupVO.getPageIndex()
                        ,null));
        if(jSONArray.size()==0){
            return Result.success();
        }
        List<Map<String,Object>>result=new ArrayList<>();
        for (int i=0;i<jSONArray.size();i++){
            JSONObject group=JSONObject.parseObject(jSONArray.get(i).toString());
            //b、获取扫码总次数
            long VisitNum=0;
            //c、获取扫码人数
            long UserNum=0;
            //d、获取扫码新人数
            long NewVisitNum=0;
            //查询组内所有二维码
            BoolQueryBuilder queryBuilder1= QueryBuilders.boolQuery();
            queryBuilder1.must(QueryBuilders.matchQuery("appId.keyword",statisticsTableOfQrGroupVO.getAppId()));
            queryBuilder1.must(QueryBuilders.matchQuery("qrGroupId.keyword",group.getString("id")));
            JSONArray jSONArray1=esUtil.getData(queryBuilder1
                    , EsTable.QR_CODE
                    ,null);
            if(jSONArray1.size()==0){
                group.put("VisitNum",VisitNum);
                group.put("UserNum",UserNum);
                group.put("NewVisitNum",NewVisitNum);
            }else{
                JSONArray pathArray=new JSONArray();
                for (int j=0;i<jSONArray1.size();i++){
                    JSONObject qrObject=jSONArray1.getJSONObject(i);
                    pathArray.add(qrObject.getString("page"));
                }
                if(pathArray.size()==0){
                    group.put("VisitNum",VisitNum);
                    group.put("UserNum",UserNum);
                    group.put("NewVisitNum",NewVisitNum);
                }
                JSONObject totalCount=this.getAllCounts(pathArray,
                        statisticsTableOfQrGroupVO.getBeginDate().getTime(),
                        statisticsTableOfQrGroupVO.getEndDate().getTime());
                group.put("VisitNum",totalCount.get("VisitNum"));
                group.put("UserNum",totalCount.get("UserNum"));
                group.put("NewVisitNum",totalCount.get("NewVisitNum"));
            }
            //根据二维码统计扫码总数，扫码人数，新增扫码人数
            result.add(group);
        }
        return Result.success(result);
    }

    @Override
    //获取二维码组统计概览
    public Result getQrGroupStatistics(GetQrGroupStatisticsVO getQrGroupStatisticsVO) {
        //a、二维码组个数
        long ScanQrGroupNum=0;
        //b、获取扫码总次数
        long VisitNum=0;
        //c、获取扫码人数
        long UserNum=0;
        //d、获取扫码新人数
        long NewVisitNum=0;
        JSONObject result=new JSONObject();
        try {
            //1、通过appId查询所有二维码组
            JSONObject pathObject=this.getQrCodeByQrGroup(getQrGroupStatisticsVO.getAppId());
            JSONArray pathArray=JSONArray.parseArray(pathObject.getString("pathArray"));
            if(pathArray.size()==0){
                log.info("二维码为空");
                result.put("ScanQrGroupNum",ScanQrGroupNum);
                result.put("VisitNum",VisitNum);
                result.put("UserNum",UserNum);
                result.put("NewVisitNum",NewVisitNum);
                return Result.success(result);
            }
            //2、通过二维码路径统计数据
            result=getAllCounts(pathArray,getQrGroupStatisticsVO.getBeginDate().getTime(),getQrGroupStatisticsVO.getEndDate().getTime());
            result.put("ScanQrGroupNum",pathObject.get("ScanQrGroupNum"));
            return Result.success(result);

        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage(),e);
        }
        result.put("ScanQrGroupNum",ScanQrGroupNum);
        result.put("VisitNum",VisitNum);
        result.put("UserNum",UserNum);
        result.put("NewVisitNum",NewVisitNum);
        return Result.success(result);
    }

    @Override
    public Result getQrGroupChart(GetQrGroupChartVO getQrGroupChartVO) {
        //通过二维码组获取所有二维码
        JSONObject pathObject=this.getQrCodeByQrGroup(getQrGroupChartVO.getAppId());
        //获取所有二维码
        JSONArray pathArray=JSONArray.parseArray(pathObject.getString("pathArray"));
        //获取时间范围内的数据
        return Result.success(this.getQrCodeChart(pathArray,getQrGroupChartVO.getBeginDate(),getQrGroupChartVO.getEndDate()));
    }

    public List<DownLoadStatisticsTableOfQrGroupVO> DownLoadStatisticsTableOfQrGroup(StatisticsTableOfQrGroupVO statisticsTableOfQrGroupVO) {
        //获取所有二维码组分页信息
        List<DownLoadStatisticsTableOfQrGroupVO>list =new ArrayList<>();
        BoolQueryBuilder queryBuilder= QueryBuilders.boolQuery();
        queryBuilder.must(QueryBuilders.matchQuery("appId.keyword",statisticsTableOfQrGroupVO.getAppId()));
        JSONArray jSONArray=esUtil.getData(queryBuilder
                , EsTable.QR_CODE_GROUP
                ,null);
        if(jSONArray.size()==0){
            return list;
        }
        //List<Map<String,Object>>result=new ArrayList<>();
        JSONArray result=new JSONArray();
        for (int i=0;i<jSONArray.size();i++){
            JSONObject group=JSONObject.parseObject(jSONArray.get(i).toString());
            //b、获取扫码总次数
            long VisitNum=0;
            //c、获取扫码人数
            long UserNum=0;
            //d、获取扫码新人数
            long NewVisitNum=0;
            //查询组内所有二维码
            BoolQueryBuilder queryBuilder1= QueryBuilders.boolQuery();
            queryBuilder1.must(QueryBuilders.matchQuery("appId.keyword",statisticsTableOfQrGroupVO.getAppId()));
            queryBuilder1.must(QueryBuilders.matchQuery("qrGroupId.keyword",group.getString("id")));
            JSONArray jSONArray1=esUtil.getData(queryBuilder1
                    , EsTable.QR_CODE
                    ,null);
            if(jSONArray1.size()==0){
                group.put("visitNum",VisitNum);
                group.put("userNum",UserNum);
                group.put("newVisitNum",NewVisitNum);
            }else{
                JSONArray pathArray=new JSONArray();
                for (int j=0;i<jSONArray1.size();i++){
                    JSONObject qrObject=jSONArray1.getJSONObject(i);
                    pathArray.add(qrObject.getString("page"));
                }
                if(pathArray.size()==0){
                    group.put("visitNum",VisitNum);
                    group.put("userNum",UserNum);
                    group.put("newVisitNum",NewVisitNum);
                }
                JSONObject totalCount=this.getAllCounts(pathArray,
                        statisticsTableOfQrGroupVO.getBeginDate().getTime(),
                        statisticsTableOfQrGroupVO.getEndDate().getTime());
                group.put("visitNum",totalCount.get("VisitNum"));
                group.put("userNum",totalCount.get("UserNum"));
                group.put("newVisitNum",totalCount.get("NewVisitNum"));
            }
            //根据二维码统计扫码总数，扫码人数，新增扫码人数
            result.add(group);
        }
        for(int i=0;i<result.size();i++){
            DownLoadStatisticsTableOfQrGroupVO downLoadStatisticsTableOfQrGroupVO=result.getObject(i,DownLoadStatisticsTableOfQrGroupVO.class);
            if(downLoadStatisticsTableOfQrGroupVO.getCreatedTime()!=null){
                long time=Long.decode(downLoadStatisticsTableOfQrGroupVO.getCreatedTime());
                downLoadStatisticsTableOfQrGroupVO.setCreatedTime(TimeUtils.getString(time));
            }
            list.add(downLoadStatisticsTableOfQrGroupVO);
        }
        return list;
    }

    @Override
    public Result DownLoadStatisticsTableOfQrCode(StatisticsTableOfQrCodeVO statisticsTableOfQrCodeVO) {
        return null;
    }

    /**
     * 获取时间段范围二维码统计内折线图
     *
     **/
    private List<Map<String,Object>> getQrCodeChart(JSONArray pathArray,Date begDate,Date endDate){
        //2、获取每天的扫码次数，扫码人数，扫码新增人数
        BoolQueryBuilder queryBuilder= QueryBuilders.boolQuery();
        queryBuilder.must(QueryBuilders.termsQuery("p.keyword",pathArray));
        queryBuilder.must(QueryBuilders.rangeQuery("t")
                .from(begDate.getTime())
                .to(endDate.getTime()));
        SearchSourceBuilder searchSourceBuilder=new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilder);
        searchSourceBuilder.size(0);
        //去重统计
        CardinalityAggregationBuilder cardinality= AggregationBuilders.cardinality("agg_uu").field("uu.keyword");
        //日期分组
        DateHistogramAggregationBuilder dateHistogram= AggregationBuilders
                .dateHistogram("groupByDate")
                .field("t")
                .fixedInterval(DateHistogramInterval.DAY)
                .format("yyyy-MM-dd")
                .subAggregation(cardinality);
        searchSourceBuilder.aggregation(dateHistogram);
        searchSourceBuilder.sort("t", SortOrder.ASC);
        SearchRequest searchRequest=new SearchRequest();
        searchRequest.indices(EsTable.INDEX);
        searchRequest.source(searchSourceBuilder);
        List<Map<String,Object>>result=new ArrayList<>();
        try {
            SearchResponse searchResponse=client.search(searchRequest,RequestOptions.DEFAULT);
            Aggregations aggregations=searchResponse.getAggregations();
            Aggregation aggregation=aggregations.get("groupByDate");
            List<? extends Histogram.Bucket> buckets = ((Histogram) aggregation).getBuckets();

            for (Histogram.Bucket bucket:buckets) {
                Map<String,Object>map=new HashMap<>();
                //获取时间戳
                String key=bucket.getKey().toString();
                Date date=TimeUtils.getstrDate(key);
                String strDate=TimeUtils.getDateToString(date);
                map.put("DateTime",strDate);
                //b、获取扫码总次数
                long VisitNum=bucket.getDocCount();
                map.put("VisitNum",VisitNum);
                //c、获取扫码人数
                long UserNum=((ParsedCardinality)bucket.getAggregations().get("agg_uu")).getValue();
                map.put("UserNum",UserNum);
                //d、获取扫码新人数
                BoolQueryBuilder userQueryBuilder= QueryBuilders.boolQuery();
                queryBuilder.must(QueryBuilders.termsQuery("p.keyword",pathArray));
                queryBuilder.must(QueryBuilders.rangeQuery("t")
                        .from(TimeUtils.getstrDate(strDate+" 00:00:00").getTime())
                        .to(TimeUtils.getstrDate(strDate+" 23:59:59").getTime()));
                long NewVisitNum=this.getCount(queryBuilder,3);
                map.put("NewVisitNum",NewVisitNum);
                result.add(map);
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage(),e);
        }
        return result;
    }
    /**
     * 通过二维码组获取获取所有二维码
     * */
    private JSONObject getQrCodeByQrGroup(String appId){
        //1、通过appId查询所有二维码组
        //获取所有二维码组分页信息
        JSONObject result=new JSONObject();
        JSONArray pathArray=new JSONArray();
        BoolQueryBuilder queryBuilder= QueryBuilders.boolQuery();
        queryBuilder.must(QueryBuilders.matchQuery("appId.keyword",appId));
        JSONArray jSONArray=esUtil.getData(queryBuilder
                , EsTable.QR_CODE_GROUP
                ,null);
        //通过二维码组获取所有二维码
        for(int i=0;i<jSONArray.size();i++){
            //获取通过二维码组所有二维码分页信息
            JSONObject groups=JSONObject.parseObject(jSONArray.get(i).toString());
            BoolQueryBuilder queryBuilder1= QueryBuilders.boolQuery();
            queryBuilder1.must(QueryBuilders.matchQuery("qrGroupId.keyword",groups.getString("id")));
            JSONArray qrCodes=esUtil.getData(queryBuilder
                    , EsTable.QR_CODE
                    ,null);
            if(qrCodes.size()==0){
                continue;
            }else{
                for (int j=1;j<qrCodes.size();j++){
                    JSONObject qrCode=JSONObject.parseObject(qrCodes.get(j).toString());
                    pathArray.add(qrCode.getString("page"));
                }
            }
        }
        result.put("ScanQrGroupNum",jSONArray.size());
        result.put("pathArray",pathArray);
        return result;
    }
    /**
     * 获取扫码人数，扫码次数，扫码新增人数
     * */
    private JSONObject getAllCounts(JSONArray pathArray,long fromDate,long toDate){
        JSONObject jsonObject=new JSONObject();
        //b、获取扫码总次数
        long VisitNum=0;
        //c、获取扫码人数
        long UserNum=0;
        //d、获取扫码新人数
        long NewVisitNum=0;
        BoolQueryBuilder queryBuilder= QueryBuilders.boolQuery();
        queryBuilder.must(QueryBuilders.termsQuery("p.keyword",pathArray));
        queryBuilder.must(QueryBuilders.rangeQuery("t")
                .from(fromDate)
                .to(toDate));
        VisitNum=this.getCount(queryBuilder,1);
        UserNum=this.getCount(queryBuilder,2);
        NewVisitNum=this.getCount(queryBuilder,3);
        jsonObject.put("VisitNum",VisitNum);
        jsonObject.put("UserNum",UserNum);
        jsonObject.put("NewVisitNum",NewVisitNum);
        return jsonObject;
    }
    /**
     * 获取所有二维码
     *
     * */
    private JSONArray getAllQrCodePathArray(String appId){
        JSONArray pathArray=new JSONArray();
        try {
            BoolQueryBuilder boolQueryBuilder= QueryBuilders.boolQuery()
                    .must(QueryBuilders.matchQuery("appId.keyword",appId));
            JSONArray jSONArray=esUtil.getData(boolQueryBuilder, EsTable.QR_CODE,null);
            if(jSONArray.size()>0){
                for (int i=0;i<jSONArray.size();i++){
                    JSONObject obj=JSONObject.parseObject(jSONArray.get(i).toString());
                    pathArray.add(obj.getString("page"));
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return pathArray;
    }
    /**
     * 查询数据总条数
     * type : 1统计扫码总次数，2统计扫码总人数，3统计扫码新人数
     * */
    private long getCount(BoolQueryBuilder queryBuilder,int type){
        long count=0;
        try {
            if(type==3){
                //新用户数-首次访问小程序页面的用户数，同一用户多次访问不重复计,isfirstday=1，app.launch
                queryBuilder.must(QueryBuilders.matchQuery("isfirstday",true));
                queryBuilder.must(QueryBuilders.matchQuery("tv.keyword","app"));
                queryBuilder.must(QueryBuilders.matchQuery("tl.keyword","launch"));
            }
            SearchSourceBuilder searchSourceBuilder=new SearchSourceBuilder();
            searchSourceBuilder.query(queryBuilder);
            if(type==2){
                //统计扫码总人数去重查询
                CardinalityAggregationBuilder cardinality= AggregationBuilders.cardinality("agg_uu").field("uu.keyword");
                searchSourceBuilder.aggregation(cardinality);
                SearchRequest searchRequest=new SearchRequest();
                searchRequest.indices(EsTable.INDEX);
                searchRequest.source(searchSourceBuilder);
                SearchResponse searchResponse =client.search(searchRequest,RequestOptions.DEFAULT);
                Aggregations aggregations=searchResponse.getAggregations();
                Aggregation aggregation=aggregations.get("agg_uu");
                count=((ParsedCardinality)aggregation).getValue();
            }else{
                CountRequest countRequest=new CountRequest();
                countRequest.indices(EsTable.INDEX);
                countRequest.source(searchSourceBuilder);
                CountResponse countResponse=client.count(countRequest, RequestOptions.DEFAULT);
                count=countResponse.getCount();
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage(),e);
        }
        return count;
    }
}
