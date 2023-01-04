package com.zzmhome.seckill;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.PolyNull;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Data:
 * Author: zhouzm
 * ---------------------------
 */
@Slf4j
public class CaffeineTest {

    @Test
    void testCaffeine(){
        // 构建cache对象
        Cache<Object, Object> cache = Caffeine.newBuilder().build();

        //存数据
        cache.put("gf","张碧晨");

        //取数据
        Object gf = cache.getIfPresent("gf");
        System.out.println(gf);
        
        //取数据，如果未命中，则查询数据库
        Object o1 = cache.get("newGF", o -> {
            return "刘亦菲";
        });
        System.out.println(o1);
    }

}
