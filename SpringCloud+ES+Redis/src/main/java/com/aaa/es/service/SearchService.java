package com.aaa.es.service;

import com.aaa.es.utils.ESUtil;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ：pyt
 * @date ：Created in 2019/12/17 8:51
 * @version: ${version}
 * @description：
 */
@Service
public class SearchService {

    public Map<String, Object> createIndex(String index) {
        return ESUtil.createIndex(index);
    }

    public Map<String, Object> addData(Map<String, Object> map) {
        return ESUtil.addData(map, "test_index2", "test_type1");
    }


    public List<Map<String, Object>> selectAll() {

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //查询所有
        MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
        BoolQueryBuilder must = boolQueryBuilder.must(matchAllQueryBuilder);

        return ESUtil.selectAllData("test_index2", "test_type1", must, null, null, null, null);

    }

    public List<Map<String, Object>> selectAllLike() {

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        MatchPhraseQueryBuilder matchPhraseQueryBuilder = QueryBuilders.matchPhraseQuery("username", "peng");

        BoolQueryBuilder must = boolQueryBuilder.must(matchPhraseQueryBuilder);

        return ESUtil.selectAllData("test_index2", "test_type1", must, 50, null, null, "username");

    }

    public Map<String, Object> selectOneById(String id) {
        return ESUtil.selectDataById("test_index2", "test_type1", id, null);
    }

}
