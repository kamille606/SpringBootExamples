package com.aaa.es.utils;

import com.aaa.es.status.StatusEnum;
import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class ESUtil {

    @Autowired
    private TransportClient transportClient;

    private static TransportClient client;

    @PostConstruct
    public void init() {
        client = this.transportClient;
    }

    public static Map<String, Object> resultMap = new HashMap<String, Object>();

    /**
     * @Author      : pyt
     * @Description : 创建索引
     * @Params:     : [index]
     * @Return      : java.lang.String
     * @Date        : Created in 2019/12/16 18:58
     **/
    public static Map<String, Object> createIndex(String index) {
        if (!isIndexExist(index)) {
            CreateIndexResponse createIndexResponse = client.admin().indices().prepareCreate(index).execute().actionGet();
            //再次判断创建是非成功
            if (createIndexResponse.isAcknowledged()) {
                resultMap.put("code", StatusEnum.OPRATION_SUCCESS.getCode());
                resultMap.put("msg", StatusEnum.OPRATION_SUCCESS.getMsg());
            } else {
                resultMap.put("code", StatusEnum.OPRATION_FAILED.getCode());
                resultMap.put("msg", StatusEnum.OPRATION_FAILED.getMsg());
            }
        } else {
            resultMap.put("code", StatusEnum.EXIST.getCode());
            resultMap.put("msg", StatusEnum.EXIST.getMsg());
        }


        return resultMap;
    }
    /**
     * @Author      : pyt
     * @Description : 删除索引
     * @Params:     : [index]
     * @Return      : java.util.Map<java.lang.String,java.lang.Object>
     * @Date        : Created in 2019/12/16 19:46
     **/
    public static Map<String, Object> deleteIndex(String index) {
        if (!isIndexExist(index)) {
            resultMap.put("code", StatusEnum.NOT_EXIST.getCode());
            resultMap.put("msg", StatusEnum.NOT_EXIST.getMsg());
        } else {
            DeleteIndexResponse deleteIndexResponse = client.admin().indices().prepareDelete(index).execute().actionGet();
            if (deleteIndexResponse.isAcknowledged()) {
                resultMap.put("code", StatusEnum.OPRATION_SUCCESS.getCode());
                resultMap.put("msg", StatusEnum.OPRATION_SUCCESS.getMsg());
            } else {
                resultMap.put("code", StatusEnum.OPRATION_FAILED.getCode());
                resultMap.put("msg", StatusEnum.OPRATION_FAILED.getMsg());
            }
        }
        return resultMap;
    }
    /**
     * @Author      : pyt
     * @Description : 添加数据
     * @Params:     : [index, type, id]
     * @Return      : java.util.Map<java.lang.String,java.lang.Object>
     * @Date        : Created in 2019/12/16 19:51
     **/
    public static Map<String, Object> addData(Map<String, Object> mapObj, String index, String type, String id) {
        IndexResponse indexResponse = client.prepareIndex(index, type, id).setSource(mapObj).get();
        String responseStatus = indexResponse.status().toString();
        if ("CREATED".equals(responseStatus.toUpperCase())) {
            resultMap.put("code", StatusEnum.OPRATION_SUCCESS.getCode());
            resultMap.put("msg", StatusEnum.OPRATION_SUCCESS.getMsg());
        } else {
            resultMap.put("code", StatusEnum.OPRATION_FAILED.getCode());
            resultMap.put("msg", StatusEnum.OPRATION_FAILED.getMsg());
        }
        return resultMap;
    }

    /**
     * @Author      : pyt
     * @Description : UUID添加数据
     * @Params:     : [mapObj, index, type]
     * @Return      : java.util.Map<java.lang.String,java.lang.Object>
     * @Date        : Created in 2019/12/16 19:56
     **/
    public static Map<String, Object> addData(Map<String, Object> mapObj, String index, String type) {
        return addData(mapObj, index, type, UUID.randomUUID().toString().replaceAll("-","").toUpperCase());
    }


    /**
     * @Author      : pyt
     * @Description : 通过id删除数据
     * @Params:     : [index, type, id]
     * @Return      : java.util.Map<java.lang.String,java.lang.Object>
     * @Date        : Created in 2019/12/16 20:00
     **/
    public static Map<String, Object> deletaDataById(String index, String type, String id) {
        DeleteResponse deleteResponse = client.prepareDelete(index, type, id).execute().actionGet();
        if ("OK".equals(deleteResponse.status().toString().toUpperCase())) {
            resultMap.put("code", StatusEnum.OPRATION_SUCCESS.getCode());
            resultMap.put("msg", StatusEnum.OPRATION_SUCCESS.getMsg());
        } else {
            resultMap.put("code", StatusEnum.OPRATION_FAILED.getCode());
            resultMap.put("msg", StatusEnum.OPRATION_FAILED.getMsg());
        }
        return resultMap;
    }

    /**
     * @Author      : pyt
     * @Description : 通过id修改数据
     * @Params:     : [mapObj, index, type, id]
     * @Return      : java.util.Map<java.lang.String,java.lang.Object>
     * @Date        : Created in 2019/12/16 20:09
     **/
    public static Map<String, Object> updateDataById(Map<String, Object> mapObj, String index, String type, String id) {
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.index(index).type(type).id(id).doc(mapObj);
        ActionFuture<UpdateResponse> update = client.update(updateRequest);
        if ("OK".equals(update.actionGet().status().toString().toUpperCase())) {
            resultMap.put("code", StatusEnum.OPRATION_SUCCESS.getCode());
            resultMap.put("msg", StatusEnum.OPRATION_SUCCESS.getMsg());
        } else {
            resultMap.put("code", StatusEnum.OPRATION_FAILED.getCode());
            resultMap.put("msg", StatusEnum.OPRATION_FAILED.getMsg());
        }
        return resultMap;
    }

    /**
     * @Author      : pyt
     * @Description : 通过id查询数据 fields显示所需字段 null为所有
     * @Params:     : [index, type, id, fields]
     * @Return      : java.util.Map<java.lang.String,java.lang.Object>
     * @Date        : Created in 2019/12/16 20:15
     **/
    public static Map<String, Object> selectDataById(String index, String type, String id, String fields) {
        GetRequestBuilder getRequestBuilder = client.prepareGet(index, type, id);
        if (null != fields && !"".equals(fields)) {
            getRequestBuilder.setFetchSource(fields.split(","), null);
        }
        GetResponse documentFields = getRequestBuilder.execute().actionGet();
        return documentFields.getSource();
    }
    /**
     * @Author      : pyt
     * @Description : 分词全文检索
     * @Params:     : [index, type, query, size, fields, sourField, highlighField]
     * @Return      : java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     * @Date        : Created in 2019/12/16 20:31
     **/
    public static List<Map<String, Object>> selectAllData(String index, String type, QueryBuilder query, Integer size,
                                                          String fields, String sourField, String highlightField) {
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(index);
        if (null != type && !"".equals(type)) {
            searchRequestBuilder.setTypes(type.split(","));
        }
        if (null != highlightField && !"".equals(highlightField)) {
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field(highlightField);
            searchRequestBuilder.highlighter(highlightBuilder);
        }
        searchRequestBuilder.setQuery(query);
        //判断需要显示的字段不为null处理
        if (null != fields && !"".equals(fields)) {
            searchRequestBuilder.setFetchSource(fields.split(","),null);
        }
        if (null != sourField && !"".equals(sourField)) {
            searchRequestBuilder.addSort(sourField, SortOrder.DESC);
        }
        if (null != size && 0 < size) {
            searchRequestBuilder.setSize(size);
        }

        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();

//        if ("OK".equals(searchResponse.status().toString().toUpperCase())) {
//
//        }
        if (searchResponse.status().getStatus() == 200) {
            //setSelectResponse查询返回结果集
            return setSelectResponse(searchResponse, highlightField);
        }
        return null;
    }

    /**
     * @Author      : pyt
     * @Description : 处理高亮显示
     * @Params:     : [searchResponse, highlightField]
     * @Return      : java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     * @Date        : Created in 2019/12/16 21:05
     **/
    public static List<Map<String, Object>> setSelectResponse(SearchResponse searchResponse, String highlightField) {
        List<Map<String, Object>> sourceList = new ArrayList<Map<String, Object>>();
        StringBuffer stringBuffer = new StringBuffer();
        for (SearchHit searchHit : searchResponse.getHits().getHits()) {
            searchHit.getSourceAsMap().put("id",searchHit.getId());
            if (null != highlightField && !"".equals(highlightField)) {
                Text[] texts = searchHit.getHighlightFields().get(highlightField).getFragments();
                if (texts != null) {
                    for (Text str : texts) {
                        stringBuffer.append(str.toString());
                    }
                    searchHit.getSourceAsMap().put(highlightField, stringBuffer.toString());
                }
            }
            sourceList.add(searchHit.getSourceAsMap());
        }
        return sourceList;
    }

    /**
     * @Author     : pyt
     * @Description: 判断存在
     * @Params:    : [index]
     * @Return     : boolean
     * @Date       : Created in 2019/12/16 19:03
     **/
    public static boolean isIndexExist(String index) {
        IndicesExistsResponse indicesExistsResponse = client.admin().indices().exists(new IndicesExistsRequest(index)).actionGet();
        return indicesExistsResponse.isExists();
    }

    public static boolean isTypeExist(String index, String type) {
        return isIndexExist(index) ? client.admin().indices().prepareTypesExists(type).execute().actionGet().isExists():false;
    }

}