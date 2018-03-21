package com.graduation.util;

import com.google.common.base.Predicate;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * @author WuGYu
 * @date 2018/3/21 16:23
 */
public class ReflectUtil {
  /**
   * 获取类的所有字段
   *
   * @param clazz
   * @param isSuper 是否迭代超类
   * @return
   */
  public static List<Field> getFields(Class<?> clazz, boolean isSuper) {
    return getFields(clazz, isSuper, field -> !isStatic(field));
  }

  public static boolean isStatic(Field field) {
    return Modifier.isStatic(field.getModifiers());
  }

  /**
   * @param clazz
   * @param isSuper
   * @param filter 静态变量
   * @return
   */
  public static List<Field> getFields(Class<?> clazz, boolean isSuper, Predicate<Field> filter) {
    List<Field> list = new ArrayList<>();
    Class<?> nextClass = clazz;
    do {
      Field[] declaredFields = nextClass.getDeclaredFields();
      for (Field field : declaredFields) {
        // 排除静态变量
        if (!filter.apply(field)) continue;
        list.add(field);
      }
      nextClass = nextClass.getSuperclass();
    } while (nextClass != Object.class && isSuper);
    return list;
  }
}
