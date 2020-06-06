package com.zsl.upmall.service.impl;

import com.zsl.upmall.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * redis操作Service的实现类
*/

@Service
public class RedisServiceImpl implements RedisService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void set(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    @Override
    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    @Override
    public boolean expire(String key, long expire) {
        return stringRedisTemplate.expire(key, expire, TimeUnit.SECONDS);
    }

    @Override
    public boolean exists(String key) {
        return stringRedisTemplate.hasKey(key);
    }

    @Override
    public void remove(String key) {
        stringRedisTemplate.delete(key);
    }



    @Override
    public Long increment(String key, long delta) {
        return stringRedisTemplate.opsForValue().increment(key,delta);
    }

    // Hash（哈希表）
    /**
     * 实现命令：HSET key field value，将哈希表 key中的域 field的值设为 value
     *
     * @param key
     * @param field
     * @param value
     */
    @Override
    public void hset(String key, String field, Object value) {
        stringRedisTemplate.opsForHash().put(key, field, value);
    }

    /**
     * 实现命令：HGET key field，返回哈希表 key中给定域 field的值
     *
     * @param key
     * @param field
     * @return
     */
    @Override
    public String hget(String key, String field) {
        Object obj = stringRedisTemplate.opsForHash().get(key, field);
        return String.valueOf(obj);
    }

    @Override
    public void zSet(String key,String value,double score){
        stringRedisTemplate.opsForZSet().add(key,value,score);
    }

    @Override
    public void removeZset(String key,String value){
        stringRedisTemplate.opsForZSet().remove(key,value);
    }


    @Override
    public void zUpdateScore(String key,String value,double score){
        stringRedisTemplate.opsForZSet().add(key,value,score);
    }


    /**
     * 实现命令：HDEL key field [field ...]，删除哈希表 key 中的一个或多个指定域，不存在的域将被忽略。
     *
     * @param key
     * @param fields
     */
    @Override
    public void hdel(String key, Object... fields) {
        stringRedisTemplate.opsForHash().delete(key, fields);
    }

    /**
     * 实现命令：HGETALL key，返回哈希表 key中，所有的域和值。
     *
     * @param key
     * @return
     */
    @Override
    public Map<Object, Object> hgetall(String key) {
        return stringRedisTemplate.opsForHash().entries(key);
    }

    @Override
    public List<String> lgetall(String key){
        return stringRedisTemplate.opsForList().range(key,0,-1);
    }

    // List（列表）
    /**
     * 实现命令：LPUSH key value，将一个值 value插入到列表 key的表头
     *
     * @param key
     * @param value
     * @return 执行 LPUSH命令后，列表的长度。
     */
    @Override
    public long lpush(String key, String value) {
        return stringRedisTemplate.opsForList().leftPush(key, value);
    }

    @Override
    public Long lpushList(String key, List<String> values) {
        return stringRedisTemplate.opsForList().leftPushAll(key, values);
    }

    /**
     * 实现命令：LPOP key，移除并返回列表 key的头元素。
     *
     * @param key
     * @return 列表key的头元素。
     */
    @Override
    public String lpop(String key) {
        return stringRedisTemplate.opsForList().leftPop(key);
    }

    @Override
    public String rpop(String key) {
        return stringRedisTemplate.opsForList().rightPop(key);
    }

    /**
     * 实现命令：RPUSH key value，将一个值 value插入到列表 key的表尾(最右边)。
     *
     * @param key
     * @param value
     * @return 执行 LPUSH命令后，列表的长度。
     */
    @Override
    public long rpush(String key, String value) {
        return stringRedisTemplate.opsForList().rightPush(key, value);
    }

    @Override
    public long lsize(String string) {
        return stringRedisTemplate.opsForList().size(string);
    }

    @Override
    public String lindex(String key,Long index) {
        return stringRedisTemplate.opsForList().index(key, index);
    }

}
