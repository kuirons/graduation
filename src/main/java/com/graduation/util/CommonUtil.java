package com.graduation.util;

import org.apache.commons.lang3.StringUtils;

import java.nio.charset.Charset;

/**
 * @author WuGYu
 * @date 2018/4/27 19:55
 */
public class CommonUtil {
  public static String bytesToString(byte[] bytes) {
    return StringUtils.toEncodedString(bytes, Charset.forName("UTF-8"));
  }
}
