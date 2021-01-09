package com.jzq.jdk.uml.selector;

public interface UmlClassSelector {
    boolean qualifiedBeforeLoadClass(String classFullPath);

    boolean qualifiedAfterLoadClass(Class<?> clazz);
}
