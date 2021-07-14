package com.etocrm.sdk.util;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class DecimalFormatUtils {

    public static  String  numberWithPrecision(double num){
        DecimalFormat df = new DecimalFormat("#,##0.00");
        df.setRoundingMode(RoundingMode.HALF_UP);
        String format = df.format(num);
        return format;
    }

    public static String getPercent(int num1, int num2) {
        // 创建一个数值格式化对象
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后2位
        numberFormat.setMaximumFractionDigits(2);
        if(num1==0||num2==0){
            return "0.00";
        }
        String result = numberFormat.format((float) num1 / (float) num2 * 100);
        return result;
    }

}
