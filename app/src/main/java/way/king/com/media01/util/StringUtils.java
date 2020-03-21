package way.king.com.media01.util;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2016/8/16
 *     desc  : 字符串相关工具类
 * </pre>
 */
public class StringUtils {

    private StringUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }


    /**
     * 判断字符串是否为null或长度为0
     *
     * @param s 待校验字符串
     * @return {@code true}: 空<br> {@code false}: 不为空
     */
    public static boolean isEmpty(CharSequence s) {
        return s == null || s.length() == 0;
    }


    /**
     * 判断字符串不为null或长度不为0
     *
     * @param s 待校验字符串
     * @return {@code true}: 不为空 <br> {@code false}: 空
     */
    public static boolean isNotEmpty(CharSequence s) {
        return !isEmpty(s);
    }


    /**
     * 判断字符串是否为null或全为空格
     *
     * @param s 待校验字符串
     * @return {@code true}: null或全空格<br> {@code false}: 不为null且不全空格
     */
    public static boolean isSpace(String s) {
        return (s == null || s.trim().length() == 0);
    }


    /**
     * 判断两字符串是否相等
     *
     * @param a 待校验字符串a
     * @param b 待校验字符串b
     * @return {@code true}: 相等<br>{@code false}: 不相等
     */
    public static boolean equals(CharSequence a, CharSequence b) {
        if (a == b) {
            return true;
        }
        int length;
        if (a != null && b != null && (length = a.length()) == b.length()) {
            if (a instanceof String && b instanceof String) {
                return a.equals(b);
            } else {
                for (int i = 0; i < length; i++) {
                    if (a.charAt(i) != b.charAt(i)) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }


    /**
     * null转为长度为0的字符串
     *
     * @param s 待转字符串
     * @return s为null转为长度为0字符串，否则不改变
     */
    public static String null2Length0(String s) {
        return s == null ? "" : s;
    }


    /**
     * 返回字符串长度
     *
     * @param s 字符串
     * @return null返回0，其他返回自身长度
     */
    public static int length(CharSequence s) {
        return s == null ? 0 : s.length();
    }


    /**
     * 区分中英文字符长度，一个汉字等于2个字母
     *
     * @param paramString 字符串
     */
    public static int chineseLength(String paramString) {
        int i = 0;
        for (int j = 0; j < paramString.length(); j++) {
            if (paramString.substring(j, j + 1).matches("[Α-￥]")) {
                i += 2;
            } else {
                i++;
            }
        }

        if (i % 2 > 0) {
            i = 1 + i / 2;
        } else {
            i = i / 2;
        }

        return i;
    }


    /**
     * 首字母大写
     *
     * @param s 待转字符串
     * @return 首字母大写字符串
     */
    public static String upperFirstLetter(String s) {
        if (isEmpty(s) || !Character.isLowerCase(s.charAt(0))) {
            return s;
        }
        return String.valueOf((char) (s.charAt(0) - 32)) + s.substring(1);
    }


    /**
     * 首字母小写
     *
     * @param s 待转字符串
     * @return 首字母小写字符串
     */
    public static String lowerFirstLetter(String s) {
        if (isEmpty(s) || !Character.isUpperCase(s.charAt(0))) {
            return s;
        }
        return String.valueOf((char) (s.charAt(0) + 32)) + s.substring(1);
    }


    /**
     * 反转字符串
     *
     * @param s 待反转字符串
     * @return 反转字符串
     */
    public static String reverse(String s) {
        int len = length(s);
        if (len <= 1) {
            return s;
        }
        int mid = len >> 1;
        char[] chars = s.toCharArray();
        char c;
        for (int i = 0; i < mid; ++i) {
            c = chars[i];
            chars[i] = chars[len - i - 1];
            chars[len - i - 1] = c;
        }
        return new String(chars);
    }


    /**
     * 转化为半角字符
     *
     * @param s 待转字符串
     * @return 半角字符串
     */
    public static String toDBC(String s) {
        if (isEmpty(s)) {
            return s;
        }
        char[] chars = s.toCharArray();
        for (int i = 0, len = chars.length; i < len; i++) {
            if (chars[i] == 12288) {
                chars[i] = ' ';
            } else if (65281 <= chars[i] && chars[i] <= 65374) {
                chars[i] = (char) (chars[i] - 65248);
            } else {
                chars[i] = chars[i];
            }
        }
        return new String(chars);
    }


    /**
     * 转化为全角字符
     *
     * @param s 待转字符串
     * @return 全角字符串
     */
    public static String toSBC(String s) {
        if (isEmpty(s)) {
            return s;
        }
        char[] chars = s.toCharArray();
        for (int i = 0, len = chars.length; i < len; i++) {
            if (chars[i] == ' ') {
                chars[i] = (char) 12288;
            } else if (33 <= chars[i] && chars[i] <= 126) {
                chars[i] = (char) (chars[i] + 65248);
            } else {
                chars[i] = chars[i];
            }
        }
        return new String(chars);
    }


    /**
     * 替换回车和换行
     */
    public static CharSequence replaceEnter(CharSequence oldString) {
        Pattern pattern = Pattern.compile("(\r\n|\r|\n|\n\r)");
        Matcher m = pattern.matcher(oldString);
        return m.replaceAll("");
    }


    /**
     * 替换回车和换行
     */
    public static boolean containsEnter(CharSequence oldString) {
        Pattern pattern = Pattern.compile("(\r\n|\r|\n|\n\r)");
        Matcher m = pattern.matcher(oldString);
        return m.find();
    }


    /**
     * 限制昵称为10个汉子大小
     */
    public static String subNickName(String userName) {
        if (TextUtils.isEmpty(userName)) {
            return "";
        }
        int length = 10;
        int i = StringUtils.chineseLength(userName);
        if (i > length) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int n = 0; ; n++) {
                String substring = userName.substring(n, n + 1);
                stringBuilder.append(substring);
                int i1 = StringUtils.chineseLength(stringBuilder.toString());
                if (i1 > length) {
                    return stringBuilder.delete(i1, i1 + 1).toString();
                }
            }
        } else {
            return userName;
        }
    }


    /**
     * 加入空格
     */
    public static StringBuilder addSpace(int spaceLength) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < spaceLength; i++) {
            builder.append("   ");
        }
        return builder;
    }


    public static String meterToKm(double meter) {
        int m = (int) meter;
        if (m >= 1000) {
            double km = m / 1000;
            return String.valueOf(km + "km");
        } else {
            return m + "米";
        }

    }

    /*
     *
     * 限制不能输入阿拉伯汉子
     *name 要检查的文字
     * replacecontext替换的内容
     * */


    public static StringBuilder noinput(String name, String replacecontext) {
        StringBuilder sb = new StringBuilder(name);
        Pattern p = Pattern.compile("[\u0600-\u06ff]");
        Matcher m = p.matcher(name);
        while (m.find()) {
            String s = "";
            for (int i = 0; i < m.end() - m.start(); i++) {
                s += replacecontext;
            }
            sb.replace(m.start(), m.end(), s);
        }
        return sb;
    }


    public static String listToString(String[] list, String s) {
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < list.length; i++) {
            buffer.append(list[i]);
            if (i < list.length - 1) {
                buffer.append(s);
            }
        }
        return buffer.toString();

    }


    public static String[] listToString(String string) {
        return string.split("-");
    }


    public static boolean isStringNull(String string) {
        return TextUtils.isEmpty(string) || string.equals("0");
    }

}
