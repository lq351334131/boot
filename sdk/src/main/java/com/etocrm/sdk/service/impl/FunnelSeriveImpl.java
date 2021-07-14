package com.etocrm.sdk.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.etocrm.sdk.base.ResponseCode;
import com.etocrm.sdk.base.Result;
import com.etocrm.sdk.entity.funnel.*;
import com.etocrm.sdk.service.FunnelSerive;
import com.etocrm.sdk.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FunnelSeriveImpl implements FunnelSerive {

    @Autowired
    private EsUtil esUtil;

    @Autowired
    private RestHighLevelClient client;

    @Autowired
    private OvalUtils ovalUtils;

    @Override
    public Result getFunnelAnalysisList(FunnelVO vo) {
        if (ovalUtils.validatorRequestParam(vo).size() > 0) return Result.error(ResponseCode.PARAMETERS_NULL);
        PageUtils pageUtils = new PageUtils(vo.getPageSize(), vo.getPageIndex(), null);
        List<FunnelRepVO> funnelList = getFunnelList(vo, pageUtils);
        for(FunnelRepVO funnelRepVO:funnelList){
            String id=funnelRepVO.getId();
            BoolQueryBuilder mustQuery = QueryBuilders.boolQuery();
            if (StringUtils.isNotBlank(vo.getFunnelName())) {
                MatchQueryBuilder appKeyBuilder = QueryBuilders.matchQuery("funnelId.keyword", id);
                mustQuery.must(appKeyBuilder);
            }
            JSONArray data1 = esUtil.getData(mustQuery, EsTable.STEP, null);
            int size = data1.size();
            if(size>=2){
                String stepName = data1.getJSONObject(0).getString("stepName");
                funnelRepVO.setBeginStepName(stepName);
                String endstepName = data1.getJSONObject(data1.size()-1).getString("stepName");
                funnelRepVO.setEndStepName(endstepName);
                //事件id或者页面id
                String peId = data1.getJSONObject(0).getString("peId");
                Map<String, Object> stringObjectMap = esUtil.selectOne(EsTable.PAGE, peId);
                if(stringObjectMap==null||stringObjectMap.size()==0){
                    continue;
                }
                String visitPath = (String)stringObjectMap.get("visitPath");
                BoolQueryBuilder first = QueryBuilders.boolQuery();
                first.must(QueryBuilders.matchQuery("p.keyword",visitPath));
                Long begCount = esUtil.groupBy(EsTable.PAGE, "uu.keyword", first);
                BoolQueryBuilder end = QueryBuilders.boolQuery();
                String endPeId = data1.getJSONObject(data1.size()-1).getString("peId");
                Map<String, Object> endObjectMap = esUtil.selectOne(EsTable.PAGE, endPeId);
                if(endObjectMap==null||endObjectMap.size()==0){
                    continue;
                }
                String endVistPath = (String)endObjectMap.get("visitPath");
                end.must(QueryBuilders.matchQuery("p.keyword",endVistPath));
                Long endCount= esUtil.groupBy(EsTable.PAGE, "uu.keyword", end);
                if(begCount>0&&endCount>0){
                    String percent = DecimalFormatUtils.getPercent(endCount.intValue(), begCount.intValue());
                    funnelRepVO.setConversionRate(percent);
                }else{
                    funnelRepVO.setConversionRate("0.00%");
                }
            }else if(size>0&&size<2){
                String stepName = data1.getJSONObject(0).getString("stepName");
                funnelRepVO.setBeginStepName(stepName);
            }

        }

        Map<String, Object> result = new HashMap();
        result.put("totalNum", pageUtils.getTotalNum());
        result.put("count", pageUtils.getCount());
        result.put("data", funnelList);
        return Result.success(result);
    }

    @Override
    public Result addFunnel(FunnelAddVO vo) {
        if (ovalUtils.validatorRequestParam(vo).size()>0)return Result.error(ResponseCode.PARAMETERS_NULL);
        FunnelAddRepVO esVo=new FunnelAddRepVO();
        BeanUtils.copyProperties(vo,esVo);
        esVo.setCreateTime(new Date());
        esVo.setK(vo.getAppKey());
        String id = esUtil.insert(EsTable.FUNNEL, JSON.toJSONString(esVo));
        if (StringUtils.isBlank(id)) return  Result.error(ResponseCode.ES_EXCEPTION);
        List<FunnelSetpsVO> funnelSetps = vo.getFunnelSetps();
        List<StepRepVO> stepList=new ArrayList<>();
        for(FunnelSetpsVO funnelSetpsVO:funnelSetps){
            StepRepVO stepRepVO=new StepRepVO();
            BeanUtils.copyProperties(funnelSetpsVO,stepRepVO);
            stepRepVO.setCreateTime(new Date());
            stepRepVO.setFunnelId(id);
            stepList.add(stepRepVO);
        }
        int i = insertBatch(EsTable.STEP, stepList);
        if(i==0){
            log.info("添加步骤失败");
            return  Result.error(ResponseCode.ES_EXCEPTION);
        }
        return Result.success(ResponseCode.SUCCESS);
    }

    @Override
    public Result delFunnel(String id) {
        if (StringUtils.isBlank(id))
            return Result.error(ResponseCode.PARAMETERS_NULL);
        esUtil.deleteId(EsTable.FUNNEL, id);
        return  Result.success(ResponseCode.SUCCESS);
    }

    @Override
    public Result editFunnel(FunnelEditVO vo) {
        if (ovalUtils.validatorRequestParam(vo).size()>0)
            return Result.error(ResponseCode.PARAMETERS_NULL);
        //指定字段过滤，修改的时候，id不会在_source中出现
        SimplePropertyPreFilter spf = new SimplePropertyPreFilter();
        spf.getExcludes().add("id");
        spf.getExcludes().add("funnelSetps");
        String json= JSONObject.toJSONString(vo, spf);
        boolean flag = esUtil.updateColum(EsTable.FUNNEL, vo.getId(), json);
        if (!flag) return  Result.error(ResponseCode.ES_EXCEPTION);
        List<FunnelSetpsIdVO> funnelSetps = vo.getFunnelSetps();
        //删除原来id
        deletId(funnelSetps,vo);
        for(FunnelSetpsIdVO funnelSetpsIdVO:funnelSetps){
            funnelSetpsIdVO.setFunnelId(vo.getId());
            SimplePropertyPreFilter step = new SimplePropertyPreFilter();
            step.getExcludes().add("setpId");
            String stepjson=JSONObject.toJSONString(funnelSetpsIdVO,step);
            if(StringUtils.isNotBlank(funnelSetpsIdVO.getSetpId())){
                esUtil.updateColum(EsTable.STEP,funnelSetpsIdVO.getSetpId(),stepjson);
            }else{
                esUtil.insert(EsTable.STEP,stepjson);
            }
        }
        return  Result.success(ResponseCode.SUCCESS);
    }

    @Override
    public Result getSingleFunnel(FunnelOneVO vo) {
        if (ovalUtils.validatorRequestParam(vo).size()>0)
            return Result.error(ResponseCode.PARAMETERS_NULL);
        Map<String, Object> map = esUtil.selectOne(EsTable.FUNNEL, vo.getId());
        if(map==null||map.size()==0){
            return  Result.success();
        }
        FunnelEditVO funnelEditVO=new FunnelEditVO();
        BoolQueryBuilder mustQuery = QueryBuilders.boolQuery();
        MatchQueryBuilder appKeyBuilder = QueryBuilders.matchQuery("funnelId.keyword", vo.getId());
        mustQuery.must(appKeyBuilder);
        JSONArray data = esUtil.getData(mustQuery, EsTable.STEP, null);
        List<FunnelSetpsIdVO> list=new ArrayList<>();
        funnelEditVO.setFunnelName((String)map.get("funnelName"));
        funnelEditVO.setId((String)map.get("id"));
        funnelEditVO.setTypeId((String)map.get("typeId"));


        for (int i = 0; i < data.size(); i++) {
            FunnelSetpsIdVO funnelRepVO = data.getObject(i, FunnelSetpsIdVO.class);
            String id = data.getJSONObject(i).getString("id");
            funnelRepVO.setSetpId(id);
            list.add(funnelRepVO);
        }
        funnelEditVO.setFunnelSetps(list);
        return  Result.success(funnelEditVO);
    }

    @Override
    public List<FunnelRepExcelVO> downloadFunnelAnalysisList(DownLoadFunnelExcel vo) {
        if(ovalUtils.validatorRequestParam(vo).size()>0)return Collections.emptyList();
        List<FunnelRepExcelVO> funnelList = getFunnelDownloadList(vo);
        return funnelList;
    }

    private List<FunnelRepVO> getFunnelList(FunnelVO vo,PageUtils pageUtils){
        BoolQueryBuilder mustQuery = QueryBuilders.boolQuery();
        if (StringUtils.isNotBlank(vo.getFunnelName())) {
            WildcardQueryBuilder wildcardQueryBuilder = QueryBuilders.wildcardQuery("funnelName.keyword", "*" + vo.getFunnelName() + "*");
            mustQuery.must(wildcardQueryBuilder);
        }
        mustQuery.must(QueryBuilders.matchQuery("k.keyword",vo.getAppKey()));
        JSONArray data = esUtil.getData(mustQuery, EsTable.FUNNEL, pageUtils);
        List<FunnelRepVO> list=new ArrayList();
        for (int i = 0; i < data.size(); i++) {
            FunnelRepVO funnelRepVO = data.getObject(i, FunnelRepVO.class);
            JSONObject j = data.getJSONObject(i);
            Long createTime = j.getLong("createTime");
            funnelRepVO.setCreateTime(TimeUtils.getString(createTime));
            list.add(funnelRepVO);
        }
        return list;
    }

    private List<FunnelRepExcelVO> getFunnelDownloadList(DownLoadFunnelExcel vo ){
        BoolQueryBuilder mustQuery = QueryBuilders.boolQuery();
        if (StringUtils.isNotBlank(vo.getFunnelName())) {
            WildcardQueryBuilder wildcardQueryBuilder = QueryBuilders.wildcardQuery("funnelName.keyword", "*" + vo.getFunnelName() + "*");
            mustQuery.must(wildcardQueryBuilder);
        }
        mustQuery.must(QueryBuilders.matchQuery("k.keyword",vo.getAppKey()));
        JSONArray data = esUtil.getData(mustQuery, EsTable.FUNNEL, null);
        List<FunnelRepExcelVO> list=new ArrayList();
        for (int i = 0; i < data.size(); i++) {
            FunnelRepExcelVO funnelRepVO = data.getObject(i, FunnelRepExcelVO.class);
            JSONObject j = data.getJSONObject(i);
            Long createTime = j.getLong("createTime");
            funnelRepVO.setCreateTime(TimeUtils.getString(createTime));
            list.add(funnelRepVO);
        }
        return list;
    }

    public int insertBatch(String index,List<StepRepVO> list) {
        BulkRequest request = new BulkRequest();
        int i=0;
        try {
            list.forEach(item -> {
                IndexRequest indexRequest = new IndexRequest(index);
                indexRequest.source(JSONObject.toJSONString(item),XContentType.JSON);
                request.add(indexRequest);
            });
            BulkResponse bulk = client.bulk(request, RequestOptions.DEFAULT);
            i = bulk.getItems().length;
        } catch (Exception e) {
           log.error("{}",e);
        }
        return i;
    }

    private List<String> getId(QueryBuilder queryBuilder, String index){
        List<String> list=new ArrayList<>();
        try{
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(queryBuilder);
            SearchRequest searchRequest = new SearchRequest(index);
            searchSourceBuilder.size(1000);
            searchRequest.source(searchSourceBuilder);
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = searchResponse.getHits();
            SearchHit[] hits1 = hits.getHits();
            for(SearchHit hit:hits1){
                list.add(hit.getId());
            }
        }catch (Exception e){
            log.error("{}",e.getMessage());
        }
        return list;
    }

    private void  deletId(List<FunnelSetpsIdVO>funnelSetps,FunnelEditVO vo){
        BoolQueryBuilder mustQuery = QueryBuilders.boolQuery();
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("funnelId.keyword", vo.getId());
        mustQuery.must(matchQueryBuilder);
        //原来与当前id交集，不包含删除
        List<String> id = getId(matchQueryBuilder, EsTable.STEP);
        //当前id
        List<String> stringList = funnelSetps.stream().map(FunnelSetpsIdVO::getSetpId).collect(Collectors.toList());
        //交集
        List<String> intersection  = id.stream().filter(item -> stringList.contains(item)).collect(Collectors.toList());
        if(intersection.size()>0){
            id.removeAll(intersection);
        }
        for(String string:id){
            log.info("删除id"+id);
            esUtil.deleteId(EsTable.STEP,string);
        }
    }




}
