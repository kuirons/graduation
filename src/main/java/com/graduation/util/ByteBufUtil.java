package com.graduation.util;

import io.netty.buffer.ByteBuf;

public class ByteBufUtil {
  public static void writeBytes(ByteBuf buf, byte[] bytes, int srcIndex, int length) {
    buf.writeBytes(bytes, srcIndex, length);
  }

  public static byte[] toByteArray(ByteBuf buf) {
    int length = readableBytes(buf);
    if (length > 0) {
      byte[] bytes = new byte[length];
      readBytes(buf, bytes);
      return bytes;
    } else if (length == 0) {
      return null;
    } else {
      throw new IllegalArgumentException("内部错误,检查buf吧");
    }
  }

  public static int readableBytes(ByteBuf buf) {
    return buf.readableBytes();
  }

  public static int readBytes(ByteBuf buf, byte[] bytes) {
    int length = Math.min(readableBytes(buf), bytes.length);
    if (length > 0) {
      buf.readBytes(bytes);
      return length;
    } else if (length == 0) {
      return 0;
    } else {
      throw new IllegalArgumentException("内部错误,检查buf吧");
    }
  }

  public static void writeInt(ByteBuf buf, int value) {
    while (true) {
      if ((value & ~0x7F) == 0) {
        buf.writeByte(value);
        return;
      } else {
        buf.writeByte((value & 0x7F) | 0x80);
        value >>>= 7;
      }
    }
  }

  public static int readPositiveInt(ByteBuf buf) {
    try {
      return readInt(buf);
    } catch (IndexOutOfBoundsException e) {
      return -1;
    }
  }

  public static int readInt(ByteBuf buf) {
    byte tmp = buf.readByte();
    if (tmp >= 0) {
      return tmp;
    }
    int result = tmp & 0x7f;
    if ((tmp = buf.readByte()) >= 0) {
      result |= tmp << 7;
    } else {
      result |= (tmp & 0x7f) << 7;
      if ((tmp = buf.readByte()) >= 0) {
        result |= tmp << 14;
      } else {
        result |= (tmp & 0x7f) << 14;
        if ((tmp = buf.readByte()) >= 0) {
          result |= tmp << 21;
        } else {
          result |= (tmp & 0x7f) << 21;
          result |= (tmp = buf.readByte()) << 28;
          if (tmp < 0) {
            // Discard upper 32 bits.
            for (int i = 0; i < 5; i++) {
              if (buf.readByte() >= 0) {
                return result;
              }
            }
            throw new RuntimeException("Malformed VarInt");
          }
        }
      }
    }
    return result;
  }
}
