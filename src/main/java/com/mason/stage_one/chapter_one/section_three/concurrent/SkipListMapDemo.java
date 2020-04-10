package com.mason.stage_one.chapter_one.section_three.concurrent;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Created by WM on 2020/4/6
 */
public class SkipListMapDemo {

    public static void main(String[] args) {
        ConcurrentSkipListMap<String, String> skipListMap = new ConcurrentSkipListMap<>();
        for (int i = 0; i < 5; i++) {
            String key = RandomStringUtils.randomAlphabetic(10);
            String value = RandomStringUtils.randomAlphabetic(10);
            skipListMap.put(key, value);
            System.out.println(key + "->" + value);
        }
        System.out.println("======插入完毕=====");
        skipListMap.forEach((k, v) -> System.out.println(k + "->" + v));
    }
}
