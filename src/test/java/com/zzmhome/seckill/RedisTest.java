package com.zzmhome.seckill;

import com.zzmhome.seckill.pojo.Shop;
import com.zzmhome.seckill.utils.RedisIdWorker;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.BitFieldSubCommands;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * Data:
 * Author: zhouzm
 * ---------------------------
 */
@SpringBootTest
public class RedisTest {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisIdWorker redisIdWorker;

    private ExecutorService es = Executors.newFixedThreadPool(500);

    @Test
    public void testNextId() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(300);
        Runnable task = () -> {
            for (int i = 0; i < 100; i++) {
                long orderId = redisIdWorker.nextId("order");
                System.out.println("orderId = " + orderId);
            }
            latch.countDown();
        };
        long begin = System.currentTimeMillis();
        for (int i = 0; i < 300; i++) {
            es.submit(task);
        }

        latch.await();
        long end = System.currentTimeMillis();
        System.out.println("time = " + (end - begin));
    }

    @Test
    public void test01(){
        redisTemplate.opsForValue().set("name","zzm");
        String name = String.valueOf(redisTemplate.opsForValue().get("name"));
        System.out.println(name);
    }

    @Test
    public void test02(){
        stringRedisTemplate.opsForValue().set("love", "zyq");
        String love = stringRedisTemplate.opsForValue().get("love");
        System.out.println(love);
    }
    
    @Test
    public void loadShopData(){
        //1.查看店铺信息
        List<Shop> shopList = new ArrayList<>();
        Shop shop1 = new Shop(1L,"北京",1L,116.407526,39.904030);
        Shop shop2 = new Shop(2L,"东善桥社区居委会",1L,118.776910,31.868450);
        Shop shop3 = new Shop(3L,"胜太社区居委会",1L,118.830870,31.936940);
        shopList.add(shop1);
        shopList.add(shop2);
        shopList.add(shop3);
        //2.把店铺分组，按照typeId分组，typeId一致的分到相同的组
        Map<Long, List<Shop>> map = shopList.stream().collect(Collectors.groupingBy(Shop::getTypeId));

        //3.分批完成写入Redis
        for (Map.Entry<Long, List<Shop>> entry : map.entrySet()) {
            //3.1.获取类型id
            Long typeId = entry.getKey();
            String key = "shop:seo:" + typeId;
            //3.2.获取同类型店铺的集合
            List<Shop> value = entry.getValue();
            List<RedisGeoCommands.GeoLocation<String>> locations = new ArrayList<>(shopList.size());
            //3.3.写入redis GEOADD key 经度 纬度 member
            for (Shop shop : shopList) {
                locations.add(new RedisGeoCommands.GeoLocation<>(shop.getId().toString(),
                        new Point(shop.getX(), shop.getY())));
            }
            stringRedisTemplate.opsForGeo().add(key, locations);
        }
    }

    /**
     * 签到
     */
    @Test
    public void testSign(){
        //1.获取当前登录用户
        Long userId = 1L;
        //2.获取日期
        LocalDateTime now = LocalDateTime.now();
        //3.拼接key
        String format = now.format(DateTimeFormatter.ofPattern(":yyyyMM"));
        String key = "sign:" + userId + format;
        //4.获取今天是本月的第几天
        int dayOfMonth = now.getDayOfMonth();
        //5.写入Redis SETBIT key offset 1
        stringRedisTemplate.opsForValue().setBit(key,dayOfMonth,true);

    }

    /**
     * 签到统计
     */
    @Test
    public void testSignCount(){
        //1.获取当前登录用户
        Long userId = 1L;
        //2.获取日期
        LocalDateTime now = LocalDateTime.now();
        //3.拼接key
        String format = now.format(DateTimeFormatter.ofPattern(":yyyyMM"));
        String key = "sign:" + userId + format;
        //4.获取今天是本月的第几天
        int dayOfMonth = now.getDayOfMonth();
        //5.获取本月截止今天所有的签到记录，返回一个十进制的数据 BITFIELD key GET u19 0
        List<Long> result = stringRedisTemplate.opsForValue().bitField(key,
                BitFieldSubCommands.create()
                        .get(BitFieldSubCommands.BitFieldType.unsigned(dayOfMonth)).valueAt(0));
        if (CollectionUtils.isEmpty(result)){
            return;
        }
        Long num = result.get(0);
        if (num == null || num == 0){
            return;
        }
        //6.循环遍历
        int count = 0;
        while (true){
            //6.1 让这个数字和1做运算，得到数字的最后一位bit位  判断这个bit为是否为0
            if ((num & 1) == 0){
                //如果为0，说明未签到 ，结束
                break;
            }else {
                //如果不为0，说明已签到，计数器+1
                count ++;
            }
            //把数字右移一位，抛弃最后一位bit位，继续下一位bit位
            num >>>= 1;
        }
        System.out.println(count);
    }

    /**
     * UA 数量统计
     */
    @Test
    public void testHyperLogLog(){
        String[] strings = new String[1000];
        int j = 0;
        for (int i = 0; i < 1000000; i++) {
            j = i % 1000;
            strings[j] = "user_" + i;
            if (j == 999){
                //发送到redis
                stringRedisTemplate.opsForHyperLogLog().add("hl2",strings);
            }
        }
        //统计数量
        Long size = stringRedisTemplate.opsForHyperLogLog().size("hl2");
        System.out.println("count: " + size);

    }

}
