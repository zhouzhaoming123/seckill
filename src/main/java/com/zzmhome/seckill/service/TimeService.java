package com.zzmhome.seckill.service;

import org.springframework.stereotype.Service;

/**
 * System.currentTimeMillis消耗大，每个线程进来都这样，
 * 在服务器启动的时候，开一个线程不断去拿，调用方直接获取值就好了
 * Data:
 * Author: zhouzm
 * ---------------------------
 */
@Service
public class TimeService {
    private static long time;
    static {
        new Thread(new Runnable(){
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    long cur = System.currentTimeMillis();
                    setTime(cur);
                }
            }
        }).start();
    }

    public static long getTime() {
        return time;
    }

    public static void setTime(long time) {
        TimeService.time = time;
    }
}
