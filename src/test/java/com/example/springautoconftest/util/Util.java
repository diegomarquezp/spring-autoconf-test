package com.example.springautoconftest.util;

import java.beans.PropertyDescriptor;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class Util {
  public static void printBeanPropertyKeysRecursively(Object bean, String prefix) {
    BeanWrapper beanWrapper = new BeanWrapperImpl(bean);
    PropertyDescriptor[] propertyDescriptors = beanWrapper.getPropertyDescriptors();
    for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
      String propertyName = propertyDescriptor.getName();
      Class<?> propertyType = propertyDescriptor.getPropertyType();
      if (propertyType != null) {
        if (!propertyName.equals("class")) {
          System.out.println(prefix + propertyName);
          Object nestedBean = beanWrapper.getPropertyValue(propertyName);
          if (nestedBean != null) {
            printBeanPropertyKeysRecursively(nestedBean, prefix + propertyName + ".");
          }
        }
      }
    }
  }
}
