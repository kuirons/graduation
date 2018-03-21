package com.graduation.log;

import com.graduation.util.ReflectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author WuGYu
 * @date 2018/3/21 15:24
 */
public class LogServer {
  private static final Logger RUNNING_LOG = LoggerFactory.getLogger("RUNNING_LOG");
  private static final Logger LOGGER = LoggerFactory.getLogger(LogServer.class);
  private Map<Class<? extends AbstractLog>, List<Field>> logClazz =
      new HashMap<Class<? extends AbstractLog>, List<Field>>();
  private ExecutorService logExecutor =
      Executors.newSingleThreadExecutor(
          r -> {
            Thread t = new Thread(r);
            t.setName("running_log");
            return t;
          });

  // 一般不会用到
  public void shutdown() {
    logExecutor.shutdown();
  }

  public void log(AbstractLog log) {
    logExecutor.execute(
        () -> {
          Class<? extends AbstractLog> clazz = log.getClass();
          // 类名转化为小写
          String name = clazz.getSimpleName().toLowerCase();
          List<Field> fields = getFields(clazz);
          StringBuilder builder = new StringBuilder();
          builder.append(name).append("|");
          try {
            for (Field field : fields) {
              builder.append(field.get(log)).append("|");
            }
          } catch (IllegalAccessException e) {
            LOGGER.error(e.getMessage());
            return;
          }
          RUNNING_LOG.info(builder.deleteCharAt(builder.length() - 1).toString());
        });
  }

  private List<Field> getFields(Class<? extends AbstractLog> clazz) {
    // 只会在第一次写入日志的时候通过反射寻找，之后可以直接使用
    List<Field> fields = logClazz.get(clazz);
    if (fields == null) {
      fields = ReflectUtil.getFields(clazz, true);
      fields.forEach(f -> f.setAccessible(true));
    }
    return fields;
  }
}
