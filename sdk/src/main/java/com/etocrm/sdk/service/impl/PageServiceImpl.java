package com.etocrm.sdk.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.etocrm.sdk.base.ResponseCode;
import com.etocrm.sdk.base.Result;
import com.etocrm.sdk.entity.VO.*;
import com.etocrm.sdk.service.PageService;
import com.etocrm.sdk.util.*;
import lombok.extern.slf4j.Slf4j;
import net.sf.oval.ConstraintViolation;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.*;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.*;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.*;

@Service
@Slf4j
public class PageServiceImpl  implements PageService {

    @Autowired
    private EsUtil esUtil;



    @Autowired
    private RestHighLevelClient client;

    @Autowired
    private OvalUtils ovalUtils;

    @Override
    public Result getPageNameList() {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        SearchRequest searchRequest = new SearchRequest(EsTable.INDEX);
        JSONArray jsonArray=new JSONArray();
        try{
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = searchResponse.getHits();
            for (SearchHit hit : hits.getHits()) {
                JSONObject obj = JSONObject.parseObject(hit.getSourceAsString());
                String p=obj.getString("p");
                String moduleName=obj.getString("moduleName");
                String pathTypeId=obj.getString("pathTypeId");
                JSONObject  jsonObject=new JSONObject();
                jsonObject.put("visitPath",p);
                jsonObject.put("id",hit.getId());
                jsonObject.put("moduleName",moduleName);
                jsonObject.put("pathTypeId",pathTypeId);
                jsonArray.add(jsonObject);
            }
        }catch (Exception  e){
            log.error(e.getMessage(), e);
            return  Result.error(ResponseCode.ES_EXCEPTION);
        }
        return  Result.success(jsonArray);
    }

    @Override
    public Result getHomePage(PageAccessVo vo) {
        if (ovalUtils.validatorRequestParam(vo).size()>0) return Result.error(ResponseCode.PARAMETERS_NULL);
        long begTime =vo.getBeginDate().getTime();
        long endTime = vo.getEndDate().getTime();
        QueryBuilder queryBuilder = QueryBuilders.rangeQuery("t").gte(begTime).lte(endTime);
        long visitNum = esUtil.count(EsTable.INDEX, queryBuilder);
        if (visitNum < 0) return Result.error(ResponseCode.ES_EXCEPTION);
        long personNum = esUtil.groupBy(EsTable.INDEX, "uu.keyword", queryBuilder);
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("m_entrypage", 1);
        BoolQueryBuilder mustQuery = QueryBuilders.boolQuery();
        mustQuery.must(queryBuilder);
        mustQuery.must(matchQueryBuilder);
        long entryNum = esUtil.count(EsTable.INDEX, mustQuery);
        if (entryNum < 0) return Result.error(ResponseCode.ES_EXCEPTION);
        MatchQueryBuilder m_openapp = QueryBuilders.matchQuery("m_openapp", 1);
        BoolQueryBuilder avgStopTimeQuery = QueryBuilders.boolQuery();
        avgStopTimeQuery.must(queryBuilder);
        avgStopTimeQuery.must(m_openapp);
        long mppenapp = esUtil.count(EsTable.INDEX, avgStopTimeQuery);
        if (mppenapp < 0) return Result.error(ResponseCode.ES_EXCEPTION);
        long avgStopTime = esUtil.sum(EsTable.INDEX, "m_apptime", avgStopTimeQuery);
        if (avgStopTime < 0) return Result.error(ResponseCode.ES_EXCEPTION);
        String time = "00:00:00";
        if (mppenapp > 0) time = TimeUtils.getGapTime(avgStopTime / mppenapp);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("visitNum", visitNum);
        jsonObject.put("personNum", personNum);
        jsonObject.put("entryNum", entryNum);
        jsonObject.put("avgStopTime", time);
        return Result.success(jsonObject);
    }

    @Override
    public Result getPageVisitHabitFrequency(PageVO vo) {
        List<ConstraintViolation> constraintViolations = ovalUtils.validatorRequestParam(vo);
        if (constraintViolations.size() > 0) return Result.error(ResponseCode.PARAMETERS_NULL);
        BoolQueryBuilder mustQuery = QueryBuilders.boolQuery();
        MatchQueryBuilder eventNameBuilder = QueryBuilders.matchQuery("tl.keyword", "show");
        mustQuery.must(eventNameBuilder);
        mustQuery.must(QueryBuilders.matchQuery("k.keyword", vo.getAppKey()));
        MatchQueryBuilder tvNameBuilder = QueryBuilders.matchQuery("tv.keyword", "page");
        mustQuery.must(tvNameBuilder);
        long begTime = vo.getBeginDate().getTime();
        long endTime = vo.getEndDate().getTime();
        ;//TimeUtils.getstrDate(endDate).getTime();
        mustQuery.must(QueryBuilders.rangeQuery("t").gte(begTime).lte(endTime));
        List<PageDepthVO> pageVisitHabitFrequency = getPageVisitHabitFrequency(mustQuery);
        return  Result.success(pageVisitHabitFrequency);
    }

