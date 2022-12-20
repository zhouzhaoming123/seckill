package com.zzmhome.seckill.service;

/**
 * redis分布式锁
 * Data:
 * Author: zhouzm
 * ---------------------------
 */
public interface ILock {

    /**
     * 上锁
     * @param timeoutSec
     * @return
     */
    boolean tryLock(long timeoutSec);

    /**
     * 删除锁
     */
    void unLock();

}
