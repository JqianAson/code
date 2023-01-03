package com.demo.code.util;

import org.apache.commons.codec.digest.DigestUtils;

import java.security.SecureRandom;
import java.util.Random;

/**
 * @author Stephen Sun
 * @date 2019-04-10 15:47
 * ━━━━━━神兽出没━━━━━━
 * 　　　┏┓　　　┏┓
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　　　　　┃
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　　　　　┃
 * 　　┃　　　┻　　　┃
 * 　　┃　　　　　　　┃
 * 　　┗━┓　　　┏━┛
 * 　　　　┃　　　┃
 * 　　　　┃　　　┃
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * <p>
 * ━━━━━━感觉萌萌哒━━━━━━
 */
public class Password {


    private String password;

    private String salt;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }


    /**
     * 用于去生成随机密码
     */
    private final static char[] passwordCharArray = new char[]{
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};


    /**
     * 创建指定位数的随机密码，密码由字母加数字组成
     *
     * @param length 允许传入的最小值为6
     * @return
     */
    public static String createRandomPassword(int length) {
        if (length < 6) {
            throw new IllegalArgumentException(String.format("创建的随机密码最小长度为6！申请长度为: %s", length));
        }
        char[] passwordChars = new char[length];
        for (int i = 0; i < length; i++) {
            passwordChars[i] = passwordCharArray[(int) (Math.random() * passwordCharArray.length)];
        }
        return String.copyValueOf(passwordChars);
    }


    /**
     * 创建密码
     *
     * @param clearPwd
     * @return
     */
    public static Password createPassword(String clearPwd) {
        if (StringUtils.isEmpty(clearPwd)) {
            throw new RuntimeException("[Password] [createPassword] clearPwd cant be null");
        }
        String salt = createSecureSalt();
        String temp = DigestUtils.md5Hex(clearPwd);
        String password = DigestUtils.sha512Hex(salt + temp);
        Password pwd = new Password();
        pwd.setPassword(password);
        pwd.setSalt(salt);
        return pwd;
    }

    /**
     * 获取密码
     *
     * @param clearPwd
     * @param salt
     * @return
     */
    public static Password getPassword(String clearPwd, String salt) {
        if (StringUtils.isEmpty(clearPwd)) {
            throw new RuntimeException("[Password] [createPassword] clearPwd cant be null");
        }
        if (StringUtils.isEmpty(salt)) {
            throw new RuntimeException("[Password] [createPassword] salt cant be null");
        }
        String temp = DigestUtils.md5Hex(clearPwd);
        String password = DigestUtils.sha512Hex(salt + temp);
        Password pwd = new Password();
        pwd.setPassword(password);
        pwd.setSalt(salt);
        return pwd;
    }

    /**
     * 创建资金密码
     *
     * @param fundPassword
     * @return
     */
    public static Password createFundPassword(String fundPassword) {
        if (StringUtils.isEmpty(fundPassword)) {
            throw new RuntimeException("[Password] [createFundPassword] fundPassword cannot be null");
        }
        String temp = DigestUtils.md5Hex(fundPassword);
        String password = DigestUtils.sha512Hex(temp);
        Password pwd = new Password();
        pwd.setPassword(password);
        return pwd;
    }

    /**
     * 创建密码salt
     *
     * @return
     */
    private static String createSecureSalt() {
        Random ranGen = new SecureRandom();
        byte[] aesKey = new byte[20];
        ranGen.nextBytes(aesKey);
        StringBuffer hexString = new StringBuffer();

        for (int i = 0; i < aesKey.length; ++i) {
            String hex = Integer.toHexString(255 & aesKey[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
