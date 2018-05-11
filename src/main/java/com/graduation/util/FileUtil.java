package com.graduation.util;

import io.netty.buffer.ByteBufAllocator;

import java.io.*;

import static com.google.common.base.Preconditions.checkNotNull;

public class FileUtil {
  public static byte[] readBytes(String file) throws IOException {
    return readBytes(newFileInputStream(file));
  }

  public static byte[] readBytes(File file) throws IOException {
    return readBytes(new FileInputStream(file));
  }

  public static byte[] readBytes(InputStream fis) throws IOException {
    checkNotNull(fis, "文件为空");
    return IOUtil.readBytes(fis);
  }

  public static void writeBytes(byte[] bytes, File to) throws IOException {
    checkNotNull(to, "文件为空");
    checkNotNull(bytes, "你让我写什么到%s？", to.getAbsolutePath());
    FileOutputStream fos = new FileOutputStream(to);
    try {
      fos.write(bytes);
    } finally {
      fos.close();
    }
  }

  public static File getFileInDir(File dir, String fileName) {
    checkNotNull(dir);
    for (File file : dir.listFiles()) {
      if (file.getName().equals(fileName)) {
        return file;
      }
    }
    return null;
  }

  /**
   * 先从当前目录查找文件，如果没有则从resource中查找
   *
   * @param name
   * @return
   */
  public static InputStream newFileInputStream(String name) throws FileNotFoundException {
    return newFileInputStream(name, ClassLoader.getSystemClassLoader());
  }

  public static InputStream newFileInputStream(String name, ClassLoader loader)
      throws FileNotFoundException {
    File file = new File(name);
    if (file.exists()) {
      return new FileInputStream(file);
    } else {
      return checkNotNull(
          loader.getResourceAsStream(name), "can not find %s", loader.getResource(name).getPath());
    }
  }

  /**
   * 删除文件夹及所有的文件
   *
   * @param dir
   */
  public static void deleteDir(File dir) throws IOException {
    if (dir == null || !dir.exists()) {
      return;
    }
    if (dir.delete()) {
      System.err.println(dir.exists() + ":" + dir.getAbsolutePath());
      return;
    }
    for (File file : dir.listFiles()) {
      deleteDir(file);
    }
    if (dir.delete()) {
      System.err.println(dir.exists() + ":" + dir.getAbsolutePath());
      return;
    }
    throw new IOException("can not remove file:" + dir.getAbsolutePath());
  }

  /**
   * 将文件所有内容读取到bytebuf中，注意该bytebuf需要手动释放，否则会有内存泄露
   *
   * @param fileName
   * @return
   * @throws Exception
   */
  public static io.netty.buffer.ByteBuf read(String fileName) throws Exception {
    io.netty.buffer.ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
    try {
      FileInputStream in = new FileInputStream(fileName);
      try {
        byte[] tmp = new byte[128];
        int len;
        while ((len = in.read(tmp)) != -1) {
          buf.writeBytes(tmp, 0, len);
        }
      } finally {
        in.close();
      }
    } catch (Exception e) {
      buf.release();
      throw e;
    }
    return buf;
  }

  /**
   * 创建文件，如果父目录不存在，则依次创建
   *
   * @param file
   * @return
   */
  public static boolean mkDir(File file) {
    if (file.getParentFile().exists()) {
      return file.mkdir();
    } else {
      mkDir(file.getParentFile());
      return file.mkdir();
    }
  }

  public static String readString(String fn, ClassLoader loader) throws IOException {
    StringBuilder sb = new StringBuilder();
    byte[] buf = new byte[1024];
    try (InputStream fis = newFileInputStream(fn, loader)) {
      int len;
      while ((len = fis.read(buf)) > 0) {
        sb.append(new String(buf, 0, len));
      }
    }
    return sb.toString();
  }
}
