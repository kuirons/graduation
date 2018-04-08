//package com.graduation.security;
//
//import org.springframework.security.access.ConfigAttribute;
//import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
//
//import java.util.Collection;
//
///**
// * 这个先不用，直接在方法上进行管理，如果确实需要对url进行权限管理，则通过这个来配置
// *
// * @author WuGYu
// * @date 2018/4/8 17:38
// */
//public class MySecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
//    @Override
//    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
//        return null;
//    }
//
//    @Override
//    public Collection<ConfigAttribute> getAllConfigAttributes() {
//        return null;
//    }
//
//    @Override
//    public boolean supports(Class<?> aClass) {
//        return true;
//    }
//}
