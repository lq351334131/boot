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
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
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
        List<Map<String, Object>> pageVisitHabitFrequency = getPageVisitHabitFrequency(mustQuery);
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
        //字段值不为空
        mustQuery.must(QueryBuilders.existsQuery("visitPath"));
        mustQuery.must(QueryBuilders.existsQuery("moduleName"));
        mustQuery.must(QueryBuilders.existsQuery("pathName"));
        PageUtils pageUtils = new PageUtils(vo.getPageSize(), vo.getPageIndex(), null);
        JSONArray data = esUtil.getData(mustQuery, EsTable.PAGE, pageUtils);
        List<VisitPageResVO> list = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            VisitPageResVO pageList = data.getObject(i, VisitPageResVO.class);
            //受访页,取上报数据字段p,事件类型page.show
            String visitPath = pageList.getVisitPath();
            QueryBuilder queryBuilder = QueryBuilders.rangeQuery("t").gte(vo.getBeginDate()).lte(vo.getEndDate());
            mustQuery.must(queryBuilder);
            long visitNum = esUtil.count(EsTable.INDEX, mustQuery);
            long personNum=esUtil.groupBy(EsTable.INDEX,"uu.keyword",mustQuery);

            pageList.setVisitNum(visitNum);
            pageList.setPersonNum(personNum);

            list.add(pageList);
        }
        Map<String, Object> result = new HashMap();
        result.put("totalNum", pageUtils.getTotalNum());
        result.put("count", pageUtils.getCount());
        result.put("data", list);
        return Result.success(result);
    }

    private List<Map<String, Object>> getPageVisitHabitFrequency(QueryBuilder query) {
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

            List<Map<String, Object>> frequency = getFrequency(userNum, total);
            return frequency;
        } catch (IOException e) {
            log.error("{}", e);
            return Collections.EMPTY_LIST;
        }
    }

    private List<Map<String, Object>> getFrequency(Map<Long, Long> userNum, int total) {
        int lastNum = 0;
        List<Map<String, Object>> list = new ArrayList<>();
        for (Map.Entry<Long, Long> m : userNum.entrySet()) {
            //key-userNum
            //value-num
            Long key = m.getKey();
            Long value = m.getValue();
            if (key < 10) {
                //10次及10次以下的数据
                Map<String, Object> map = new HashMap<>();
                map.put("keyName", key + "次");
                map.put("num", value);
                String percent = getPercent(value.intValue(), total);
                map.put("rate", total == 0 ? "0.00%" : percent + "%");
                list.add(map);

            } else {
                int i = value.intValue();
                lastNum += i;
            }
        }

        if (lastNum > 0) {
            Map<String, Object> map = new HashMap<>();
            map.put("keyName", "10次以上");
            map.put("num", lastNum);
            String percent = getPercent(lastNum, total);
            map.put("rate", total == 0 ? "0.00%" : percent + "%");
            list.add(map);
        }
        return list;
    }

    private String getPercent(int num1, int num2) {
        // 创建一个数值格式化对象
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后2位
        numberFormat.setMaximumFractionDigits(2);
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

}
