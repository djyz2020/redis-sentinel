package com.nudt.sentinel.service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by haibozhang on 2018/12/15.
 */
public class FailOverSample {

    public static void main(String[] args) {
        //JedisSentinelPool Jedis的Sentinel连接池
        String nodename = "mymaster";
        Set<String> sentinels = new HashSet<>();
        sentinels.add("10.107.18.32:26379");
        sentinels.add("10.107.18.32:26380");
        sentinels.add("10.107.18.32:26381");
        JedisSentinelPool sentinelPool = new JedisSentinelPool(nodename , sentinels);
        int count = 0;
        while (true) {
            Jedis jedis = null;
            try {
                jedis = sentinelPool.getResource();
                int idx = new Random().nextInt(100000);
                jedis.set("k-" + idx, "v-" + idx);
                if (count % 100 == 0) {
                    System.out.println("k-" + idx);
                }
                count++;
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                if (jedis != null) {
                    jedis.close();
                }
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
