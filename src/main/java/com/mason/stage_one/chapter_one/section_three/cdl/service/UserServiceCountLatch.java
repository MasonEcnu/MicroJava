package com.mason.stage_one.chapter_one.section_three.cdl.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by WM on 2020/4/10
 */
@Service
public class UserServiceCountLatch {

    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final CopyOnWriteArrayList<JSONObject> jsonObjects = new CopyOnWriteArrayList<>();

    private final RestTemplate restTemplate;

    @Autowired
    public UserServiceCountLatch(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * 查询多个系统的数据，合并返回
     */
    public Object getUserInfo(String userId) throws InterruptedException {
        CountDownLatch count = new CountDownLatch(2);
        String url1 = "http://localhost:10101/userInfo?userId=" + userId;
        String url2 = "http://localhost:10101/integral?userId=" + userId;

        executorService.submit(
                new CountLatchRequest(url1, count, jsonObjects, restTemplate)
        );
        executorService.submit(
                new CountLatchRequest(url2, count, jsonObjects, restTemplate)
        );

        count.await();
        JSONObject result = new JSONObject();
        jsonObjects.forEach(jsonObject -> {
            if (jsonObject != null) {
                result.putAll(jsonObject);
            }
        });
        return result;
    }

    class CountLatchRequest implements Runnable {

        private CountDownLatch count;
        private CopyOnWriteArrayList<JSONObject> jsonObjects;
        private String url;
        private RestTemplate restTemplate;

        public CountLatchRequest(String url, CountDownLatch count, CopyOnWriteArrayList<JSONObject> jsonObjects, RestTemplate restTemplate) {
            this.url = url;
            this.count = count;
            this.jsonObjects = jsonObjects;
            this.restTemplate = restTemplate;
        }

        @Override
        public void run() {
            long integralApiTime = System.currentTimeMillis();
            String integral = restTemplate.getForObject(url, String.class);
            JSONObject info = JSONObject.parseObject(integral);
            int start = url.lastIndexOf("/");
            int end = url.lastIndexOf("?");
            String apiName = url.substring(start + 1, end);
            System.out.println(apiName + "-api积分接口调用时间为：" + (System.currentTimeMillis() - integralApiTime) + "ms");
            jsonObjects.add(info);
            count.countDown();
        }
    }
}
