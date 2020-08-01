package com.sam.framework.utils;

import com.sam.framework.enums.ErrorCodeEnum;

import java.math.BigDecimal;

public class BigDecimalUtils {
    // 除法运算默认精度
    private static final int DEF_DIV_SCALE = 2;

    /**
     * 精确加法
     *
     * @param value1
     * @param value2
     * @return
     */
    public static double add(double value1, double value2) {
        BigDecimal b1 = BigDecimal.valueOf(value1);
        BigDecimal b2 = BigDecimal.valueOf(value2);
        return b1.add(b2).doubleValue();
    }

    /**
     * 精确加法
     *
     * @param value1
     * @param value2
     * @return
     */
    public static double add(String value1, String value2) {
        BigDecimal b1 = new BigDecimal(value1);
        BigDecimal b2 = new BigDecimal(value2);
        return b1.add(b2).doubleValue();
    }

    /**
     * 多个数精确加法
     *
     * @param arg
     * @return
     */
    public static double add(double... arg) {
        BigDecimal sum = BigDecimal.valueOf(0.0);
        for (double d : arg) {
            BigDecimal bigDecimal = BigDecimal.valueOf(d);
            sum = sum.add(bigDecimal);
        }
        return sum.doubleValue();
    }

    /**
     * 多个数精确加法
     *
     * @param arg
     * @return
     */
    public static double add(String... arg) {
        BigDecimal sum = new BigDecimal(0.0);
        for (String s : arg) {
            BigDecimal bigDecimal = new BigDecimal(s);
            sum = sum.add(bigDecimal);
        }
        return sum.doubleValue();
    }

    /**
     * 精确减法
     *
     * @param value1
     * @param value2
     * @return
     */
    public static double sub(double value1, double value2) {
        BigDecimal b1 = BigDecimal.valueOf(value1);
        BigDecimal b2 = BigDecimal.valueOf(value2);
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 精确减法
     *
     * @param value1
     * @param value2
     * @return
     */
    public static double sub(String value1, String value2) {
        BigDecimal b1 = new BigDecimal(value1);
        BigDecimal b2 = new BigDecimal(value2);
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 多个数精确减法
     *
     * @param arg
     * @return
     */
    public static double sub(double base, double... arg) {
        BigDecimal sum = BigDecimal.valueOf(base);
        for (double d : arg) {
            BigDecimal bigDecimal = BigDecimal.valueOf(d);
            sum = sum.subtract(bigDecimal);
        }
        return sum.doubleValue();
    }

    /**
     * 多个数精确减法
     *
     * @param arg
     * @return
     */
    public static double sub(String base, String... arg) {
        BigDecimal sum = new BigDecimal(base);
        for (String s : arg) {
            BigDecimal bigDecimal = new BigDecimal(s);
            sum = sum.subtract(bigDecimal);
        }
        return sum.doubleValue();
    }

    /**
     * 精确乘法
     *
     * @param value1
     * @param value2
     * @return
     */
    public static double mul(double value1, double value2) {
        BigDecimal b1 = BigDecimal.valueOf(value1);
        BigDecimal b2 = BigDecimal.valueOf(value2);
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 精确乘法
     *
     * @param value1
     * @param value2
     * @return 返回double
     */
    public static double mul(String value1, String value2) {
        BigDecimal b1 = new BigDecimal(value1);
        BigDecimal b2 = new BigDecimal(value2);
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 精确乘法
     *
     * @param value1
     * @param value2
     * @return 返回int
     */
    public static int mulInt(double value1, double value2) {
        BigDecimal b1 = BigDecimal.valueOf(value1);
        BigDecimal b2 = BigDecimal.valueOf(value2);
        return b1.multiply(b2).intValue();
    }

    /**
     * 精确除法 使用默认精度
     *
     * @param value1
     * @param value2
     * @return
     */
    public static double div(double value1, double value2) {
        return div(value1, value2, DEF_DIV_SCALE);
    }

    /**
     * 精确除法 使用默认精度
     *
     * @param value1
     * @param value2
     * @return
     */
    public static double div(String value1, String value2) {
        return div(value1, value2, DEF_DIV_SCALE);
    }

    /**
     * 精确除法
     *
     * @param scale 精度
     */
    public static double div(double value1, double value2, int scale) {
        ApiAssert.isFalse(ErrorCodeEnum.INTERNAL_SERVER_ERROR.convert("精确度不能小于0"), scale < 0);
        BigDecimal b1 = BigDecimal.valueOf(value1);
        BigDecimal b2 = BigDecimal.valueOf(value2);
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 精确除法
     *
     * @param value1
     * @param value2
     * @param scale  精度
     * @return
     */
    public static double div(String value1, String value2, int scale) {
        ApiAssert.isFalse(ErrorCodeEnum.INTERNAL_SERVER_ERROR.convert("精确度不能小于0"), scale < 0);
        BigDecimal b1 = new BigDecimal(value1);
        BigDecimal b2 = new BigDecimal(value2);
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 四舍五入
     *
     * @param v
     * @param scale 小数点后保留几位
     * @return
     */
    public static double round(double v, int scale) {
        return div(v, 1, scale);
    }

    /**
     * 四舍五入
     *
     * @param v
     * @param scale 小数点后保留几位
     * @return
     */
    public static double round(String v, int scale) {
        return div(v, "1", scale);
    }

    /**
     * 比较大小
     *
     * @param b1
     * @param b2
     * @return
     */
    public static boolean equalTo(BigDecimal b1, BigDecimal b2) {
        if (b1 == null || b2 == null) {
            return false;
        }
        return 0 == b1.compareTo(b2);
    }

    public static void main(String[] args) {
        System.out.println(0.1 + 0.2);//0.30000000000000004
        System.out.println(0.3 - 0.2);//0.09999999999999998
        System.out.println(0.1 * 0.2);//0.020000000000000004
        System.out.println(0.1 / 3);//0.03333333333333333
        System.out.println();
        System.out.println(add(0.1, 0.2));//0.3
        System.out.println(sub(0.3, 0.2));//-0.1
        System.out.println(mul(0.1, 0.2));//0.02
        System.out.println(div(0.1, 3));//0.03
    }
}
