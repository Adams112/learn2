package com.jzq.jdk.uml;

import java.io.Serializable;
import java.util.HashSet;
import java.util.function.Function;

public class UmlScriptGenerateConditions {
    private static final HashSet<Class<?>> DEFAULT_EXCLUDE_CLASS = new HashSet<>();

    static {
        DEFAULT_EXCLUDE_CLASS.add(Object.class);
        DEFAULT_EXCLUDE_CLASS.add(Comparable.class);
        DEFAULT_EXCLUDE_CLASS.add(Cloneable.class);
        DEFAULT_EXCLUDE_CLASS.add(Serializable.class);
        DEFAULT_EXCLUDE_CLASS.add(Iterable.class);
    }

    public static UmlScriptGenerateCondition newDefaultCondition(Function<Class<?>, Boolean> function) {
        return c -> !DEFAULT_EXCLUDE_CLASS.contains(c) && function.apply(c);
    }

}
