package com.kunghsu.config;

import com.kunghsu.utils.RedissonUtil;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.SerializationCodec;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 *
 * Created by xuyaokun On 2019/8/15 21:43
 * @desc:
 */
@Configuration
public class RedissonConfig {


    @Value("${redisson.url}")
    private String url;
    @Value("${redisson.cluster.urls}")
    private String clusterUrls;
    @Value("${redisson.password}")
    private String password;

    /**
     * 单实例模式
     *
     * @return
     * @throws IOException
     */
    @Bean(name = "redissonClient")
    public RedissonClient redissonClientSingle() throws IOException {
        RedissonClient redisson = null;
        Config config = new Config();
        config.useSingleServer().setAddress("redis://" + url);
        //去掉默认的序列化Jackson,改用JDK序列化
        config.setCodec(new SerializationCodec());
        redisson = Redisson.create(config);
        //检测是否配置成功
        System.out.println("Redisson初始化结果：" + redisson.getConfig().toJSON().toString());
        RedissonUtil.init(redisson);
        return redisson;
    }

    /**
     * 集群模式
     *
     * @return
     * @throws IOException
     */
//    @Bean(name = "redissonClient")
//    public RedissonClient redissonClientCluster() throws IOException {
//        String[] nodes = clusterUrls.split(",");
//        //redisson版本是3.5，集群的ip前面要加上“redis://”，不然会报错，3.2版本可不加
//        for (int i = 0; i < nodes.length; i++) {
//            nodes[i] = "redis://" + nodes[i];
//        }
//        RedissonClient redisson = null;
//        Config config = new Config();
//        ClusterServersConfig clusterServersConfig = config.useClusterServers() //集群模式
//                .setScanInterval(5000) //设置集群状态扫描时间
//                .addNodeAddress(nodes);
//        if (!StringUtils.isEmpty(password)){
//            clusterServersConfig.setPassword(password);
//        }
//        redisson = Redisson.create(config);
//        //检测是否配置成功
//        System.out.println("Redisson初始化结果：" + redisson.getConfig().toJSON().toString());
//        RedissonUtil.init(redisson);
//        return redisson;
//    }


}
