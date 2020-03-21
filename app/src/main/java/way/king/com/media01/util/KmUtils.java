package way.king.com.media01.util;

import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

/*
 * Author: VICK
 * Create: 2020-03-06
 * Description:
 */
public class KmUtils {
    private static final NavigableMap<Long, String> suffixes = new TreeMap<>();

    static {
        suffixes.put(1_000L, "K");
        suffixes.put(10000L, "W");
        suffixes.put(1_000_000L, "M");
        suffixes.put(1_000_000_000L, "G");
        suffixes.put(1_000_000_000_000L, "T");
        suffixes.put(1_000_000_000_000_000L, "P");
        suffixes.put(1_000_000_000_000_000_000L, "E");
    }

    public static String formatKm(long value) {
        if (value < 0) return "-" + formatKm(-value);
        if (value <= 9999) return Long.toString(value); //Dealing with value > 5 length
        Map.Entry<Long, String> e = suffixes.floorEntry(value);
        Long divideBy = e.getKey();
        String suffix = e.getValue();
        long truncated = value / (divideBy / 1000); //the number part of the output times 1000
        StringBuilder number = new StringBuilder((truncated / 10) + suffix);
        String convertStr = number.insert(number.length() - 3, ".").toString();
        System.out.println("原始字符串 " + number);
        System.out.println("转换后字符串 " + convertStr);
        return convertStr;
    }
}
