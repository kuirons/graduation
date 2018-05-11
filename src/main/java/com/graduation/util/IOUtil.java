package com.graduation.util;

import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.io.IOException;
import java.io.InputStream;

public class IOUtil {
  public static byte[] readBytes(InputStream is) throws IOException {
    Preconditions.checkNotNull(is);
    try {
      ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
      try {
        byte[] tmp = new byte[1024];
        int read;
        while ((read = is.read(tmp)) > 0) {
          ByteBufUtil.writeBytes(buf, tmp, 0, read);
        }
        return ByteBufUtil.toByteArray(buf);
      } finally {
        buf.release();
      }
    } finally {
      is.close();
    }
  }
}
