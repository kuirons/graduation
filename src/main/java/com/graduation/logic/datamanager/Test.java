package com.graduation.logic.datamanager;

import java.io.IOException;
import java.io.RandomAccessFile;

public class Test {
  public static void main(String[] args) throws IOException {
    //    byte[] result = FileUtil.readBytes("E:\\光电科创\\20160817
    // 交张欢数据\\16年07月18日_00时31分40秒.ocam2-dq");
    //    byte[] test = new byte[2];
    //    System.arraycopy(result, 8, test, 0, 2);
    //      double d = ByteBuffer.wrap(test).getShort() / 256.0;
    //    System.out.println(d/256.0);

    RandomAccessFile accessFile =
        new RandomAccessFile("E:\\光电科创\\20160817 交张欢数据\\16年07月18日_00时29分23秒.ocamdq-gv", "r");
    for (int i = 0; i < 500; i++) {
      System.out.println((accessFile.readShort() / 256.0)/256);
      accessFile.seek(1024 * 2 * (i + 1));
    }
  }
}
