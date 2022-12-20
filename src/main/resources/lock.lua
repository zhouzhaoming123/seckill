-- 这里的 KEYS[1] 指锁的key ，  ARGV[1] 就是当前线程标识
-- 获取锁中的标识，判断是否与当前线程标识一致
if redis.call("get",KEYS[1])==ARGV[1] then
    -- 一致，删除锁
    return redis.call("del",KEYS[1])
else
    return 0
end