package com.jzq.jdk.uml;

import com.jzq.jdk.uml.selector.*;

import java.util.regex.Pattern;

public class UmlScriptGenerateContext {
    private UmlClassSelector umlClassSelector;
    private UmlPackageSelector umlPackageSelector;
    private UmlJarSelector umlJarSelector;
    private UmlScriptGenerateCondition umlScriptGenerateCondition;

    private UmlScriptGenerateContext() {
        umlClassSelector = UmlFunctionSelectors.newUmlClassSelector(s -> !s.contains("$"), c -> true);
        umlPackageSelector = UmlFunctionSelectors.newUmlPackageSelector(p -> true);
        umlJarSelector = UmlFunctionSelectors.newUmlJarSelector(j -> j.endsWith("rt.jar"));
        umlScriptGenerateCondition = UmlScriptGenerateConditions.newDefaultCondition(c -> true);
    }

    public UmlJarSelector getUmlJarSelector() {
        return umlJarSelector;
    }

    public UmlPackageSelector getUmlPackageSelector() {
        return umlPackageSelector;
    }

    public UmlClassSelector getUmlClassSelector() {
        return umlClassSelector;
    }

    public UmlScriptGenerateCondition getUmlScriptGenerateCondition() {
        return umlScriptGenerateCondition;
    }

    public static UmlScriptGenerateContextBuilder builder() {
        return new UmlScriptGenerateContextBuilder();
    }

    public static class UmlScriptGenerateContextBuilder {
        private UmlScriptGenerateContext context = new UmlScriptGenerateContext();
        public UmlScriptGenerateContextBuilder umlJarSelector(UmlJarSelector umlJarSelector) {
            context.umlJarSelector = umlJarSelector;
            return this;
        }

        public UmlScriptGenerateContextBuilder umlClassSelector(UmlClassSelector umlClassSelector) {
            context.umlClassSelector = umlClassSelector;
            return this;
        }

        public UmlScriptGenerateContextBuilder umlPackageSelector(UmlPackageSelector umlPackageSelector) {
            context.umlPackageSelector = umlPackageSelector;
            return this;
        }

        public UmlScriptGenerateContextBuilder umlScriptGenerateCondition(UmlScriptGenerateCondition umlScriptGenerateCondition) {
            context.umlScriptGenerateCondition = umlScriptGenerateCondition;
            return this;
        }

        public UmlScriptGenerateContext build() {
            return context;
        }

    }
}
