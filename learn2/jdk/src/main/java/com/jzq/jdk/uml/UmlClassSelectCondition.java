package com.jzq.jdk.uml;

public interface UmlClassSelectCondition {
    boolean jarQualified(String jarName);
    boolean packageQualified(String packageName);
    boolean classQualified(String className);
}
