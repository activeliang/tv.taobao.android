package com.tvtaobao.voicesdk.utils;

import com.google.zxing.common.StringUtils;
import com.taobao.muniontaobaosdk.p4p.anticheat.model.ClientTraceData;
import com.yunos.tvtaobao.biz.listener.OnReminderListener;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class SimilarDegreeByCos {
    public static double getSimilarity(String doc1, String doc2) {
        int charIndex;
        int charIndex2;
        if (doc1 == null || doc1.trim().length() <= 0 || doc2 == null || doc2.trim().length() <= 0) {
            throw new NullPointerException(" the Document is null or have not cahrs!!");
        }
        Map<Integer, int[]> AlgorithmMap = new HashMap<>();
        for (int i = 0; i < doc1.length(); i++) {
            char d1 = doc1.charAt(i);
            if (isHanZi(d1) && (charIndex2 = getGB2312Id(d1)) != -1) {
                int[] fq = AlgorithmMap.get(Integer.valueOf(charIndex2));
                if (fq == null || fq.length != 2) {
                    AlgorithmMap.put(Integer.valueOf(charIndex2), new int[]{1, 0});
                } else {
                    fq[0] = fq[0] + 1;
                }
            }
        }
        for (int i2 = 0; i2 < doc2.length(); i2++) {
            char d2 = doc2.charAt(i2);
            if (isHanZi(d2) && (charIndex = getGB2312Id(d2)) != -1) {
                int[] fq2 = AlgorithmMap.get(Integer.valueOf(charIndex));
                if (fq2 == null || fq2.length != 2) {
                    AlgorithmMap.put(Integer.valueOf(charIndex), new int[]{0, 1});
                } else {
                    fq2[1] = fq2[1] + 1;
                }
            }
        }
        double sqdoc1 = ClientTraceData.b.f47a;
        double sqdoc2 = ClientTraceData.b.f47a;
        double denominator = ClientTraceData.b.f47a;
        for (Integer num : AlgorithmMap.keySet()) {
            int[] c = AlgorithmMap.get(num);
            denominator += (double) (c[0] * c[1]);
            sqdoc1 += (double) (c[0] * c[0]);
            sqdoc2 += (double) (c[1] * c[1]);
        }
        return denominator / Math.sqrt(sqdoc1 * sqdoc2);
    }

    public static boolean isHanZi(char ch) {
        return ch >= 19968 && ch <= 40869;
    }

    public static short getGB2312Id(char ch) {
        try {
            byte[] buffer = Character.toString(ch).getBytes(StringUtils.GB2312);
            if (buffer.length != 2) {
                return -1;
            }
            return (short) ((((buffer[0] & OnReminderListener.RET_FULL) - 161) * 94) + ((buffer[1] & OnReminderListener.RET_FULL) - 161));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static void main() {
        System.out.println("str1和str2相识度：" + getSimilarity("我上个月花了多少钱", "上个月我在淘宝花了多少钱"));
        System.out.println("str1和str3相识度：" + getSimilarity("我上个月花了多少钱", "我要买西瓜"));
        System.out.println("str3和str4相识度：" + getSimilarity("我要买西瓜", "我想买花生油"));
        System.out.println("str1和str5相识度：" + getSimilarity("我上个月花了多少钱", "有没有西瓜"));
        System.out.println("str1和str6相识度：" + getSimilarity("我上个月花了多少钱", "我用了多少钱在上周"));
        System.out.println("str2和str6相识度：" + getSimilarity("上个月我在淘宝花了多少钱", "我用了多少钱在上周"));
        System.out.println("str3和str5相识度：" + getSimilarity("我要买西瓜", "有没有西瓜"));
        System.out.println("str4和str5相识度：" + getSimilarity("我想买花生油", "有没有西瓜"));
        System.out.println("str7和str8相识度：" + getSimilarity("我不喜欢唱歌，但是喜欢跳舞", "我喜欢唱歌，而且喜欢跳舞"));
        System.out.println("str7和str9相识度：" + getSimilarity("我不喜欢唱歌，但是喜欢跳舞", "我喜欢唱歌，但是不喜欢跳舞"));
        System.out.println("str8和str9相识度：" + getSimilarity("我喜欢唱歌，而且喜欢跳舞", "我喜欢唱歌，但是不喜欢跳舞"));
    }
}
