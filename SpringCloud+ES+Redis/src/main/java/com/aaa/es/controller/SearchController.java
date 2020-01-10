package com.aaa.es.controller;

import com.aaa.es.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ：pyt
 * @date ：Created in 2019/12/17 8:02
 * @version: ${version}
 * @description：
 */
@RestController
public class SearchController {

    @Autowired
    SearchService searchService;

    @RequestMapping("/createIndex")
    public Map<String, Object> createIndex() {
        String index = "test_index20";
        return searchService.createIndex(index);
    }

    @RequestMapping("/addData")
    public Map<String, Object> addData() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("username","peng2");
        map.put("password","123456");
        map.put("age",21);
        return searchService.addData(map);
    }

    @RequestMapping("/all")
    public List<Map<String, Object>> selectAll() {
        return searchService.selectAll();
    }

    @RequestMapping("/allLike")
    public List<Map<String, Object>> selectAllLike() {
        return searchService.selectAllLike();
    }

    @RequestMapping("/selectOne")
    public Map<String, Object> selectOneById(String id) {
        return searchService.selectOneById(id);
    }

}
