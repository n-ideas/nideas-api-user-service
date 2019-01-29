package com.nideas.api.userservice.util;

import java.util.StringJoiner;

/** Created by Nanugonda on 8/28/2018. */
public class ToString {

  private final StringJoiner stringJoiner;
  private final Class clazz;

  public static ToString csvBuilder(Class clazz) {
    return new ToString(", ", clazz);
  }

  public static ToString lineSeparatedBuilder(Class clazz) {
    return new ToString(System.lineSeparator(), clazz);
  }

  private ToString(String delimiter, Class clazz) {
    this.clazz = clazz;
    stringJoiner = new StringJoiner(delimiter);
  }

  public ToString add(String fieldName, Object value) {
    stringJoiner.add(fieldName + "='" + value.toString() + "'");
    return this;
  }

  public ToString append(String fieldName, Object value) {
    stringJoiner.add(fieldName + "='" + value.toString() + "'");
    return this;
  }

  @Override
  public String toString() {
    return clazz.getSimpleName() + "(" + stringJoiner.toString() + ")";
  }
}
