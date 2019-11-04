package com.sso.server.utils;

import org.redisson.api.*;

import java.util.concurrent.TimeUnit;

/**
 * Created by xuyaokun On 2019/8/15 21:47
 * @desc: 
 */
public class RedissonUtil {

    private static RedissonClient redissonClient;

    public static void init(RedissonClient redisson){
        redissonClient = redisson;
    }

    public static boolean delete(String keyName){
        return redissonClient.getBucket(keyName).delete();
    }

    /**
     * 设置字符串对象
     * @param keyName
     * @param value
     * @param timeToLive 过期时间，单位秒
     */
    public static void setString(String keyName, String value, long... timeToLive) {

        RBucket<String> rBucket = getRBucket(redissonClient, keyName);
        //同步放置
        rBucket.set(value);
        // 异步放置(在当前线程起另一个线程去放置)
//		rBucket.setAsync("kung");
        if(timeToLive != null && timeToLive.length > 0){
            rBucket.expire(timeToLive[0], TimeUnit.SECONDS);
        }else{
            //清过期时间，表示永久存
            rBucket.clearExpire();
        }
    }


    public static String getString(String keyName) {

        //取一个不存在的key对应的值，会返回null，不会报错
        RBucket<String> bucket = getRBucket(redissonClient, keyName);
        return bucket.get();
    }

    /**
     * 获取字符串对象
     *
     * @param objectName
     * @return
     */
    private static <T> RBucket<T> getRBucket(RedissonClient redissonClient, String objectName) {
        RBucket<T> bucket = redissonClient.getBucket(objectName);
        return bucket;
    }

    /**
     * 设置自定义对象
     * @param keyName
     * @param value
     * @param timeToLive 过期时间，单位秒
     */
    public static <T> void setObject(String keyName, T value, long... timeToLive) {

        RBucket<T> rBucket = getRBucket(redissonClient, keyName);
        // 同步放置
        rBucket.set(value);
        // 异步放置(在当前线程起另一个线程去放置)
//		rBucket.setAsync("kung");
        setExpireTime(rBucket, timeToLive);

    }

    /**
     *
     * @return
     */
    public static <T> T getObject(String keyName) {

        //取一个不存在的key对应的值，会返回null，不会报错
        RBucket<T> bucket = getRBucket(redissonClient, keyName);
        return bucket.get();
    }


    /**
     * 设置过期时间
     * 因为几乎所有Ression的API对象，都继承了RExpirable 所以可以用父类来设置过期时间
     * @param object
     * @param timeToLive
     * @return
     */
    private static boolean setExpireTime(RExpirable object, long... timeToLive){

        boolean isExpireSuccess = false;
        if(timeToLive != null && timeToLive.length > 0){
            isExpireSuccess = object.expire(timeToLive[0], TimeUnit.SECONDS);
        }else{
            //清过期时间，表示永久存
            isExpireSuccess = object.clearExpire();
        }
        return isExpireSuccess;
    }

    public static boolean setExpireTime(String objectName, long... timeToLive){

        RExpirable object = redissonClient.getBucket(objectName);
        boolean isExpireSuccess = false;
        if(timeToLive != null && timeToLive.length > 0){
            isExpireSuccess = object.expire(timeToLive[0], TimeUnit.SECONDS);
        }else{
            //清过期时间，表示永久存
            isExpireSuccess = object.clearExpire();
        }
        return isExpireSuccess;
    }


    /**
     * 获取双端队列
     *
     * @param objectName
     * @return
     */
    public static <V> RDeque<V> getRDeque(String objectName) {
        RDeque<V> rDeque = redissonClient.getDeque(objectName);
        return rDeque;
    }

    /**
     * 获取锁
     *
     * @param objectName
     * @return
     */
    public static RLock getRLock(String objectName) {
        RLock rLock = redissonClient.getLock(objectName);
        return rLock;
    }


    /** ================================下面部分还没做封装优化工作==========================================*/

    /**
     * 获取Map对象
     *
     * @param objectName
     * @return
     */
    public static <K, V> RMap<K, V> getRMap(String objectName) {
        RMap<K, V> map = redissonClient.getMap(objectName);
        return map;
    }

    /**
     * 获取有序集合
     *
     * @param objectName
     * @return
     */
    public static <V> RSortedSet<V> getRSortedSet(String objectName) {
        RSortedSet<V> sortedSet = redissonClient.getSortedSet(objectName);
        return sortedSet;
    }

    /**
     * 获取集合
     *
     * @param objectName
     * @return
     */
    public static <V> RSet<V> getRSet(String objectName) {
        RSet<V> rSet = redissonClient.getSet(objectName);
        return rSet;
    }

    /**
     * 获取列表
     *
     * @param objectName
     * @return
     */
    public static <V> RList<V> getRList(RedissonClient redissonClient, String objectName) {
        RList<V> rList = redissonClient.getList(objectName);
        return rList;
    }

    /**
     * 获取队列
     *
     * @param objectName
     * @return
     */
    public static <V> RQueue<V> getRQueue(String objectName) {
        RQueue<V> rQueue = redissonClient.getQueue(objectName);
        return rQueue;
    }


    /**
     * 获取原子数
     *
     * @param objectName
     * @return
     */
    public static RAtomicLong getRAtomicLong(String objectName) {
        RAtomicLong rAtomicLong = redissonClient.getAtomicLong(objectName);
        return rAtomicLong;
    }

    /**
     * 获取记数锁
     *
     * @param objectName
     * @return
     */
    public static RCountDownLatch getRCountDownLatch(String objectName) {
        RCountDownLatch rCountDownLatch = redissonClient.getCountDownLatch(objectName);
        return rCountDownLatch;
    }

    /**
     * 获取消息的Topic
     *
     * @param objectName
     * @return
     */
    public static <M> RTopic<M> getRTopic(String objectName) {
        RTopic<M> rTopic = redissonClient.getTopic(objectName);
        return rTopic;
    }
}
