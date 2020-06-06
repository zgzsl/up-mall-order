package com.zsl.upmall.util;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RedPacket {

    public static void main(String[] args) {
        List<Double> result = hb(100.15, 19, 0.01);
        System.out.println(result);// 金额，个数，最少值
        List<BigDecimal> reuslt1 = result.stream().map(item -> new BigDecimal(item).setScale(2,BigDecimal.ROUND_HALF_UP)).collect(Collectors.toList());
        System.out.println(reuslt1);// 金额，个数，最少值
    }

    public static List<Double> hb(double total, int num, double min) {
        List<Double> result = new ArrayList<>();

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
        return result;
    }

    void zb() {
        for (int a = 0; a <= 10000; a++) {

            if (a % 1000 == 0){
                System.out.println(a);
            }
        }
    }
}
