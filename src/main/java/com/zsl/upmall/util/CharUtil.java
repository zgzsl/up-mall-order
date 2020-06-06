package com.zsl.upmall.util;

import com.zsl.upmall.config.SystemConfig;
import com.zsl.upmall.entity.GrouponOrderMaster;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @ClassName CharUtil
 * @Description 拼团订单号 生成规则：单号类型1位 + 时间戳后8位 + 用户id后4位 + 随机数4位）
 * @Author binggleW
 * @Date 2020-05-14 9:56
 * @Version 1.0
 **/
public class CharUtil {
    /**
     * 取用户id长度的后2位，不足4位的前面补0至4位
     */
    private static final int USER_ID_LENGTH = 4;

    /**
     * 取时间戳后8位
     */
    private static final int TIMESTAMP_LENGTH = 8;

    /**
     * 生成四位随机数
     *
     * @return
     */
    public static String getRandomCode() {
        return String.valueOf(String.format("%04d", (int) (Math.random() * 9999)));
    }

    /**
     * 取时间戳后8位
     *
     * @return
     */
    private static String getDateTime() {
        String dateStr = String.valueOf(System.currentTimeMillis());
        return dateStr.substring(dateStr.length() - TIMESTAMP_LENGTH, dateStr.length());
    }

    /**
     * 取用户id后四位
     *
     * @param userId
     * @return
     */
    public static String getUserIdCode(Integer userId) {
        if (Objects.isNull(userId)) {
            userId = 10;
        }
        String userIdStr = String.valueOf(userId);
        if (userIdStr.length() <= USER_ID_LENGTH) {
            userIdStr = String.format("%04d", userId);
        } else {
            userIdStr = userIdStr.substring(userIdStr.length() - USER_ID_LENGTH, userIdStr.length());
        }
        return userIdStr;
    }

    /**
     * 生成拼团订单号
     * @param userId
     * @param mode
     * @return
     */
    public static String getCode(Integer userId,Integer mode) {
        if(mode - 0 == 0){
            return "P" + getDateTime() + getUserIdCode(userId) + getRandomCode();
        }else if (mode - 1 == 0){
            return "C" + getDateTime() + getUserIdCode(userId) + getRandomCode();
        }else{
            return "O" + getDateTime() + getUserIdCode(userId) + getRandomCode();
        }
    }

    /**
     * 生成拼团凭证
     * @return
     */
    public static String generateGrouponCode() {
        String[] chars = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        StringBuffer shortBuffer = new StringBuffer();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int j = 0; j < 8; j++) {
            String str = uuid.substring(j * 4, j * 4 + 4);
            int par = Integer.parseInt(str, 16);
            shortBuffer.append(chars[par % 10]);
        }
        return shortBuffer.toString();
    }

    /**
     * 生成参团凭证
     * @return
     */
    public static List<String> generateJoinGroupCode(int groupCount) {
        List<String> list = Stream.iterate(SystemConfig.BASE_VOUCHER, k -> ++k)
                .limit(groupCount)
                .map(item -> item.toString())
                .collect(Collectors.toList());
        Collections.shuffle(list);
        return list;
    }

    /**
     * 限定字符串长度，超过15个字符显示。。。
     * @param str
     * @return
     */
    public static String getString15Length(String str){
        if(str.length() - 15 > 0){
            return str.substring(0,15) + "...";
        }else{
            return str;
        }
    }

    public static void main(String[] args) {
       // System.out.println(generateGrouponCode());
       /* LocalDateTime nowDate = LocalDateTime.now();
        Date createTime = Date.from( nowDate.atZone( ZoneId.systemDefault()).toInstant());
        LocalDateTime endDate = nowDate.plusHours(24);
        Date  endTime = Date.from( endDate.atZone( ZoneId.systemDefault()).toInstant());
        System.out.println("开始时间:"+DateUtil.DateToString(createTime,"yyyy-MM-dd HH:mm:ss"));
        System.out.println("结束时间:"+DateUtil.DateToString(endTime,"yyyy-MM-dd HH:mm:ss"));*/
        /*System.out.println(generateJoinGroupCode(9));*/
        String str = "时间节点34你的号我是你把";
        System.out.println("阶段："+getString15Length(str));
    }
}
