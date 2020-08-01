package com.sam.framework.utils;

/**
 * 任意进制转换
 *
 * @author Administrator
 */
public class BinaryConversion {

    static char[] chs = new char[36];

    static {
        for (int i = 0; i < 10; i++) {
            chs[i] = (char) ('0' + i);
        }
        for (int i = 10; i < chs.length; i++) {
            chs[i] = (char) ('A' + (i - 10));
        }
    }

    /**
     * 转换方法
     *
     * @param num       元数据字符串
     * @param fromRadix 元数据的进制类型
     * @param toRadix   目标进制类型
     * @return
     */
    public static String transRadix(String num, int fromRadix, int toRadix) {
        long number = Long.valueOf(num, fromRadix);
        StringBuilder sb = new StringBuilder();
        while (number != 0) {
            sb.append(chs[Integer.parseInt(String.valueOf(number % toRadix))]);
            number = number / toRadix;
        }
        String result = sb.reverse().toString();
        if ("".equals(result)) {
            result = "0";
        }
        return result;
    }

}
