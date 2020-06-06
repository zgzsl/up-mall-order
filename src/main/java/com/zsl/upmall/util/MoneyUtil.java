package com.zsl.upmall.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class MoneyUtil {

    private final static DecimalFormat NF_YUAN = new DecimalFormat("#####################0.00");
    private final static DecimalFormat NF_FEN = new DecimalFormat("#####################0");

    public static BigDecimal moneyFen2Yuan(String fen) {
        if (fen == null || !fen.matches(Regex.REGEX_MONEY_FEN))
            return null;
        return formatYuan(new BigDecimal(fen).divide(new BigDecimal("100")));
    }

    public static BigDecimal moneyYuan2Fen(BigDecimal yuan) {
        return yuan == null ? null : formatFen(yuan.multiply(new BigDecimal("100")));
    }

    public static String moneyFen2YuanStr(BigDecimal fen) {
        return fen == null ? null : NF_YUAN.format(moneyFen2Yuan(fen.toString()));
    }

    public static String moneyYuan2FenStr(BigDecimal yuan) {
        return yuan == null ? null : NF_FEN.format(yuan.multiply(new BigDecimal("100")));
    }

    public static BigDecimal formatYuan(BigDecimal yuan) {
        return yuan == null ? null : new BigDecimal(NF_YUAN.format(yuan));
    }

    public static BigDecimal formatFen(BigDecimal fen) {
        return fen == null ? null : new BigDecimal(NF_FEN.format(fen));
    }
}

class Regex {
    public static final String REGEX_MONEY_YUAN = "^((([1-9]\\d*)(\\.\\d{1,2})?)|(0(\\.\\d{1,2})))$";
    public static final String REGEX_MONEY_FEN = "^[0-9]{1}\\d*$";
    public static final String REGEX_SERIALNO = "^[A-Za-z0-9]+$";
    public static final String REGEX_REQORDERID = "^[A-Za-z0-9]+$";
    public static final String REGEX_ORDERTIME = "^\\d{14}$";
    public static final String REGEX_ORDERPERIOD = "^\\d{0,10}$";
    public static final String REGEX_NUM = "^\\d{1,8}$";
    public static final String REGEX_PHONE = "^((13[0-9])|(15[^4,\\\\D])|(18[0,5-9]))\\\\d{8}$";

}

