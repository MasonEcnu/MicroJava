package com.mason.stage_one.chapter_one.section_three;

import com.alibaba.fastjson.JSON;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by WM on 2020/4/10
 */
@SpringBootApplication
@RestController
public class WebsiteDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebsiteDemoApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format("Hello %s!", name);
    }

    @GetMapping("/userInfo")
    public String userInfo(@RequestParam(value = "userId", defaultValue = "Mason") String userId) {
        Map<String, Object> map = new HashMap<String, Object>() {{
            put("name", userId);
            put("age", 22);
            put("sex", "男");
        }};
        return JSON.toJSONString(map);
    }

    @GetMapping("/integral")
    public String integral(@RequestParam(value = "userId", defaultValue = "Mason") String userId) {
        Map<String, Object> map = new HashMap<String, Object>() {{
            put("name", userId);
            put("integral", 10000);
        }};
        return JSON.toJSONString(map);
    }
}
