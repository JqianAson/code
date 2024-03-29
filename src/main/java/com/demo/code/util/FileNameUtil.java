package com.demo.code.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.zip.CRC32;

/**
 * @Author roman
 * @create 2021/6/11 2:39 下午
 */
public class FileNameUtil {

    private static final byte[] IV = new byte[]{0x00, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9, 0xa, 0xb, 0xc, 0xd, 0xe, 0xf};

    private static final byte DEFAULT_MAX_CONFOUND_SIZE = 3;


    public static String encryptFile(String plainData, String password) {
        return encryptFile(plainData, password, false, DEFAULT_MAX_CONFOUND_SIZE);
    }

    private static String encryptFile(String plainData, String password, boolean caseInsensitive, int confoundSize) {
        if (plainData == null) {
            return null;
        }
        byte[] cipherDataArray = null;
        try {
            Random random = new Random();
            byte[] plainDataArray = plainData.getBytes("UTF-8");
            byte fixedConfoundSize = confoundSize <= 0 ? 0 : (byte) (Math.abs(random.nextInt() % confoundSize) + 1);
            cipherDataArray = encrypt(combinePlainDataWithChecksum(mixPlainData(plainDataArray, fixedConfoundSize)), password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (cipherDataArray == null || cipherDataArray.length == 0) {
            return null;
        }
        return caseInsensitive ? StringUtils.byte2HexString(cipherDataArray) : Base64.encodeWithDajieSpec(cipherDataArray);
    }

    /**
     * 加密， password 如果为null，默认会赋值为空串。
     *
     * @param plainData 需要加密的内容
     * @param password  加密密码，默认为空串
     * @return 加密后的内容，如果plainData为null或者长度为0，会原样返回。
     */
    private static byte[] encrypt(byte[] plainData, String password) {
        if (plainData == null) {
            return plainData;
        }
        if (password == null) {
            password = "";
        }
        return aesHandler(plainData, password, true);
    }

    private static byte[] aesHandler(byte[] toBeHandleData, String password, boolean isEncrypt) {
        try {
            SecretKeySpec key = new SecretKeySpec(toKey(password), "AES");
            Cipher cipher = Cipher.getInstance("AES/CFB8/NoPadding");
            IvParameterSpec initialParameter = new IvParameterSpec(IV);
            cipher.init(isEncrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, key, initialParameter);// 初始化
            return cipher.doFinal(toBeHandleData);
        } catch (Exception e) {
            return null;
        }
    }

    private static byte[] toKey(String toBeHashedString) {
        try {
            MessageDigest digestAlgorithm = MessageDigest.getInstance("MD5");
            digestAlgorithm.reset();
            digestAlgorithm.update(toBeHashedString.getBytes());
            return digestAlgorithm.digest();
        } catch (Exception e) {
            return null;
        }
    }

    private static byte[] combinePlainDataWithChecksum(byte[] plainData) {
        byte[] checksum = calculateChecksum(plainData);
        byte[] combineResult = new byte[plainData.length + checksum.length];
        System.arraycopy(plainData, 0, combineResult, 0, plainData.length);
        System.arraycopy(checksum, 0, combineResult, plainData.length, checksum.length);
        return combineResult;
    }

    private static byte[] calculateChecksum(byte[] plainData) {
        CRC32 crc32 = new CRC32();
        crc32.update(plainData);
        return toBytes(crc32.getValue());
    }

    private static byte[] mixPlainData(byte[] plainData, byte mixDataSize) {
        if (plainData == null) {
            return plainData;
        }
        //第一位为混淆数的长度，最多mixDataSize个混淆数；紧接着下来的是混淆数；剩下的就是待加密数据
        byte[] toBeEncrypt = new byte[1 + mixDataSize + plainData.length];
        Random random = new Random();
        toBeEncrypt[0] = mixDataSize;
        for (int i = 0; i < toBeEncrypt[0]; i++) {
            toBeEncrypt[i + 1] = (byte) Math.abs(random.nextInt() % 127);
        }
        System.arraycopy(plainData, 0, toBeEncrypt, 1 + toBeEncrypt[0], plainData.length);
        return toBeEncrypt;
    }

    private static byte[] toBytes(long input) {
        byte[] output = new byte[4];
        for (int i = 0; i < output.length; i++) {
            output[i] = (byte) ((input >>> (i * 8)) & 0xff);//big-endian
        }
        return output;
    }


    public static String decrypt(String cipherData, String password) {
        if (cipherData == null || !cipherData.matches("[0-9a-zA-Z*_-]+")) {
            return null;
        }
        return decrypt(cipherData, password, false);
    }

    private static String decrypt(String cipherData, String password, boolean caseInsensitive) {
        if (cipherData == null) {
            return null;
        }
        byte[] cipherDataArray = caseInsensitive ? StringUtils.hexString2Bytes(cipherData) : Base64.decodeWithDajieSpec(cipherData);
        byte[] plainDataArray = verifyChecksum(decrypt(cipherDataArray, password));
        if (plainDataArray == null || plainDataArray.length == 0) {
            return null;
        }
        String result = null;
        try {
            result = new String(retrieveToBeDecryptData(plainDataArray), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static byte[] verifyChecksum(byte[] plainData) {
        if (plainData == null || plainData.length <= 4) {
            return null;
        }
        byte[] checksum = new byte[4];
        byte[] srcData = new byte[plainData.length - checksum.length];
        System.arraycopy(plainData, 0, srcData, 0, srcData.length);
        System.arraycopy(plainData, srcData.length, checksum, 0, checksum.length);
        return isEqualByteArray(calculateChecksum(srcData), checksum) ? srcData : null;
    }

    private static boolean isEqualByteArray(byte[] array1, byte[] array2) {
        if (array1 == null || array2 == null) {
            return array1 == null && array2 == null;
        }
        boolean isEqual = true;
        for (int i = 0; i < array1.length; i++) {
            isEqual = array1[i] == array2[i];
            if (!isEqual) {
                break;
            }

        }
        return isEqual;
    }

    private static byte[] retrieveToBeDecryptData(byte[] cipherData) {
        if (cipherData == null) {
            return cipherData;
        }
        if (cipherData[0] < 0 || cipherData[0] >= cipherData.length) {
            List<Byte> list = new ArrayList<Byte>();
            for (byte aCipherData : cipherData) {
                list.add(aCipherData);
            }
            throw new IllegalArgumentException("Illegal confound size,maybe there is an error when decrypt.The decrypted data is " + StringUtils.join(list, ","));
        }
        byte[] toBeDecryptData = new byte[cipherData.length - cipherData[0] - 1];
        System.arraycopy(cipherData, 1 + cipherData[0], toBeDecryptData, 0, toBeDecryptData.length);
        return toBeDecryptData;
    }

    private static byte[] decrypt(byte[] cipherData, String password) {
        if (cipherData == null || cipherData.length == 0) {
            return cipherData;
        }
        if (password == null) {
            password = "";
        }
        return aesHandler(cipherData, password, false);
    }

}
