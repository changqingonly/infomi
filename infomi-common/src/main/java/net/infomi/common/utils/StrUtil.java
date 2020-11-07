package net.infomi.common.utils;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * 字符串常用函数类
 *
 * @author hongcq
 * @since 2020/07/01
 */
public class StrUtil {

    /**
     * <p>Creates a random string whose length is the number of characters
     * specified.</p>
     *
     * <p>Characters will be chosen from the set of Latin alphabetic
     * characters (a-z, A-Z) and the digits 0-9.</p>
     *
     * @param length  the length of random string to create
     * @return the random string
     */
    public static String getRandom(int length) {
        return RandomStringUtils.randomAlphanumeric(length);
    }

    public static void main(String[] args) {
        System.out.println(getRandom(16));
    }

}
