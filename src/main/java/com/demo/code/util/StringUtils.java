package com.demo.code.util;

import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Stephen Suen
 * 2020-06-10 19:06
 * 做咩呀???
 */
public class StringUtils {


    // 邮箱正则
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z\\d]+([-_.]*[A-Za-z\\d_]+)*@([A-Za-z\\d]+[-.])+[A-Za-z\\d]{2,}$");
    // 密码复杂度正则
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$|^(?![a-z]+$)(?![0-9A-Z]+$)[0-9A-Za-z]{6,20}$|^(?![A-Z]+$)(?![0-9a-z]+$)[0-9A-Za-z]{6,20}$");
    // 密码复杂度正则
    private static final Pattern PASSWORD_PATTERN_2 = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{5,16}$");
    // 数字正则
    private static final Pattern NUMBER_PATTERN = Pattern.compile("^[0-9]*$");
    // 密码难度评级2
    private static final Pattern PASS_LEVEL_2 = Pattern.compile("^(((?=.*[A-Z])(?=.*[a-z]))|((?=.*[A-Z])(?=.*[0-9]))|((?=.*[a-z])(?=.*[0-9]))).{8,20}$");
    // 密码难度评级3
    private static final Pattern PASS_LEVEL_3 = Pattern.compile("^(?=.*[A-Z])(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[a-z])(?=.*[0-9])(?=.*([0-9]|[a-z]|[A-Z])).{8,20}$");
    // 18 位身份证
    private static final Pattern ID_NUMBER_18 = Pattern.compile("^[1-9][0-9]{5}(18|19|20)[0-9]{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)[0-9]{3}([0-9]|([Xx]))");
    // 15 位身份证
    private static final Pattern ID_NUMBER_15 = Pattern.compile("^[1-9][0-9]{5}[0-9]{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)[0-9]{2}[0-9]");

    /**
     * 校验URL可用
     *
     * @param connection 需要校验的URL
     * @return 校验结果
     */
    public static Boolean checkUrl(String connection) {
        URL url;
        try {
            url = new URL(connection);
            InputStream inputStream = url.openStream();
            if (inputStream != null) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public static String createUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static boolean isEmpty(Object str) {
        return str == null || "".equals(str);
    }

    public static boolean isNotEmpty(Object str) {
        return !isEmpty(str);
    }

    public static boolean equals(Object a, Object b) {
        if (a == null && b == null) {
            return false;
        } else {
            return a != null && a.equals(b);
        }
    }

    public static boolean equals(Object a, Object b, boolean defaultIs) {
        if (a == null && b == null) {
            return defaultIs;
        } else {
            return a != null && a.equals(b);
        }
    }

    public static boolean patternMail(String mail) {
        Matcher matcher = EMAIL_PATTERN.matcher(mail);
        return matcher.matches();
    }

    public static boolean patternId18(String id) {
        Matcher matcher = ID_NUMBER_18.matcher(id);
        return matcher.matches();
    }

    public static boolean patternId15(String id) {
        Matcher matcher = ID_NUMBER_15.matcher(id);
        return matcher.matches();
    }

    public static boolean patternPassword(String originPassword) {
        Matcher matcher = PASSWORD_PATTERN.matcher(originPassword);
        return matcher.matches();
    }

    public static boolean patternPassword2(String originPassword) {
        Matcher matcher = PASSWORD_PATTERN_2.matcher(originPassword);
        return matcher.matches();
    }

    public static boolean patternNumber(String originNumber) {
        Matcher matcher = NUMBER_PATTERN.matcher(originNumber);
        return matcher.matches();
    }

    /**
     * 处理邮箱和手机号
     *
     * @param str  参数
     * @param type 1:手机号   2：邮箱   3: 身份证号   4 银行卡号
     * @return 隐藏部分值
     */
    public static String hideString(String str, int type) {
        if (isEmpty(str)) {
            return str;
        } else {
            if (type == 1) {
                str = str.replaceAll(" ", "");
                String pre = "";
                if (str.startsWith("+")) {
                    pre = "+";
                    str = str.replaceAll("\\+", "");
                }

                int length = str.length();
                if (length >= 6) {
                    String start = str.substring(0, 3);
                    String end = str.substring(length - 4, length);
                    return pre + start + " **** " + end;
                }
            } else if (type == 2) {
                str = str.replaceAll(" ", "");
                String[] arr = str.split("@");
                if (arr.length <= 1) {
                    return str;
                }

                String s = arr[0];
                if (s.length() > 1) {
                    String[] sub = s.split("");
                    return sub[0] + "****@" + arr[1];
                }
            } else {
                if (type == 3) {
                    if (!isEmpty(str) && str.length() >= 8) {
                        return str.replaceAll("(?<=\\w{3})\\w(?=\\w{4})", "*");
                    }

                    return str;
                }

                if (type == 4) {
                    if (!isEmpty(str) && str.length() >= 8) {
                        return str.replaceAll("(?<=\\w{3})\\w(?=\\w{4})", "*");
                    }

                    return str;
                }

                if (type == 5) {
                    if (!isEmpty(str) && str.length() >= 6) {
                        return str.replaceAll("(?<=\\w{2})\\w(?=\\w{3})", "*");
                    }

                    return str;
                }
            }

            return str;
        }
    }

    public static boolean isNumber(String str) {
        if (isEmpty(str)) {
            return false;
        } else {
            char[] chars = str.toCharArray();
            int sz = chars.length;
            boolean hasExp = false;
            boolean hasDecPoint = false;
            boolean allowSigns = false;
            boolean foundDigit = false;
            int start = chars[0] == '-' ? 1 : 0;
            int i;
            if (sz > start + 1 && chars[start] == '0' && chars[start + 1] == 'x') {
                i = start + 2;
                if (i == sz) {
                    return false;
                } else {
                    while (i < chars.length) {
                        if ((chars[i] < '0' || chars[i] > '9') && (chars[i] < 'a' || chars[i] > 'f') && (chars[i] < 'A' || chars[i] > 'F')) {
                            return false;
                        }

                        ++i;
                    }

                    return true;
                }
            } else {
                --sz;

                for (i = start; i < sz || i < sz + 1 && allowSigns && !foundDigit; ++i) {
                    if (chars[i] >= '0' && chars[i] <= '9') {
                        foundDigit = true;
                        allowSigns = false;
                    } else if (chars[i] == '.') {
                        if (hasDecPoint || hasExp) {
                            return false;
                        }

                        hasDecPoint = true;
                    } else if (chars[i] != 'e' && chars[i] != 'E') {
                        if (chars[i] != '+' && chars[i] != '-') {
                            return false;
                        }

                        if (!allowSigns) {
                            return false;
                        }

                        allowSigns = false;
                        foundDigit = false;
                    } else {
                        if (hasExp) {
                            return false;
                        }

                        if (!foundDigit) {
                            return false;
                        }

                        hasExp = true;
                        allowSigns = true;
                    }
                }

                if (i < chars.length) {
                    if (chars[i] >= '0' && chars[i] <= '9') {
                        return true;
                    } else if (chars[i] != 'e' && chars[i] != 'E') {
                        if (chars[i] == '.') {
                            return !hasDecPoint && !hasExp && foundDigit;
                        } else if (!allowSigns && (chars[i] == 'd' || chars[i] == 'D' || chars[i] == 'f' || chars[i] == 'F')) {
                            return foundDigit;
                        } else if (chars[i] != 'l' && chars[i] != 'L') {
                            return false;
                        } else {
                            return foundDigit && !hasExp;
                        }
                    } else {
                        return false;
                    }
                } else {
                    return !allowSigns && foundDigit;
                }
            }
        }
    }

    public static byte[] hexString2Bytes(String src) {
        int length = src.length() / 2;
        byte[] result = new byte[length];
        char[] array = src.toCharArray();

        for (int i = 0; i < length; ++i) {
            result[i] = uniteBytes(array[2 * i], array[2 * i + 1]);
        }

        return result;
    }

    public static String byte2HexString(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        int var3 = bytes.length;

        for (byte aByte : bytes) {
            String hex = Integer.toHexString(255 & aByte);
            if (hex.length() == 1) {
                hexString.append('0');
            }

            hexString.append(hex);
        }

        return hexString.toString();
    }

    private static byte uniteBytes(char mostChar, char secondChar) {
        byte b0 = Byte.decode("0x" + mostChar);
        b0 = (byte) (b0 << 4);
        byte b1 = Byte.decode("0x" + secondChar);
        return (byte) (b0 | b1);
    }


    public static Integer passwordLevel(String password) {
        if (PASS_LEVEL_3.matcher(password).matches()) {
            return 3;
        } else {
            return PASS_LEVEL_2.matcher(password).matches() ? 2 : 1;
        }
    }


    public static String generateRandom() {
        String uuid = UUID.randomUUID().toString();
        return uuid.substring(0, 8) + uuid.substring(9, 13) + uuid.substring(14, 18) + uuid.substring(19, 23) + uuid.substring(24);
    }

    //生成随机数字和字母,
    public static String getRandomPwd() {

        StringBuilder val = new StringBuilder();
        Random random = new Random();
        //length为几位密码
        for (int i = 0; i < 6; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if ("char".equalsIgnoreCase(charOrNum)) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val.append((char) (random.nextInt(26) + temp));
            } else {
                val.append(random.nextInt(10));
            }
        }
        return val.toString();
    }


    /**
     * 1、用户名最多25个字符长度、不能包含空格、单双引号、问号等特殊符号
     */
    public static boolean validateName(String userName) {
        if (userName == null || userName.length() > 25) {
            return false;
        }
        return Pattern.matches("^[A-Za-z0-9*]*$", userName);
    }

    /**
     * 密码6-8位，只能包含字母、数字
     */
    public static boolean validatePassWord(String passWord) {
        if (passWord == null || passWord.length() < 6 || passWord.length() > 8) {
            return false;
        }
        for (char ch : passWord.toCharArray()) {
            //如果此字符不是数字或字母
            if (!Character.isLetterOrDigit(ch)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取随机生成的验证码
     */
    public static String getVerifyCode(int len) {
        String[] verifyString = new String[]{"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9"};
        Random random = new Random(System.currentTimeMillis());
        StringBuilder verifyBuilder = new StringBuilder();
        for (int i = 0; i < len; i++) {
            int rd = random.nextInt(10);
            verifyBuilder.append(verifyString[rd]);
        }
        return verifyBuilder.toString();
    }

    public static String join(Object[] array, String delimiter) {
        List<Object> list;
        if (array == null || array.length == 0) {
            list = Collections.emptyList();
        } else {
            list = Arrays.asList(array);
        }
        return join(list, delimiter);
    }

    public static String join(List<?> list, String delimiter) {
        if (list == null || list.size() <= 0) {
            return "";
        }
        if (delimiter == null || delimiter.isEmpty()) {
            delimiter = " ";
        }
        StringBuilder buffer = new StringBuilder();
        int count = 1;
        for (Object aList : list) {
            if (aList == null) {
                continue;
            }
            if (count != 1) {
                buffer.append(delimiter);
            }
            buffer.append(aList.toString());
            count++;
        }
        return buffer.toString();
    }

    public static boolean isBlank(String str) {
        int strLen;
        if (str != null && (strLen = str.length()) != 0) {
            for(int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(str.charAt(i))) {
                    return false;
                }
            }

            return true;
        } else {
            return true;
        }
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }
}
