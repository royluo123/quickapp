package com.roy.quickapp.base.utils;

/**
 * Created by Administrator on 2017/8/11.
 */

public class StringUtils {
    private StringUtils(){
    }

    public static boolean isEmpty(String text) {
        return text == null || text.length() == 0;
    }

    public static boolean isEmpty(StringBuffer stringBuffer){
        return stringBuffer == null || stringBuffer.length() == 0;
    }

    public static boolean isNotEmpty(String text) {
        return !isEmpty(text);
    }

    public static String avoidNull(String text) {
        return text == null ? "" : text;
    }

    public static String merge(String ... texts) {
        if(texts == null){
            return "";
        }

        StringBuffer buffer = new StringBuffer(32 * texts.length * 10);
        for(int j = 0; j < texts.length; j++){
            buffer.append(texts[j]);
        }
        return buffer.toString();
    }
}
