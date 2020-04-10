package com.mason.stage_one.chapter_one.section_three.concurrent;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by WM on 2020/4/4
 */
public class HashMapDemo {

    public static void main(String[] args) {
        HashMap<Integer, Integer> map = new HashMap<>();
        map.put(null, null);
        ConcurrentHashMap<Integer, Integer> concurrentMap = new ConcurrentHashMap<>();
        concurrentMap.put(1, 1);
        System.out.println(concurrentMap.size());
        Map<Integer, Integer> lockMap = Collections.synchronizedMap(map);
        lockMap.put(null, null);
        System.out.println(lockMap.size());
    }
}
