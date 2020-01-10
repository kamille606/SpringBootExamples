package com.aaa.es.service;

import com.aaa.es.mapper.BookMapper;
import com.aaa.es.model.Book;
import com.aaa.es.utils.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ：pyt
 * @date ：Created in 2019/12/17 17:05
 * @version: ${version}
 * @description：
 */
@Service
public class BookService {

    @Value("${book_key}")
    public String bookKey;
    @Autowired
    private BookMapper bookMapper;

    public Map<String, Object> selectAllBooks(RedisService redisService, SearchService searchService) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        String bookString = redisService.get(bookKey);
        if (null == bookString || "".equals(bookString)) {
            List<Book> bookList = bookMapper.selectAll();
            if (bookList.size() > 0) {
                try {
                    redisService.set(bookKey, JSONUtil.toJsonString(bookList));
                    resultMap.put("code",200);
                    resultMap.put("result", bookList);
                } catch (RedisSystemException e) {
                    redisService.set(bookKey, JSONUtil.toJsonString(bookList));
                    resultMap.put("code",404);
                    resultMap.put("result", bookList);
                    e.printStackTrace();
                    return resultMap;
                }
            } else {
                resultMap.put("code",404);
            }
        } else {
            List<Book> bookList1 = JSONUtil.toList(bookString, Book.class);
            List<Book> bookList2 = bookMapper.selectAll();
            if (bookList1.size() == bookList2.size()) {
                resultMap.put("code",200);
                resultMap.put("result", bookList1);
            } else {
                redisService.set(bookKey, JSONUtil.toJsonString(bookList2));
                resultMap.put("code",200);
                resultMap.put("result", bookList2);
            }
        }
        return resultMap;
    }

}
