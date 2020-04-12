package com.mason.stage_one.chapter_one.section_three.fork.servive;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.concurrent.*;

/**
 * Created by WM on 2020/4/11
 * 用ForkJoin方式
 * 实现多接口并发调用
 * 并组装最终结果
 */
@Service
public class UserServiceForkJoin {

    // 本质是一个线程池，默认线程数量为CPU核数
    private final ForkJoinPool forkJoinPool = new ForkJoinPool
            (Runtime.getRuntime().availableProcessors(),
                    ForkJoinPool.defaultForkJoinWorkerThreadFactory,
                    null, true);

    private final ArrayList<String> urls = new ArrayList<>();

    private final RestTemplate restTemplate;

    @Autowired
    public UserServiceForkJoin(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * 查询多个系统的数据，合并返回
     */
    public Object getUserInfo(String userId) throws ExecutionException, InterruptedException {
        urls.add("http://localhost:10101/userInfo?userId=" + userId + "1");
        urls.add("http://localhost:10101/integral?userId=" + userId + "2");
        urls.add("http://localhost:10101/userInfo?userId=" + userId + "3");
        urls.add("http://localhost:10101/integral?userId=" + userId + "4");
        urls.add("http://localhost:10101/userInfo?userId=" + userId + "5");
        urls.add("http://localhost:10101/integral?userId=" + userId + "6");
        urls.add("http://localhost:10101/userInfo?userId=" + userId + "7");
        urls.add("http://localhost:10101/integral?userId=" + userId + "8");
        urls.add("http://localhost:10101/userInfo?userId=" + userId + "9");
        urls.add("http://localhost:10101/integral?userId=" + userId + "10");

        if (urls.isEmpty()) return null;
        HttpJsonRequest request = new HttpJsonRequest(restTemplate, urls, 0, urls.size());
        ForkJoinTask<JSONObject> forkJoinTask = forkJoinPool.submit(request);
        return forkJoinTask.get();
    }

    private final static CopyOnWriteArrayList<HttpJsonRequest> requests = new CopyOnWriteArrayList<>();
    private final static ConcurrentHashMap<Integer, Integer> records = new ConcurrentHashMap<>();

    // 任务
    class HttpJsonRequest extends RecursiveTask<JSONObject> {

        private ArrayList<String> urls;
        private RestTemplate restTemplate;
        private int start;
        private int end;
        private final static int SPLIT_COUNT = 20;

        HttpJsonRequest(RestTemplate restTemplate, ArrayList<String> urls, int start, int end) {
            this.restTemplate = restTemplate;
            this.urls = urls;
            this.start = start;
            this.end = end;
        }

        /**
         * 任务拆分
         *
         * @return JSONObject
         */
        @Override
        protected JSONObject compute() {
            // 当前task需要处理多少数据
            int count = end - start;
            // 如果只有一个接口调用，直接调用
            if (count == 1) {
                if (!records.contains(start)) {
                    records.put(start, 0);
                    String url = urls.get(start);
                    long userInfoTime = System.currentTimeMillis();
                    String value = restTemplate.getForObject(url, String.class);
                    JSONObject userInfo = JSONObject.parseObject(value);
                    System.out.println(Thread.currentThread().getName() + "接口调用完毕，耗时：" + (System.currentTimeMillis() - userInfoTime) + "ms，" + url);
                    return userInfo;
                } else {
                    return null;
                }

            } else {
                // 将start-->end之间，拆分成SPLIT_COUNT份
                // 每组几个元素
                int eleEachGroup = (end - start) / SPLIT_COUNT;
                if (eleEachGroup < 1) {
                    for (int i = start; i < end; i++) {
                        requests.add(new HttpJsonRequest(restTemplate, urls, i, i + 1));
                    }
                } else {
                    for (int i = 0; i < SPLIT_COUNT; i++) {
                        int midStart = start + i * eleEachGroup;
                        int midEnd = midStart + eleEachGroup;
                        if (i == SPLIT_COUNT - 1) {
                            midEnd = end;
                        }
                        int diff = midEnd - midStart;
                        if (diff == 1) {
                            requests.add(new HttpJsonRequest(restTemplate, urls, midStart, midEnd));    // 负责处理
                        } else if (diff > 1) {
                            new HttpJsonRequest(restTemplate, urls, midStart, midEnd).fork();
                        }
                    }
                }
                requests.forEach(ForkJoinTask::fork);

                JSONObject result = new JSONObject();
                requests.forEach(request -> {
                    JSONObject response = request.join();
                    if (response != null) {
                        result.putAll(response);
                    }
                });
                return result;
            }
        }

    }

    public static void main(String[] args) {
        ArrayList<String> urls = new ArrayList<>();
        urls.add("http://localhost:10101/userInfo?userId=" + 1);
        urls.add("http://localhost:10101/integral?userId=" + 2);
        urls.add("http://localhost:10101/userInfo?userId=" + 3);
        urls.add("http://localhost:10101/integral?userId=" + 4);
        urls.add("http://localhost:10101/userInfo?userId=" + 5);
        urls.add("http://localhost:10101/integral?userId=" + 6);
        urls.add("http://localhost:10101/userInfo?userId=" + 7);
        urls.add("http://localhost:10101/integral?userId=" + 8);
        urls.add("http://localhost:10101/userInfo?userId=" + 9);
        urls.add("http://localhost:10101/integral?userId=" + 10);

        split(urls, 0, urls.size());
    }

    private static void split(ArrayList<String> urls, int start, int end) {
        int groupCount = 3;
        // 每组几个元素
        int eleEachGroup = (end - start) / groupCount;
        if (eleEachGroup == 0) {
            System.out.println("最终结果：");
            for (int i = start; i < end; i++) {
                System.out.println(i + "-->" + (i + 1));
            }
            return;
        } else {
            for (int i = 0; i < groupCount; i++) {
                int midStart = start + i * eleEachGroup;
                int midEnd = midStart + eleEachGroup;
                if (i == groupCount - 1) {
                    midEnd = end;
                }
                System.out.println("\n中间值");
                System.out.println(midStart + "-->" + midEnd);
                split(urls, midStart, midEnd);
            }
        }
    }
}
