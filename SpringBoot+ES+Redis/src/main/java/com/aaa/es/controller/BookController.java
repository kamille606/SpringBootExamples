package com.aaa.es.controller;

import com.aaa.es.model.Book;
import com.aaa.es.service.BookService;
import com.aaa.es.service.RedisService;
import com.aaa.es.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author ：pyt
 * @date ：Created in 2019/12/17 19:07
 * @version: ${version}
 * @description：
 */
@RestController
public class BookController {

    @Autowired
    private BookService bookService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private SearchService searchService;

    @RequestMapping("/select")
    public List<Book> selectAllBooks() {
        Map<String, Object> resultMap = bookService.selectAllBooks(redisService, searchService);
        Integer code = (Integer) resultMap.get("code");
        List<Book> bookList = null;
        if (200 == code) {
            bookList = (List<Book>) resultMap.get("result");
        } else if (404 == code) {
            if (null == (List<Book>) resultMap.get("result")) {
                System.out.println("redis集群出现问题");
            } else {
                System.out.println("mysql集群出现问题");
            }
        } else {
            //TODO
        }
        return bookList;
    }

}
