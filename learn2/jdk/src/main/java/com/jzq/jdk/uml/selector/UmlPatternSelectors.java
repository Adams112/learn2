package com.jzq.jdk.uml.selector;

import java.util.function.Function;
import java.util.regex.Pattern;

public final class UmlPatternSelectors {
    public static UmlPackageSelector newUmlPackageSelector(Pattern pattern) {
        return packageFullPath -> pattern.matcher(packageFullPath).matches();
    }

    public static UmlJarSelector newUmlJarSelector(Pattern pattern) {
        return jarFullPath -> pattern.matcher(jarFullPath).matches();
    }

    public static UmlClassSelector newUmlClassSelector(Pattern pattern, Function<Class<?>, Boolean> function) {
        return new UmlClassSelector() {
            @Override
            public boolean qualifiedBeforeLoadClass(String classFullPath) {
                return pattern.matcher(classFullPath).matches();
            }

            @Override
            public boolean qualifiedAfterLoadClass(Class<?> clazz) {
                return function.apply(clazz);
            }
        };
    }
}
