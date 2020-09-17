package com.etocrm.sdk.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.ParsedCardinality;
import org.elasticsearch.search.aggregations.metrics.Sum;
import org.elasticsearch.search.aggregations.metrics.SumAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
public class EsUtil {

    @Autowired
    private RestHighLevelClient client;

    /**
     * es，查询数据条数
     */
    public long count(String idxName, QueryBuilder queryBuilder) {
        long count=0;
        try {
            CountRequest countRequest = new CountRequest(idxName);
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(queryBuilder);
            countRequest.source(searchSourceBuilder);
            CountResponse response = client.count(countRequest, RequestOptions.DEFAULT);
            count = response.getCount();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return  -1;
        }
        return  count  ;
    }

    /**
     * es，groupby 分组后数据条数
     */
    public long groupBy(String idxName, String field, QueryBuilder queryBuilder) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.size(0);
        searchSourceBuilder.query(queryBuilder);
        searchSourceBuilder.aggregation(AggregationBuilders.cardinality("groupByName")
                .field(field));
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(idxName);
        searchRequest.source(searchSourceBuilder);
        try {
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            Aggregations aggregations = searchResponse.getAggregations();
            Aggregation countName = aggregations.get("groupByName");
            long i = ((ParsedCardinality) countName).getValue();
            return i;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return 0;

    }

    public long sum(String indexName, String field, QueryBuilder queryBuilder) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.size(0);
        searchSourceBuilder.query(queryBuilder);
        SumAggregationBuilder sumBuilder = AggregationBuilders.sum("sum_t").field(field);
        searchSourceBuilder.aggregation(sumBuilder);
        SearchRequest searchRequest = new SearchRequest();
        // 设置request要搜索的索引和类型
        searchRequest.indices(indexName);
        // 设置SearchSourceBuilder查询属性
        searchRequest.source(searchSourceBuilder);
        try {
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            Aggregations aggregations = searchResponse.getAggregations();
            Sum countName = aggregations.get("sum_t");
            //long totalHits = searchResponse.getHits().getTotalHits();
            double i= countName.getValue();
            long l =new Double(i).longValue();
            return l;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return  -1;
        }
    }

    /**
     * 多列分组
     * @return
     */
    @Deprecated
    public void getList(String  index){
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //searchSourceBuilder.size(0);
        //searchSourceBuilder.query(queryBuilder);
        TermsAggregationBuilder aggregation = AggregationBuilders.terms("tvtl")
                .script(new Script("doc['tv.keyword'] +'#'+doc['tl.keyword']"));

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
            Terms tvgroupy = aggregations.get("tvtl");
            List<Map> result=new ArrayList<>();
            for(Terms.Bucket buck : tvgroupy.getBuckets()) {
                Map map = new HashMap();
                String[] arr = buck.getKeyAsString().split("#");
               // map.put("module", arr[0].replace("[", "").replace("]", ""));
               // map.put("user_id", arr[1].replace("[", "").replace("]", ""));
                map.put("showType", arr[0].replace("[", "").replace("]", "")+arr[1].replace("[", "").replace("]", ""));
                result.add(map);
            }

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }

    public   Map<String,Object>   selectOne(String index,String id){
       try{
           GetRequest getRequest=new GetRequest(index);
           getRequest.id(id);
           GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
           return getResponse.getSourceAsMap();
       }catch (Exception  e){
         log.error("查询信息异常",e.getMessage());
       }
       return  Collections.emptyMap();
    }

    public boolean updateColum(String index, String id, Map<String, Object> map) {
        try {
            UpdateRequest updateRequest = new UpdateRequest();
            updateRequest.index(index);
            updateRequest.id(id);
            //updateRequest.type("doc");
            // updateRequest.routing((String) map.get("uu"));
            updateRequest.doc(map);
            client.update(updateRequest, RequestOptions.DEFAULT);
            log.info("es修改成功");
            return true;
        } catch (Exception e) {
            log.error("修改数据id{}异常{}",id, e.getMessage());
            return false;
        }
    }


    public String insert(String index,String json) {
        String id = "";
        try {
            IndexRequest indexRequest = new IndexRequest();
            indexRequest.index(index);
            //indexRequest.type("doc");
           // indexRequest.routing(root.getUu());
            indexRequest.source(json, XContentType.JSON);
            log.info("准备数据插入es");
            IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
            id = indexResponse.getId();
            log.info("数据插入es成功");
            return id;
        } catch (Exception e) {
            log.error("添加单条type{}异常{}", e.getMessage());
            id = "";
            return id;
        }

    }

    public void deleteId(String index,String id) {
        try {
            DeleteRequest deleteRequest = new DeleteRequest();
            deleteRequest.index(index);
            //indexRequest.type("doc");
            // indexRequest.routing(root.getUu());
            deleteRequest.id(id);
            DeleteResponse delete = client.delete(deleteRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.error("删除单条{}异常{}",id, e.getMessage());
        }

    }

    public JSONArray getData(QueryBuilder queryBuilder, String  index, PageUtils pageUtils){
        JSONArray array=new JSONArray();
       try{
           SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
           searchSourceBuilder.query(queryBuilder);
           SearchRequest searchRequest = new SearchRequest(index);
           if(pageUtils!=null){
               long totalNum= count(index, queryBuilder);
               pageUtils.setTotalNum((int)totalNum);
               searchSourceBuilder.size(pageUtils.getPageSize().intValue());
               searchSourceBuilder.from(pageUtils.getLimit().intValue());
           }else{

               long totalNum= count(index, queryBuilder);
               searchSourceBuilder.size((int)totalNum);
           }
           searchRequest.source(searchSourceBuilder);
           SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
           SearchHits hits = searchResponse.getHits();
           log.info(""+hits.getHits().length);
           SearchHit[] hits1 = hits.getHits();
           for(SearchHit hit:hits1){
               JSONObject obj = JSONObject.parseObject(hit.getSourceAsString());
               obj.put("id",hit.getId());
               array.add(obj);
           }

       }catch (Exception e){
           log.error("{}",e.getMessage());
       }
       return array;
    }
    public boolean updateColum(String index, String id, String  json) {
        try {
            UpdateRequest updateRequest = new UpdateRequest();
            updateRequest.index(index);
            updateRequest.id(id);
            //updateRequest.type("doc");
            // updateRequest.routing((String) map.get("uu"));
            updateRequest.doc(json, XContentType.JSON);
            client.update(updateRequest, RequestOptions.DEFAULT);
            log.info("es修改成功");
            return true;
        } catch (Exception e) {
            log.error("修改数据id{}异常{}",id, e.getMessage());
            return false;
        }
    }

}
