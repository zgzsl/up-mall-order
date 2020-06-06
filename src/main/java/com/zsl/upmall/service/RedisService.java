package com.zsl.upmall.service;

import java.util.List;
import java.util.Map;

/**
 * redis操作Service,
 * 对象和数组都以json形式进行存储
 */
public interface RedisService {
    /**
     * 存储数据
     */
    void set(String key, String value);

    /**
     * 获取数据
     */
    String get(String key);

    /**
     * 设置超期时间
     */
    boolean expire(String key, long expire);

    boolean exists(String key);

    /**
     * 删除数据
     */
    void remove(String key);

    /**
     * 自增操作
     * @param delta 自增步长
     */
    Long increment(String key, long delta);


    void hset(String key, String field, Object value);

    /**
     * 实现命令：HGET key field，返回哈希表 key中给定域 field的值
     *
     * @param key
     * @param field
     * @return
     */
    String hget(String key, String field) ;

    void zSet(String key,String value,double score);

    void zUpdateScore(String key,String value,double score);

    void removeZset(String key,String value);
    /**
     * 实现命令：HDEL key field [field ...]，删除哈希表 key 中的一个或多个指定域，不存在的域将被忽略。
     *
     * @param key
     * @param fields
     */
    void hdel(String key, Object... fields);

    /**
     * 实现命令：HGETALL key，返回哈希表 key中，所有的域和值。
     *
     * @param key
     * @return
     */
    Map<Object, Object> hgetall(String key);

    /**
     * 获取所有列表
     * @param key
     * @return
     */
    List<String> lgetall(String key);

    // List（列表）
    /**
     * 实现命令：LPUSH key value，将一个值 value插入到列表 key的表头
     *
     * @param key
     * @param value
     * @return 执行 LPUSH命令后，列表的长度。
     */
    long lpush(String key, String value);

    Long lpushList(String key, List<String> values);

    /**
     * 实现命令：LPOP key，移除并返回列表 key的头元素。
     *
     * @param key
     * @return 列表key的头元素。
     */
    String lpop(String key);

    String rpop(String key);

    /**
     * 实现命令：RPUSH key value，将一个值 value插入到列表 key的表尾(最右边)。
     *
     * @param key
     * @param value
     * @return 执行 LPUSH命令后，列表的长度。
     */
    long rpush(String key, String value);

    long lsize(String string);

    String lindex(String key,Long index);

}
