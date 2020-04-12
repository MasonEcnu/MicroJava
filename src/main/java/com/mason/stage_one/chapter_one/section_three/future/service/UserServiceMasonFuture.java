package com.mason.stage_one.chapter_one.section_three.future.service;

import com.alibaba.fastjson.JSONObject;
import com.mason.stage_one.chapter_one.section_three.future.MasonFutureTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Callable;

/**
 * Created by WM on 2020/4/10
 */
@Service
public class UserServiceMasonFuture {

    private final RestTemplate restTemplate;

    @Autowired
    public UserServiceMasonFuture(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * 查询多个系统的数据，合并返回
     */
    public Object getUserInfo(String userId) throws Exception {
        Callable<JSONObject> userInfoCallable = () -> {
            long userInfoTime = System.currentTimeMillis();
            String value = restTemplate.getForObject("http://localhost:10101/userInfo?userId=" + userId, String.class);
            JSONObject userInfo = JSONObject.parseObject(value);
            System.out.println("userInfo-api用户基本信息接口调用时间为：" + (System.currentTimeMillis() - userInfoTime) + "ms");
            return userInfo;
        };

        Callable<JSONObject> integralInfoCallable = () -> {
            long integralApiTime = System.currentTimeMillis();
            String integral = restTemplate.getForObject("http://localhost:10101/integral?userId=" + userId, String.class);
            JSONObject integralInfo = JSONObject.parseObject(integral);
            System.out.println("integral-api积分接口调用时间为：" + (System.currentTimeMillis() - integralApiTime) + "ms");
            return integralInfo;
        };

        MasonFutureTask<JSONObject> userInfoFuture = new MasonFutureTask<>(userInfoCallable);
        MasonFutureTask<JSONObject> integralInfoFuture = new MasonFutureTask<>(integralInfoCallable);
        new Thread(userInfoFuture).start();
        new Thread(integralInfoFuture).start();

        // 4.合并为一个json对象
        JSONObject result = new JSONObject();
        result.putAll(userInfoFuture.get());
        result.putAll(integralInfoFuture.get());

        return result;
    }

}
