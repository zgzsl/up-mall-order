package com.zsl.upmall.util;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class RedPacket {

    public static void main(String[] args) {
      /*  List<Double> result = hb(0.26, 25, 0.01);
        System.out.println(result);// 金额，个数，最少值
        List<BigDecimal> reuslt1 = result.stream().map(item -> new BigDecimal(item).setScale(2,BigDecimal.ROUND_HALF_UP)).collect(Collectors.toList());
        System.out.println(reuslt1);// 金额，个数，最少值*/

        List<BigDecimal> reuslt1 = mathRedPacket(new BigDecimal( 0.25),25);
        System.out.println(reuslt1);
    }

    public static List<Double> hb(double total, int num, double min) {
        List<Double> result = new ArrayList<>();

        if(total - (num * 0.01) > 0){
            total = total - (num * 0.01);
        }else{
            for (int i = 0; i < num; i++) {
                result.add(0.01);
            }
            return result;
        }

        for (int i = 1; i < num; i++) {

            double safe_total = (total - (num - i) * min) / (num - i);

            double money = Math.random() * (safe_total - min) + min;

            BigDecimal money_bd = new BigDecimal(money);

            money = money_bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

            total = total - money;

            BigDecimal total_bd = new BigDecimal(total);

            total = total_bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            result.add(money);
        }
        result.add(total);

        for (int i = 0; i < num; i++) {
            result.set(i,result.get(i) + 0.01);
        }
        return result;
    }

    void zb() {
        for (int a = 0; a <= 10000; a++) {

            if (a % 1000 == 0){
                System.out.println(a);
            }
        }
    }


    /**
     * 计算每人获得红包金额;最小每人0.01元
     * @param mmm 红包总额
     * @param number 人数
     * @return
     */
    public static List<BigDecimal> mathRedPacket(BigDecimal mmm, int number) {
        if (mmm.doubleValue() < number * 0.01) {
            return null;
        }
        Random random = new Random();
        // 金钱，按分计算 10块等于 1000分
        int money = mmm.multiply(BigDecimal.valueOf(100)).intValue();
        // 随机数总额
        double count = 0;
        // 每人获得随机点数
        double[] arrRandom = new double[number];
        // 每人获得钱数
        List<BigDecimal> arrMoney = new ArrayList<BigDecimal>(number);
        // 循环人数 随机点
        for (int i = 0; i < arrRandom.length; i++) {
            int r = random.nextInt((number) * 99) + 1;
            count += r;
            arrRandom[i] = r;
        }
        // 计算每人拆红包获得金额
        int c = 0;
        for (int i = 0; i < arrRandom.length; i++) {
            // 每人获得随机数相加 计算每人占百分比
            Double x = new Double(arrRandom[i] / count);
            // 每人通过百分比获得金额
            int m = (int) Math.floor(x * money);
            // 如果获得 0 金额，则设置最小值 1分钱
            if (m == 0) {
                m = 1;
            }
            // 计算获得总额
            c += m;
            // 如果不是最后一个人则正常计算
            if (i < arrRandom.length - 1) {
                arrMoney.add(new BigDecimal(m).divide(new BigDecimal(100)));
            } else {
                // 如果是最后一个人，则把剩余的钱数给最后一个人
                arrMoney.add(new BigDecimal(money - c + m).divide(new BigDecimal(100)));
            }
        }
        // 随机打乱每人获得金额
        Collections.shuffle(arrMoney);
        return arrMoney;
    }
}
