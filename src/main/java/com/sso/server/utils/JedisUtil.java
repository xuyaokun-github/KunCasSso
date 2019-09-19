package com.sso.server.utils;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.PreDestroy;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 用Jedis操作自定义对象
 *
 * Created by xuyaokun On 2019/8/16 0:07
 * @desc:
 */
public class JedisUtil {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private static JedisPool pool;
    private volatile static JedisUtil instance;

    private static final int timeout=5000;
    private static final int database=0;
    private static final int maxConnection=500;
    private static final int maxIdle=50;
    private static final int minIdle=20;

    public static JedisUtil getInstance(){
        return instance;
    }

    public static void  init(String host, int port, String password) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxConnection);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setMaxWaitMillis(1000);
        config.setTestOnBorrow(true);
        pool = new JedisPool(config, host,port,timeout, password, database);
        instance = new JedisUtil();
    }

    /**
     * 获取jedis
     * @return
     */
    public Jedis getJedis() {
        return pool.getResource();
    }


    @PreDestroy
    public  void  destroy() {
        if(pool == null) {
            return;
        }

        pool.destroy();
        log.info("-----jedis pool destroy-----");
    }


    /**
     * 自定义对象存入redis
     * @param key
     * @param v
     * @param expireSeconds
     * @param <T>
     * @return
     */
    public <T> Boolean setExByte(String key,T v,int expireSeconds)
    {
        Jedis jedis=null;
        try {
            jedis= getJedis();

            if (expireSeconds == 0){
                jedis.set(key.getBytes(),serialize(v));
            }else {
                jedis.setex(key.getBytes(),expireSeconds,serialize(v));
            }

            return true;

        }catch (Exception ex) {
            log.error("自定义对象存入redis异常:{}",ex);

        }finally {
            if(jedis!=null)
            {
                jedis.close();
            }
        }

        return  false;
    }


    /**
     * 获取Value 为byte[]的自定义对象
     * @param key
     * @param <T>
     * @return
     */
    public <T> T getByteObject(String key) {
        Jedis jedis=null;
        try {
            jedis= getJedis();
            byte[] bytes= jedis.get(key.getBytes());
            return  (T)deserialize(bytes);

        }catch (Exception ex) {
            log.error("redis获取自定义对象异常:{}",ex);

        }finally {
            if(jedis!=null)
            {
                jedis.close();
            }
        }

        return  null;
    }


    /**
     * 对象以JSON存入redis
     * @param key
     * @param obj
     * @param expireSeconds
     * @param <T>
     * @return
     */
    public <T> Boolean setExJson(String key,T obj,int expireSeconds)
    {
        Jedis jedis=null;
        try
        {
            jedis= getJedis();
            jedis.setex(key,expireSeconds, JSON.toJSONString(obj));
            return true;

        }catch (Exception ex)
        {
            log.error("自定义对象存入redis异常:{}",ex);
        }finally {
            if(jedis!=null)
            {
                jedis.close();
            }
        }

        return  false;
    }


    /**
     * 获取Value 为Json的自定义对象
     * @param key
     * @param <T>
     * @return
     */
    public <T extends  Object> T getJsonObject(String key, Class<T> clazz)
    {
        Jedis jedis=null;
        try {
            jedis= getJedis();
            String jsonString= jedis.get(key);
            return JSON.parseObject(jsonString, clazz);

        }catch (RuntimeException ex) {
            log.error("redis获取Json自定义对象异常:{}",ex);

        } finally {
            if(jedis!=null) {
                jedis.close();
            }
        }

        return null;
    }

    /**
     * 序列化
     *
     * @param object
     * @return
     */
    public static byte[] serialize(Object object) {
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            byte[] bytes = baos.toByteArray();
            return bytes;
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 反序列化
     *
     * @param bytes
     * @return
     */
    public static Object deserialize(byte[] bytes) {
        ByteArrayInputStream bais = null;
        try {
            bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (Exception e) {

        }
        return null;
    }


}