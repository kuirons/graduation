package com.graduation.security;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;

import java.io.IOException;
import java.util.*;

/**
 * Created by kuirons on 18-4-22
 *
 * <p>key:url values:可通过的权限
 */
public class MySecurityContext {
  private static final Map<String, Collection<ConfigAttribute>> METADATA_SOURCE_MAP =
      new HashMap<>();

  public static Map<String, Collection<ConfigAttribute>> getMetadataSource() {
    if (MapUtils.isEmpty(METADATA_SOURCE_MAP)) {
      synchronized (MySecurityContext.class) {
        loadMetadataSource();
      }
    }
    return new HashMap<>(METADATA_SOURCE_MAP);
  }

  private static void loadMetadataSource() {
    if (MapUtils.isNotEmpty(METADATA_SOURCE_MAP)) {
      return;
    }
    try {
      ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
      Resource[] resources =
          resourcePatternResolver.getResources("classpath*:/security/*.properties");
      if (ArrayUtils.isEmpty(resources)) {
        return;
      }

      Properties properties = new Properties();
      for (Resource resource : resources) {
        properties.load(resource.getInputStream());
      }

      for (Map.Entry<Object, Object> entry : properties.entrySet()) {
        String key = (String) entry.getKey();
        String value = (String) entry.getValue();
        String[] values = StringUtils.split(value, ",");
        Collection<ConfigAttribute> configAttributes = new ArrayList<>();
        for (String s : values) {
          ConfigAttribute configAttribute = new SecurityConfig(s);
          configAttributes.add(configAttribute);
        }
        METADATA_SOURCE_MAP.put(StringUtils.trim(key), configAttributes);
      }
    } catch (IOException e) {
      throw new RuntimeException("加载MetadataSource失败", e);
    }
  }

  public static void main(String[] args) {
    MySecurityContext.getMetadataSource()
        .entrySet()
        .stream()
        .forEach(
            stringCollectionEntry -> {
              System.out.println(stringCollectionEntry.getKey());
              stringCollectionEntry.getValue().forEach(s -> System.out.println(s));
            });
  }
}