    @Override
    public Result getPageList(PageListVO vo) {
        if (ovalUtils.validatorRequestParam(vo).size() > 0) return Result.error(ResponseCode.PARAMETERS_NULL);
        BoolQueryBuilder mustQuery = QueryBuilders.boolQuery();
        if (StringUtils.isNotBlank(vo.getPageName())) {
            mustQuery.must(QueryBuilders.matchQuery("pageName.keyword", vo.getPageName()));
        }
        if (StringUtils.isNotBlank(vo.getPagePath())) {
            mustQuery.must(QueryBuilders.matchQuery("pagePath.keyword", vo.getPagePath()));
        }
        if (StringUtils.isNotBlank(vo.getModuleName())) {
            mustQuery.must(QueryBuilders.matchQuery("moduleName.keyword", vo.getModuleName()));
        }
        if (vo.getPathTypeId() != null ) {
            mustQuery.must(QueryBuilders.matchQuery("pathTypeId.keyword", vo.getPathTypeId()));
        }

        PageUtils pageUtils = new PageUtils(vo.getPageSize(), vo.getPageIndex(), null);
        JSONArray data = esUtil.getData(mustQuery, EsTable.PAGE, pageUtils);
        List<PageListResVO> list = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            PageListResVO pageList = data.getObject(i, PageListResVO.class);
            list.add(pageList);
        }
        Map<String, Object> result = new HashMap();
        result.put("totalNum", pageUtils.getTotalNum());
        result.put("count", pageUtils.getCount());
        result.put("data", list);
        return Result.success(result);
    }

    @Override
    public List<PageListDownloadVO> downloadPageList(PageDownloadVO vo) {
        if (ovalUtils.validatorRequestParam(vo).size() > 0) return new ArrayList<PageListDownloadVO>();
        BoolQueryBuilder mustQuery = QueryBuilders.boolQuery();
        if(StringUtils.isNotBlank(vo.getPagePath())) {
            MatchQueryBuilder eventNameBuilder = QueryBuilders.matchQuery("pagePath", vo.getPagePath());
            mustQuery.must(eventNameBuilder);
        }
        if(StringUtils.isNotBlank(vo.getPagePath())) {
            MatchQueryBuilder eventNameBuilder = QueryBuilders.matchQuery("moduleName", vo.getModuleName());
            mustQuery.must(eventNameBuilder);
        }
        if(vo.getPathTypeId()!=null) {
            MatchQueryBuilder eventNameBuilder = QueryBuilders.matchQuery("pathTypeId", vo.getPathTypeId());
            mustQuery.must(eventNameBuilder);
        }

        JSONArray data = esUtil.getData(mustQuery, EsTable.PAGE, null);
        List<PageListDownloadVO> list = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            JSONObject o= data.getJSONObject(i);
            PageListDownloadVO pageChannelEditVo = JSONObject.parseObject(data.getJSONObject(i).toJSONString(), PageListDownloadVO.class);
            list.add(pageChannelEditVo);
        }
        return list;

    }

    @Override
    public Result getSinglePageParameter(String id) {
        if (StringUtils.isBlank(id))
            return Result.error(ResponseCode.PARAMETERS_NULL);
        Map<String, Object> map = esUtil.selectOne(EsTable.PAGE, id);
        return Result.success(map);
    }

    @Override
    public Result editPageParameter(PageEditVO vo) {
        if (StringUtils.isBlank(vo.getId()) || StringUtils.isBlank(vo.getModuleName()) || vo.getPathTypeId() == null || StringUtils.isBlank(vo.getPathName()))
            return Result.error(ResponseCode.PARAMETERS_NULL);
        String id = vo.getId();
        boolean flag = esUtil.updateColum(EsTable.PAGE, id, JSONObject.toJSONString(vo));
        if (!flag) return Result.error(ResponseCode.ES_EXCEPTION);
        return Result.success(ResponseCode.SUCCESS);
    }

    @Override
    public Result getPageParameterList(PageChannelVO vo) {
        if (ovalUtils.validatorRequestParam(vo).size() > 0) return Result.error(ResponseCode.PARAMETERS_NULL);
        BoolQueryBuilder mustQuery = QueryBuilders.boolQuery();
        if (StringUtils.isNotBlank(vo.getName())) {
            mustQuery.must(QueryBuilders.matchQuery("name.keyword", vo.getName()));
        }
        mustQuery.must(QueryBuilders.matchQuery("pathId.keyword", vo.getPathId()));
        PageUtils pageUtils = new PageUtils(vo.getPageSize(), vo.getPageIndex(), null);
        JSONArray data = esUtil.getData(mustQuery, EsTable.PAGE_CHANNEL, pageUtils);
        List<PageChannelListVO> list = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            PageChannelListVO pageList = data.getObject(i, PageChannelListVO.class);
            list.add(pageList);
        }
        Map<String, Object> result = new HashMap();
        result.put("totalNum", pageUtils.getTotalNum());
        result.put("count", pageUtils.getCount());
        result.put("data", list);
        return Result.success(result);
    }

    @Override
    public Result editChannel(PageChannelEditVO vo) {
        if (ovalUtils.validatorRequestParam(vo).size() > 0) return Result.success(ResponseCode.PARAMETERS_NULL);
        String id = vo.getId();
        PageChannelListVO pageChannelList = addEditChannel(vo);
        boolean flag = esUtil.updateColum(EsTable.PAGE_CHANNEL, id, JSONObject.toJSONString(pageChannelList));
        if (!flag) return Result.error(ResponseCode.ES_EXCEPTION);
        return Result.success(ResponseCode.SUCCESS);
    }

    @Override
    public Result getChannelId(String id) {
        if (org.apache.commons.lang3.StringUtils.isBlank(id)) return Result.error(ResponseCode.PARAMETERS_NULL);
        Map<String, Object> map = esUtil.selectOne(EsTable.PAGE_CHANNEL, id);
        PageChannelEditVO pageChannelEditVo = JSONObject.parseObject(JSONObject.toJSONString(map), PageChannelEditVO.class);
        List<ChannelVO> list = new ArrayList<>();
        if (map.get("params") != null) {
            String params = (String) map.get("params");
            String[] split = params.split("&");
            ChannelVO c = null;
            for (String s : split) {
                c = new ChannelVO();
                String[] kv = s.split("=");
                String key = kv[0];
                String value = kv[0];
                c.setKey(key);
                c.setValue(value);
                list.add(c);
            }
        }
        pageChannelEditVo.setList(list);
        return Result.success(pageChannelEditVo);
    }

    @Override
    public Result delChannel(String id) {
        if (StringUtils.isBlank(id)) return Result.error(ResponseCode.PARAMETERS_NULL);
        esUtil.deleteId(EsTable.PAGE_CHANNEL, id);
        return Result.success(ResponseCode.SUCCESS);
    }

    @Override
    public Result addChannel(PageChannelEditVO vo) {
        if (ovalUtils.validatorRequestParam(vo).size() > 0) return Result.success(ResponseCode.PARAMETERS_NULL);
        PageChannelListVO pageChannelList = addEditChannel(vo);
        String id = esUtil.insert(EsTable.PAGE_CHANNEL, JSON.toJSONString(pageChannelList));
        if (StringUtils.isBlank(id)) return Result.error(ResponseCode.ES_EXCEPTION);
        return Result.success(ResponseCode.SUCCESS);
    }

    @Override
    public Result addPage(PageEditVO vo) {
        if (StringUtils.isBlank(vo.getModuleName()) || vo.getPathTypeId() == null || StringUtils.isBlank(vo.getPathName()))
            return Result.error(ResponseCode.PARAMETERS_NULL);
        String id = esUtil.insert(EsTable.PAGE, JSON.toJSONString(vo));
        if (StringUtils.isBlank(id)) return Result.error(ResponseCode.ES_EXCEPTION);
        return Result.success(ResponseCode.SUCCESS);
    }

    @Override
    public Result importExcel(MultipartFile file) {
        try {
            List<Map> excelModelList = ExcelUtils.readExcelWithModel(file.getInputStream());
            log.info("数据大小{}",excelModelList.size());
            Map<String,Object>result=new HashMap<>();
            for (Map<Integer, Object> map : excelModelList) {
                String visitPath =(String) map.get(0);
                String moduleName=(String)map.get(1);;
                String pathName= (String)map.get(2);;
                String pathTypeId= (String)map.get(3);;
                result.put("visitPath",visitPath);
                result.put("moduleName",moduleName);
                result.put("pathName",pathName);
                result.put("pathTypeId",pathTypeId);
                esUtil.insert(EsTable.PAGE,JSON.toJSONString(result));
            }
        } catch (Exception e) {
            log.error("Excel导入失败", e);
            return Result.error(ResponseCode.Fail);
        }
        return   Result.success(ResponseCode.SUCCESS);
    }

    @Override
    public Result getVisitPageList(VisitPageVO vo) {
        if(ovalUtils.validatorRequestParam(vo).size()>0)return Result.error(ResponseCode.PARAMETERS_NULL);
        PageUtils pageUtils = new PageUtils(vo.getPageSize(), vo.getPageIndex(), null);
        List<VisitPageResVO> list = getAccess(vo, pageUtils);
        Map<String, Object> result = new HashMap();
        result.put("totalNum", pageUtils.getTotalNum());
        result.put("count", pageUtils.getCount());
        result.put("data", list);
        return Result.success(result);
    }

    @Override
    public Result getVisitModulePage(ModelVO vo) {
        if(ovalUtils.validatorRequestParam(vo).size()>0) return Result.error(ResponseCode.PARAMETERS_NULL);
        BoolQueryBuilder mustQuery = QueryBuilders.boolQuery();
        mustQuery.must(QueryBuilders.matchQuery("moduleName.keyword",vo.getModuleName()));

        List<Map<String,Object>> lists = groupVisitandName(EsTable.PAGE, mustQuery);
        List<ModelEsVo> list=new ArrayList();
        ModelEsVo modelEsVo=null;
        //占比--类型pathTypeId=1数量
        long sumNum=getPathType(vo);
        for(Map<String,Object> m:lists){
            modelEsVo=new ModelEsVo();
            String visitPath = (String)m.get("visitPath");
            String pathName = (String)m.get("pathName");
            modelEsVo.setPathName(pathName);
            modelEsVo.setVisitPath(visitPath);
            //次数
            long visNum=getVistNum(EsTable.INDEX,modelEsVo,vo);
            modelEsVo.setVisitNum(visNum);
            String rate ="0.00";
            if(sumNum>0){
                rate = DecimalFormatUtils.numberWithPrecision(visNum / sumNum);
            }
            modelEsVo.setRate(rate);
            list.add(modelEsVo);
        }

        return Result.success(list);
    }

    @Override
    public Result getVisitPage(PageAccessVo vo) {
        if (ovalUtils.validatorRequestParam(vo).size()>0) return Result.error(ResponseCode.PARAMETERS_NULL);
        long begTime =vo.getBeginDate().getTime();
        long endTime = vo.getEndDate().getTime();
        QueryBuilder queryBuilder = QueryBuilders.rangeQuery("t").gte(begTime).lte(endTime);
        long visitNum = esUtil.count(EsTable.INDEX, queryBuilder);
        if (visitNum < 0) return Result.error(ResponseCode.ES_EXCEPTION);
        long personNum = esUtil.groupBy(EsTable.INDEX, "uu.keyword", queryBuilder);
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("m_exitpage", 1);
        BoolQueryBuilder mustQuery = QueryBuilders.boolQuery();
        mustQuery.must(queryBuilder);
        mustQuery.must(matchQueryBuilder);
        long entryNum = esUtil.count(EsTable.INDEX, mustQuery);
        if (entryNum < 0) return Result.error(ResponseCode.ES_EXCEPTION);
        MatchQueryBuilder m_openapp = QueryBuilders.matchQuery("m_openapp", 1);
        BoolQueryBuilder avgStopTimeQuery = QueryBuilders.boolQuery();
        avgStopTimeQuery.must(queryBuilder);
        avgStopTimeQuery.must(m_openapp);
        long mppenapp = esUtil.count(EsTable.INDEX, avgStopTimeQuery);
        if (mppenapp < 0) return Result.error(ResponseCode.ES_EXCEPTION);
        long avgStopTime = esUtil.sum(EsTable.INDEX, "m_pagetime", avgStopTimeQuery);
        if (avgStopTime < 0) return Result.error(ResponseCode.ES_EXCEPTION);
        String time = "00:00:00";
        if (mppenapp > 0) time = TimeUtils.getGapTime(avgStopTime / mppenapp);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("visitNum", visitNum);
        jsonObject.put("personNum", personNum);
        MatchQueryBuilder m_sharetime = QueryBuilders.matchQuery("m_sharetime", 1);
        jsonObject.put("shareNum", esUtil.count(EsTable.INDEX,m_sharetime));
        if(visitNum==0){
            jsonObject.put("avgExitRate", "0.00");
        }else{
            jsonObject.put("avgExitRate", DecimalFormatUtils.numberWithPrecision(entryNum/visitNum));
        }
        jsonObject.put("avgStopTime", time);
        return Result.success(jsonObject);
    }

    @Override
    public List<VisitPageResVO> downLoadVisitPageList(VisitVO vo) {
        if(ovalUtils.validatorRequestParam(vo).size()>0)return Collections.emptyList();
        VisitPageVO  visitPageVO=new VisitPageVO();
        BeanUtils.copyProperties(vo,visitPageVO);
        List<VisitPageResVO> access = getAccess(visitPageVO, null);
        return access;
    }

    @Override
    public List<HomePageExcel> downLoadHomePageList(HomePageVO vo) {
        if(ovalUtils.validatorRequestParam(vo).size()>0)return Collections.emptyList();
        List<HomePageExcel> homePage = getHomePage(vo, null);
        return homePage;
    }

    @Override
    public Result getHomePageList(VisitPageVO vo) {
        if(ovalUtils.validatorRequestParam(vo).size()>0)return Result.error(ResponseCode.PARAMETERS_NULL);
        PageUtils pageUtils = new PageUtils(vo.getPageSize(), vo.getPageIndex(), null);
        HomePageVO homePageVO=new HomePageVO();
        BeanUtils.copyProperties(vo,homePageVO);
        List<HomePageExcel> homePage = getHomePage(homePageVO, pageUtils);
        Map<String, Object> result = new HashMap();
        result.put("totalNum", pageUtils.getTotalNum());
        result.put("count", pageUtils.getCount());
        result.put("data", homePage);
        return Result.success(result);
    }

    @Override
    public Result getPageVisitHabitDepth(PageVO vo) {
        if(ovalUtils.validatorRequestParam(vo).size()>0) return  Result.error(ResponseCode.PARAMETERS_NULL);
        BoolQueryBuilder query = QueryBuilders.boolQuery();
        query.must(QueryBuilders.rangeQuery("t").from(vo.getBeginDate().getTime()).to(vo.getEndDate().getTime()));
        query.must(QueryBuilders.matchQuery("appKey", vo.getAppKey())) ;
        query.must(QueryBuilders.matchQuery("tv", "page")) ;
        query.must(QueryBuilders.matchQuery("tl", "show")) ;
        List<Map> depth = getDepth(query);
        List<PageDepthVO> list = getPageDepthVO(depth);
        return Result.success(list);
    }

    @Override
    public List<PageDepthVO> downLoadPageVisitHabitDepth(PageVO vo) {
        if(ovalUtils.validatorRequestParam(vo).size()>0) return  new ArrayList<PageDepthVO>();
        BoolQueryBuilder query = QueryBuilders.boolQuery();
        query.must(QueryBuilders.rangeQuery("t").from(vo.getBeginDate().getTime()).to(vo.getEndDate().getTime()));
        query.must(QueryBuilders.matchQuery("appKey", vo.getAppKey())) ;
        query.must(QueryBuilders.matchQuery("tv", "page")) ;
        query.must(QueryBuilders.matchQuery("tl", "show")) ;
        List<Map> depth = getDepth(query);
        List<PageDepthVO> list = getPageDepthVO(depth);
        return list;
    }

    @Override
    public Result getPageVisitHabitTime(PageVO vo) {
        if(ovalUtils.validatorRequestParam(vo).size()>0) return  Result.error(ResponseCode.PARAMETERS_NULL);
        BoolQueryBuilder query = QueryBuilders.boolQuery();
        query.must(QueryBuilders.rangeQuery("t").from(vo.getBeginDate().getTime()).to(vo.getEndDate().getTime()));
        query.must(QueryBuilders.matchQuery("appKey", vo.getAppKey())) ;
        query.must(QueryBuilders.matchQuery("appKey", vo.getAppKey())) ;
        query.must(QueryBuilders.matchQuery("m_openapp", 1));
        Long num= esUtil.groupBy(EsTable.INDEX, "o.keyword", query);
        List<String> pageTime = PageTime.getPageTime();
        List<PageDepthVO> result = getTime(query, pageTime, num);
        return Result.success(result);
    }

    @Override
    public List<PageDepthVO> downLoadPageVisitHabitTime(PageVO vo) {
        if(ovalUtils.validatorRequestParam(vo).size()>0) return new ArrayList<PageDepthVO>();
        BoolQueryBuilder query = QueryBuilders.boolQuery();
        query.must(QueryBuilders.rangeQuery("t").from(vo.getBeginDate().getTime()).to(vo.getEndDate().getTime()));
        query.must(QueryBuilders.matchQuery("appKey", vo.getAppKey())) ;
        query.must(QueryBuilders.matchQuery("appKey", vo.getAppKey())) ;
        query.must(QueryBuilders.matchQuery("m_openapp", 1));
        Long num= esUtil.groupBy(EsTable.INDEX, "o", query);
        List<String> pageTime = PageTime.getPageTime();
        List<PageDepthVO> result = getTime(query, pageTime, num);
        return result;
    }

    @Override
    public List<PageDepthVO> downLoadPageVisitHabitFrequency(PageVO vo) {
        List<ConstraintViolation> constraintViolations = ovalUtils.validatorRequestParam(vo);
        if (constraintViolations.size() > 0) return new ArrayList<PageDepthVO>();
        BoolQueryBuilder mustQuery = QueryBuilders.boolQuery();
        MatchQueryBuilder eventNameBuilder = QueryBuilders.matchQuery("tl.keyword", "show");
        mustQuery.must(eventNameBuilder);
        mustQuery.must(QueryBuilders.matchQuery("k.keyword", vo.getAppKey()));
        MatchQueryBuilder tvNameBuilder = QueryBuilders.matchQuery("tv.keyword", "page");
        mustQuery.must(tvNameBuilder);
        long begTime = vo.getBeginDate().getTime();
        long endTime = vo.getEndDate().getTime();
        ;//TimeUtils.getstrDate(endDate).getTime();
        mustQuery.must(QueryBuilders.rangeQuery("t").gte(begTime).lte(endTime));
        List<PageDepthVO> pageVisitHabitFrequency = getPageVisitHabitFrequency(mustQuery);
        return pageVisitHabitFrequency;
    }

    @Override
    public Result getPagesPathFirst(PageVO vo) {
        if(ovalUtils.validatorRequestParam(vo).size()>0)return Result.error(ResponseCode.PARAMETERS_NULL);
        BoolQueryBuilder mustQuery = QueryBuilders.boolQuery();
        MatchQueryBuilder depathBuilder = QueryBuilders.matchQuery("m_depth", 1);
        mustQuery.must(depathBuilder);
        mustQuery.must(QueryBuilders.matchQuery("k.keyword", vo.getAppKey()));
        long begTime = vo.getBeginDate().getTime();
        long endTime = vo.getEndDate().getTime();
        mustQuery.must(QueryBuilders.rangeQuery("t").gte(begTime).lte(endTime));
        List<Map<String, Object>> lists = esUtil.groupByMap(EsTable.INDEX, "p.keyword", mustQuery, Integer.MAX_VALUE);
        List<PagesPathFirstRepVO> list=new ArrayList<>();
        PagesPathFirstRepVO bean=null;
        for (Map<String, Object> map : lists) {
            bean=new PagesPathFirstRepVO();
            String p = (String) map.get("key");
            BoolQueryBuilder pagemustQuery = QueryBuilders.boolQuery();
            pagemustQuery.must(QueryBuilders.matchQuery("visitPath.keyword", p));
            JSONArray pageData = esUtil.getData(pagemustQuery, EsTable.PAGE, null);
            if(pageData.size()>0){
                bean.setPageId( pageData.getJSONObject(0).getString("id"));
                bean.setName( pageData.getJSONObject(0).getString("pathName"));
                bean.setDepth(1);
                list.add(bean);
            }
        }
        return Result.success(list);
    }

    @Override
    public Result getPagePathNode(PageNodeVO vo) {
        return null;
    }


    private List<PageDepthVO> getPageVisitHabitFrequency(QueryBuilder query) {
        SearchResponse response = null;
        Map<String, Long> countResult = new HashMap<>();
        try {
            SearchRequest searchRequest = new SearchRequest();
            searchRequest.indices(EsTable.INDEX);
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            //查询条件
            //QueryBuilder query = QueryBuilders.boolQuery().must(QueryBuilders.rangeQuery("timestamp").from(new Date(startTime)).to(new Date(endTime))).must(QueryBuilders.matchQuery("level", "ERROR"));
            //统计条件
            TermsAggregationBuilder serviceLineAgg = AggregationBuilders.terms("serviceline_count").field("b.keyword");
            TermsAggregationBuilder appNameAgg = AggregationBuilders.terms("appName_count").field("o.keyword");

            searchSourceBuilder.query(query).size(0);
            searchSourceBuilder.size(0);
            searchSourceBuilder.aggregation(serviceLineAgg.subAggregation(appNameAgg)).size(0);
            searchRequest.source(searchSourceBuilder);
            response = client.search(searchRequest, RequestOptions.DEFAULT);
            //组装数据
            analyticData(response, countResult);
            if (countResult.size() == 0) {
                return Collections.EMPTY_LIST;
            }
            int total = countResult.size();
            log.info("用户频率总数total{}", total);
            Map<Long, Long> userNum = new TreeMap<>();
            countResult.forEach((key, value) -> {
                if (userNum.get(value) == null) {
                    userNum.put(value, 1L);
                } else {
                    Long num = userNum.get(value);
                    userNum.put(value, num + 1L);
                }

            });

            List<PageDepthVO> frequency = getFrequency(userNum, total);
            return frequency;
        } catch (IOException e) {
            log.error("{}", e);
            return Collections.EMPTY_LIST;
        }
    }

    private List<PageDepthVO> getFrequency(Map<Long, Long> userNum, int total) {
        int lastNum = 0;
        List<PageDepthVO> list = new ArrayList<>();
        for (Map.Entry<Long, Long> m : userNum.entrySet()) {
            //key-userNum
            //value-num
            Long key = m.getKey();
            Long value = m.getValue();
            if (key < 10) {
                //10次及10次以下的数据
                PageDepthVO pageDepthVO = new PageDepthVO();
                pageDepthVO.setKeyName( key + "次");
                pageDepthVO.setNum(value.intValue());
                String percent = getPercent(value.intValue(), total);
                String rate=total == 0 ? "0.00%" : percent + "%";
                pageDepthVO.setRate(rate);
                list.add(pageDepthVO);

            } else {
                int i = value.intValue();
                lastNum += i;
            }
        }

        if (lastNum > 0) {
            PageDepthVO pageDepthVO = new PageDepthVO();
            pageDepthVO.setKeyName( "10次以上");
            pageDepthVO.setNum(lastNum);
            String percent = getPercent(lastNum, total);
            String rate=total == 0 ? "0.00%" : percent + "%";
            pageDepthVO.setRate(rate);
            list.add(pageDepthVO);
        }
        return list;
    }

    private String getPercent(int num1, int num2) {
        // 创建一个数值格式化对象
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后2位
        numberFormat.setMaximumFractionDigits(2);
        if(num1==0||num2==0){
           return  "0.00";
        }
        String result = numberFormat.format((float) num1 / (float) num2 * 100);
        return result;
    }

    /**
     *
     * @Description 添加或编辑渠道
     * @author xing.liu
     * @date 2020/9/14
     **/
    private  PageChannelListVO  addEditChannel(PageChannelEditVO  vo){
        List<ChannelVO> list = vo.getList();
        String string = "";
        PageChannelListVO channel = new PageChannelListVO();
        BeanUtils.copyProperties(vo, channel);
        for (int i = 0; i < list.size(); i++) {
            String key = list.get(i).getKey();
            String value = list.get(i).getValue();
            String param = key + "=" + value;
            if (i == 0) {
                string = param;
            } else {
                string = string + "&" + param;
            }
        }
        channel.setParams(string);
        return  channel;
    }

    private void analyticData(SearchResponse response, Map<String, Long> countResult) {
        Map<String, Aggregation> serviceLineMap = response.getAggregations().asMap();
        ParsedStringTerms serviceLineTerms = (ParsedStringTerms) serviceLineMap.get("serviceline_count");
        List serviceLists = serviceLineTerms.getBuckets();
        Map<String, Long> groupResult = new LinkedHashMap<>();
        for (Object serviceList : serviceLists) {
            ParsedStringTerms.ParsedBucket serviceListObj = (ParsedStringTerms.ParsedBucket) serviceList;
            String serviceLine = serviceListObj.getKeyAsString();
            Map<String, Aggregation> appNameMap = serviceListObj.getAggregations().asMap();
            ParsedStringTerms appNameTerms = (ParsedStringTerms) appNameMap.get("appName_count");
            List appNameLists = appNameTerms.getBuckets();
            for (Object appNameList : appNameLists) {
                ParsedStringTerms.ParsedBucket appNameObj = (ParsedStringTerms.ParsedBucket) appNameList;
                String appName = appNameObj.getKeyAsString();
                groupResult.put(serviceLine + "&&" + appName, 0l);
            }
        }
        //OpenId,count(1) userNum
        groupResult.forEach((key, value) -> {
            String openid = key.split("&&")[1];
            if (countResult.get(openid) == null) {
                countResult.put(openid, 1L);
            } else {
                Long num = countResult.get(openid);
                countResult.put(openid, num + 1L);
            }
        });
    }

    /**
     * @Description 退出率，次时长、次时长求和
     * @author xing.liu
     * @date 2020/9/16
     **/
    private void  sumPage(VisitPageResVO vo,QueryBuilder queryBuilder){
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilder);
        searchSourceBuilder.size(0);
        SumAggregationBuilder mexitpage = AggregationBuilders.sum("sumexitpage").field("m_exitpage");
        searchSourceBuilder.aggregation(mexitpage);
        SumAggregationBuilder msharetime = AggregationBuilders.sum("sumsharetime").field("m_sharetime");
        searchSourceBuilder.aggregation(msharetime);
        SumAggregationBuilder mapptime = AggregationBuilders.sum("sumStop").field("m_apptime");
        searchSourceBuilder.aggregation(mapptime);
        SearchRequest searchRequest = new SearchRequest();
        // 设置request要搜索的索引和类型
        searchRequest.indices(EsTable.INDEX);
        // 设置SearchSourceBuilder查询属性
        searchRequest.source(searchSourceBuilder);
        try {
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            Aggregations aggregations = searchResponse.getAggregations();
            Sum sumexitpage = aggregations.get("sumexitpage");
            Sum sumStop = aggregations.get("sumStop");
            Sum  sumsharetime=aggregations.get("sumsharetime");
            long sumExitLong =new Double(sumexitpage.getValue()).longValue();
            long sumStopLong=new Double(sumStop.getValue()).longValue();
            long sumShareLong=new Double(sumsharetime.getValue()).longValue();
            vo.setShareNum(sumShareLong);
            if(vo.getVisitNum()==0){
                vo.setAvgExitRate("0");
            }else{
                String avgRate = DecimalFormatUtils.numberWithPrecision(sumExitLong/vo.getVisitNum());
                vo.setAvgExitRate(avgRate);
            }
            String stopRate = TimeUtils.getGapTime(sumStopLong);
            vo.setAvgStopTime(stopRate);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     *
     * @Description  groupby  visitPath,pathName
     * @author xing.liu
     * @date 2020/9/17
     **/
    private   List<Map<String,Object>>   groupVisitandName(String index,QueryBuilder queryBuilder){
        List<Map<String,Object>> result=new ArrayList<>();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //searchSourceBuilder.size(0);
        searchSourceBuilder.query(queryBuilder);
        TermsAggregationBuilder aggregation = AggregationBuilders.terms("groubyVp")
                .script(new Script("doc['visitPath.keyword'] +'#'+doc['pathName.keyword']"));
        searchSourceBuilder.aggregation(aggregation);
        // TermsAggregationBuilder tlBuilder = AggregationBuilders.terms("tlgroupy").field("tl.keyword");
        SearchRequest searchRequest = new SearchRequest();
        // 设置request要搜索的索引和类型
        searchRequest.indices(index);
        // 设置SearchSourceBuilder查询属性
        searchRequest.source(searchSourceBuilder);
        try {
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            Aggregations aggregations = searchResponse.getAggregations();
            Terms tvgroupy = aggregations.get("groubyVp");
            for(Terms.Bucket buck : tvgroupy.getBuckets()) {
                Map map = new HashMap();
                String[] arr = buck.getKeyAsString().split("#");
                map.put("visitPath", arr[0].replace("[", "").replace("]", ""));
                long docCount = buck.getDocCount();
                map.put("pathName",arr[1].replace("[", "").replace("]", ""));
                result.add(map);
            }
            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }


    private long getVistNum(String index, ModelEsVo vo,ModelVO modelVO) {
        BoolQueryBuilder mustQuery = QueryBuilders.boolQuery();
        mustQuery.must(QueryBuilders.matchQuery("p.keyword",vo.getVisitPath()));
        RangeQueryBuilder timeBuilder = QueryBuilders.rangeQuery("t").gte(modelVO.getBeginDate().getTime()).lte(modelVO.getEndDate().getTime());
        mustQuery.must(timeBuilder);
        long visNum= esUtil.count(index,mustQuery);
        return visNum;
    }

    /**
     * @Description 求pathTypeId=1,上报p字段总数
     * @author xing.liu
     * @date 2020/9/17
     **/
    private   long  getPathType(ModelVO vo){
        BoolQueryBuilder mustQuery = QueryBuilders.boolQuery();
        mustQuery.must(QueryBuilders.matchQuery("pathTypeId",1));
        RangeQueryBuilder timeBuilder = QueryBuilders.rangeQuery("t").gte(vo.getBeginDate()).lte(vo.getEndDate());
        mustQuery.must(timeBuilder);
        JSONArray data = esUtil.getData(mustQuery, EsTable.PAGE, null);
        long sumNum=0;
        for (int i = 0; i < data.size(); i++) {
            String visitPath = data.getJSONObject(i).getString("visitPath");
            if(StringUtils.isBlank(visitPath)) continue;
            BoolQueryBuilder visitPathB = QueryBuilders.boolQuery();
            visitPathB.must(QueryBuilders.matchQuery("p.keyword",visitPath));
            RangeQueryBuilder visitPathRanger = QueryBuilders.rangeQuery("t").gte(vo.getBeginDate()).lte(vo.getEndDate());
            visitPathB.must(visitPathRanger);
            long count = esUtil.count(EsTable.INDEX, visitPathB);
            sumNum+=count;
        }
        return  sumNum;

    }

    private List<HomePageExcel> getHomePage(HomePageVO vo,PageUtils pageUtils){
        BoolQueryBuilder mustQuery = QueryBuilders.boolQuery();
        if (StringUtils.isNotBlank(vo.getVisitPath())) {
            mustQuery.must(QueryBuilders.matchQuery("visitPath.keyword", vo.getVisitPath()));
        }
        if (StringUtils.isNotBlank(vo.getModuleName())) {
            mustQuery.must(QueryBuilders.matchQuery("moduleName.keyword", vo.getModuleName()));
        }
        if (vo.getPathTypeId()!=null) {
            mustQuery.must(QueryBuilders.matchQuery("pathTypeId.keyword", vo.getPathTypeId()));
        }
        JSONArray data = esUtil.getData(mustQuery, EsTable.PAGE, pageUtils);
        List<HomePageExcel> list = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            HomePageExcel homePageExcel = data.getObject(i, HomePageExcel.class);
            //入口页就是当前周期的第一个页面（page.onshow)，m_entrypage=1
            String visitPath = homePageExcel.getVisitPath();
            QueryBuilder queryBuilder = QueryBuilders.rangeQuery("t").gte(vo.getBeginDate().getTime()).lte(vo.getEndDate().getTime());
            mustQuery.must(queryBuilder);
            mustQuery.must(QueryBuilders.matchQuery("p.keyword",visitPath));
            mustQuery.must(QueryBuilders.matchQuery("m_entrypage",1));
            long visitNum = esUtil.count(EsTable.INDEX, mustQuery);
            long personNum=esUtil.groupBy(EsTable.INDEX,"uu.keyword",mustQuery);
            homePageExcel.setVisitNum(visitNum);
            homePageExcel.setPersonNum(personNum);
            getHome(homePageExcel,queryBuilder);
            list.add(homePageExcel);
        }
        return list;
    }

    /**
     * 
     * @Description 
     * @author xing.liu
     * @date 2020/9/18
     **/
    private void  getHome(HomePageExcel vo,QueryBuilder queryBuilder){
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(queryBuilder);
            searchSourceBuilder.size(0);
            SumAggregationBuilder mentrypage = AggregationBuilders.sum("sumentrypage").field("m_entrypage");
            searchSourceBuilder.aggregation(mentrypage);
            SumAggregationBuilder mapptime = AggregationBuilders.sum("sumStop").field("m_apptime");
            searchSourceBuilder.aggregation(mapptime);
            SearchRequest searchRequest = new SearchRequest();
            // 设置request要搜索的索引和类型
            searchRequest.indices(EsTable.INDEX);
            // 设置SearchSourceBuilder查询属性
            searchRequest.source(searchSourceBuilder);
            try {
                SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
                Aggregations aggregations = searchResponse.getAggregations();
                Sum sumentrypage = aggregations.get("sumentrypage");
                Sum sumStop = aggregations.get("sumStop");
                long sumEntryLong =new Double(sumentrypage.getValue()).longValue();
                vo.setEntryNum(sumEntryLong);
                long sumStopLong=new Double(sumStop.getValue()).longValue();
                String stopRate = TimeUtils.getGapTime(sumStopLong);
                vo.setAvgStopTime(stopRate);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
    }

    private List<VisitPageResVO> getAccess(VisitPageVO vo,PageUtils  pageUtils){
        //页面管理--
        BoolQueryBuilder mustQuery = QueryBuilders.boolQuery();
        if (StringUtils.isNotBlank(vo.getVisitPath())) {
            mustQuery.must(QueryBuilders.matchQuery("visitPath.keyword", vo.getVisitPath()));
        }
        if (StringUtils.isNotBlank(vo.getModuleName())) {
            mustQuery.must(QueryBuilders.matchQuery("moduleName.keyword", vo.getModuleName()));
        }
        if (vo.getPathTypeId()!=null) {
            mustQuery.must(QueryBuilders.matchQuery("pathTypeId.keyword", vo.getPathTypeId()));
        }
        JSONArray data = esUtil.getData(mustQuery, EsTable.PAGE, pageUtils);
        List<VisitPageResVO> list = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            VisitPageResVO pageList = data.getObject(i, VisitPageResVO.class);
            //受访页,取上报数据字段p
            String visitPath = pageList.getVisitPath();
            QueryBuilder queryBuilder = QueryBuilders.rangeQuery("t").gte(vo.getBeginDate().getTime()).lte(vo.getEndDate().getTime());
            mustQuery.must(queryBuilder);
            mustQuery.must(QueryBuilders.matchQuery("p.keyword",visitPath));
            long visitNum = esUtil.count(EsTable.INDEX, mustQuery);
            long personNum=esUtil.groupBy(EsTable.INDEX,"uu.keyword",mustQuery);
            pageList.setVisitNum(visitNum);
            pageList.setPersonNum(personNum);
            sumPage(pageList,mustQuery);
            list.add(pageList);
        }
        return list;
    }

    private List<Map>  getDepth(QueryBuilder  queryBuilder){
        List<Map> result=new ArrayList<>();
        try {
            // 1、创建search请求
            SearchRequest searchRequest = new SearchRequest(EsTable.INDEX);
            // 2、用SearchSourceBuilder来构造查询请求体 ,请仔细查看它的方法，构造各种查询的方法都在这。
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            sourceBuilder.query(queryBuilder);
            sourceBuilder.size(0);
            //加入聚合
            //字段值项分组聚合
            TermsAggregationBuilder aggregation = AggregationBuilders.terms("by_fngroup")
                    .script(new Script("doc['b.keyword'] +'#'+doc['o.keyword']"))
                    //.field("fngroup.keyword")
                    .size(Integer.MAX_VALUE)
                    .order(BucketOrder.aggregation("count", true));
            aggregation.subAggregation(AggregationBuilders.cardinality("count")
                    .field("p.keyword"));
            sourceBuilder.aggregation(aggregation);
            searchRequest.source(sourceBuilder);
            log.info(sourceBuilder.toString());
            SearchResponse searchResponse = client.search(searchRequest,RequestOptions.DEFAULT);

            //4、处理响应
            //搜索结果状态信息
            if(RestStatus.OK.equals(searchResponse.status())) {
                // 获取聚合结果
                Aggregations aggregations = searchResponse.getAggregations();
                Terms byAgeAggregation = aggregations.get("by_fngroup");
                for(Terms.Bucket buck : byAgeAggregation.getBuckets()) {
                    Map map=new HashMap();
                    String[] arr= buck.getKeyAsString().split("#");
                    map.put("b",arr[0].replace("[","").replace("]",""));
                    map.put("o",arr[1].replace("[","").replace("]",""));
                    //取子聚合
                    ParsedCardinality count = buck.getAggregations().get("count");
                    long value = count.getValue();
                    map.put("count",value);
                    result.add(map);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error("{}",e.getMessage());
            return Collections.emptyList();
        }
        return  result;
    }

    private  List<PageDepthVO> getPageDepthVO(List<Map>depth){
        Map<Long,Map>map=new TreeMap<>();
        //count(distinct openid)Num  group count
        for(Map m:depth){
            Map dis=new HashMap();
            Long count=(Long)m.get("count");
            String open= (String)m.get("o");
            if(map.get(count)==null){
                dis.put(open,1);
                map.put(count,dis);
            }else{
                dis.put(open,1);
                Map map1 = map.get(count);
                map1.putAll(dis);
            }
        }
        //获取小程序周期总数
        Long sum=0L;
        for(Map.Entry<Long, Map> entry: map.entrySet()) {
            int size = entry.getValue().size();
            sum+=size;
        }
        List<PageDepthVO> result=new ArrayList<>();
        for(Map.Entry<Long, Map> entry: map.entrySet()) {
            int size = entry.getValue().size();
            String   keyName=entry.getKey()+"次";
            PageDepthVO pageDepthVO=new PageDepthVO();
            pageDepthVO.setKeyName(keyName);
            pageDepthVO.setNum(size);
            pageDepthVO.setRate(getPercent(size,sum.intValue())+"%");
            result.add(pageDepthVO);
        }
        return  result;
    }

    private  List<PageDepthVO> getTime(BoolQueryBuilder query,List<String>pageTime,Long num){
        List<PageDepthVO> result=new ArrayList<>();
        PageDepthVO pageDepthVO=null;
        for(String s:pageTime){
            pageDepthVO=new PageDepthVO();
            pageDepthVO.setKeyName(s);
            if(s.contains(PageTime.twentys)){
                query.must(QueryBuilders.rangeQuery("m_apptime").lte(20*1000));
            }else if(s.contains(PageTime.sixtys)){
                QueryBuilder queryBuilder = QueryBuilders.rangeQuery("m_apptime").gte(20*1000).lte(60*1000);
                query.must(queryBuilder);
            }else if(s.contains(PageTime.two)){
                QueryBuilder queryBuilder = QueryBuilders.rangeQuery("m_apptime").gte(1*60*1000).lte(2*60*1000);
                query.must(queryBuilder);
            }else if(s.contains(PageTime.five)){
                QueryBuilder queryBuilder = QueryBuilders.rangeQuery("m_apptime").gte(2*60*1000).lte(5*60*1000);
                query.must(queryBuilder);
            }else if(s.contains(PageTime.twenty)){
                QueryBuilder queryBuilder = QueryBuilders.rangeQuery("m_apptime").gte(10*60*1000).lte(20*60*1000);
                query.must(queryBuilder);
            }else if(s.contains(PageTime.thirty)){
                QueryBuilder queryBuilder = QueryBuilders.rangeQuery("m_apptime").gte(20*60*1000).lte(30*60*1000);
                query.must(queryBuilder);
            }else if(s.contains(PageTime.forty)){
                QueryBuilder queryBuilder = QueryBuilders.rangeQuery("m_apptime").gte(30*60*1000).lte(40*60*1000);
                query.must(queryBuilder);
            }else if(s.contains(PageTime.sixty)){
                QueryBuilder queryBuilder = QueryBuilders.rangeQuery("m_apptime").gte(40*60*1000).lte(60*60*1000);
                query.must(queryBuilder);
            }else if(s.contains(PageTime.oh)){
                QueryBuilder queryBuilder = QueryBuilders.rangeQuery("m_apptime").gte(1*60*60*1000).lte(2*60*60*1000);
                query.must(queryBuilder);
            }else if(s.contains(PageTime.th)){
                QueryBuilder queryBuilder = QueryBuilders.rangeQuery("m_apptime").gte(2*60*60*1000);
                query.must(queryBuilder);
            }
            Long  userNum= esUtil.groupBy(EsTable.INDEX, "o.keyword", query);
            if(userNum>0 && num>0) {
                pageDepthVO.setNum(userNum.intValue());
                pageDepthVO.setRate(getPercent(userNum.intValue(), num.intValue()) + "%");
                result.add(pageDepthVO);
            }
        }
        return  result;
    }



}
