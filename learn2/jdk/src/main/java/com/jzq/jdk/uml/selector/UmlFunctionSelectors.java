package com.jzq.jdk.uml.selector;

import java.util.function.Function;
import java.util.regex.Pattern;

public class UmlFunctionSelectors {
    public static UmlPackageSelector newUmlPackageSelector(Function<String, Boolean> function) {
        return function::apply;
    }

    public static UmlJarSelector newUmlJarSelector(Function<String, Boolean> function) {
        return function::apply;
    }

    public static UmlClassSelector newUmlClassSelector(Function<String, Boolean> function1,
                                                       Function<Class<?>, Boolean> function2) {
        return new UmlClassSelector() {
            @Override
            public boolean qualifiedBeforeLoadClass(String classFullPath) {
                return function1.apply(classFullPath);
            }

            @Override
            public boolean qualifiedAfterLoadClass(Class<?> clazz) {
                return function2.apply(clazz);
            }
        };
    }
}
