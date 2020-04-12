package com.mason.stage_one.chapter_one.section_three.fork;

import com.mason.stage_one.chapter_one.section_three.WebsiteDemoApplication;
import com.mason.stage_one.chapter_one.section_three.cdl.service.UserServiceCountLatch;
import com.mason.stage_one.chapter_one.section_three.fork.servive.UserService;
import com.mason.stage_one.chapter_one.section_three.fork.servive.UserServiceForkJoin;
import com.mason.stage_one.chapter_one.section_three.future.service.UserServiceFuture;
import com.mason.stage_one.chapter_one.section_three.future.service.UserServiceMasonFuture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by WM on 2020/4/10
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WebsiteDemoApplication.class)
public class TestWebsiteDemoApplication {

    @Before
    public void start() {
        System.out.println("开始测试");
    }

    @After
    public void end() {
        System.out.println("结束测试");
    }

    @Test
    public void exception() {
        int x = 0, y = 100;
        System.out.println("结果：" + (x + y));
    }

    @Autowired
    UserService userService;

    @Autowired
    UserServiceForkJoin userServiceForkJoin;

    @Autowired
    UserServiceCountLatch userServiceCountLatch;

    @Autowired
    UserServiceFuture userServiceFuture;

    @Autowired
    UserServiceMasonFuture userServiceMasonFuture;

    @Test
    public void testUserService() throws Exception {
        // 调用计时
        long start = System.currentTimeMillis();
        Object userInfo = userService.getUserInfo("mason");
        long end = System.currentTimeMillis();
        System.out.println("userService-getUserInfo总耗时：" + (end - start) + "ms");
        System.out.println(userInfo.toString());

    }

    @Test
    public void testUserServiceForkJoin() throws Exception {
        // 调用计时
        long start = System.currentTimeMillis();
        Object userInfo = userServiceForkJoin.getUserInfo("mason");
        long end = System.currentTimeMillis();
        System.out.println("userService-getUserInfo总耗时：" + (end - start) + "ms");
        System.out.println(userInfo.toString());
    }

    @Test
    public void testUserServiceCountLatch() throws Exception {
        // 调用计时
        long start = System.currentTimeMillis();
        Object userInfo = userServiceCountLatch.getUserInfo("mason");
        long end = System.currentTimeMillis();
        System.out.println("userService-getUserInfo总耗时：" + (end - start) + "ms");
        System.out.println(userInfo.toString());
    }

    @Test
    public void testUserServiceFuture() throws Exception {
        // 调用计时
        long start = System.currentTimeMillis();
        Object userInfo = userServiceFuture.getUserInfo("mason");
        long end = System.currentTimeMillis();
        System.out.println("userService-getUserInfo总耗时：" + (end - start) + "ms");
        System.out.println(userInfo.toString());
    }

    @Test
    public void testUserServiceMasonFuture() throws Exception {
        // 调用计时
        long start = System.currentTimeMillis();
        Object userInfo = userServiceMasonFuture.getUserInfo("mason");
        long end = System.currentTimeMillis();
        System.out.println("userService-getUserInfo总耗时：" + (end - start) + "ms");
        System.out.println(userInfo.toString());
    }
}
