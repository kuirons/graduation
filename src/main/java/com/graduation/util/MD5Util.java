package com.graduation.util;

import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

/** Created by kuirons on 18-4-7 */
public class MD5Util {
  private static final String SALT = "graduation";

  public static String getMd5String(String s) {
    return new Md5PasswordEncoder().encodePassword(s, SALT);
  }
}
