# seckill

# redis本地启动
    cdm    cd C:\redis     redis-server.exe redis.windows.conf

# 项目框架搭建
    1.StringBoot环境搭建
    2.集成Thymeleaf,RespBean
    3.MyBatisPlus
# 分布式会话
    1.用户登录
        a.设计数据库（不与正常业务共用表，单独设计秒杀订单表，商品表）
        b.明文密码MD5二次加密 （MD5Util）
        c.参数校验，全局异常处理 （IsMobile，RespBean）
    2.共享session
        a.StringSession       
        b.Redis
# 商品开发
    1.商品列表
    2.商品详情
    3.秒杀
    4.订单详情
# 系统压测
    1.Jmeter
    2.自定义变量模拟多用户
    3.Jmeter命令行的使用
    4.正式压测
        a.商品列表
        b.秒杀
# 页面优化
    1.页面缓存-URl缓存-对象缓存
    2.页面静态化，前后端分离
    3.静态资源优化
    4.CDN优化
# 接口优化
    1.Redis预减库存减少数据库访问（opsForValue.decrement）
    2.内存标记减少Redis访问 （afterPropertiesSet）
    3.RabbiteMQ异步下单
       a.StringBoot整合RabbiteMQ
       b.交换机
# 安全优化
    1.秒杀接口地址隐藏  
    2.算数验证码 （ArithmeticCaptcha）
    3.接口防刷  （AccessLimitInterceptor）  
    4.注解切面（@AccessLimit(second = 5,maxCount = 5,needLogin = true)） 
    5.全局配置切面（WebConfig.addInterceptors）                         