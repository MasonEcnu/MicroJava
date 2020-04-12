package com.mason.stage_one.chapter_one.section_three.fork.servive;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Created by WM on 2020/4/10
 */
@Service
public class UserService {

    private final RestTemplate restTemplate;

    @Autowired
    public UserService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * 查询多个系统的数据，合并返回
     */
    public Object getUserInfo(String userId) {
        // 其他例子, 查数据库的多个表数据,分多次查询

        // 1.先从调用获取用户基础信息的http接口
        long userInfoTime = System.currentTimeMillis();
        String value = restTemplate.getForObject("http://localhost:10101/userInfo?userId=" + userId, String.class);
        JSONObject userInfo = JSONObject.parseObject(value);
        System.out.println("userInfo-api用户基本信息接口调用时间为：" + (System.currentTimeMillis() - userInfoTime) + "ms");

        // 2.再调用获取用户积分信息的接口
        long integralApiTime = System.currentTimeMillis();
        String integral = restTemplate.getForObject("http://localhost:10101/integral?userId=" + userId, String.class);
        JSONObject integralInfo = JSONObject.parseObject(integral);
        System.out.println("integral-api积分接口调用时间为：" + (System.currentTimeMillis() - integralApiTime) + "ms");

        // 3.调用多个接口，时间累积

        // 4.合并为一个json对象
        JSONObject result = new JSONObject();
        result.putAll(userInfo);
        result.putAll(integralInfo);

        return result;
    }

}
