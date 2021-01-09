package com.jzq.jdk;

import com.jzq.jdk.uml.Uml;
import com.jzq.jdk.uml.UmlScriptGenerateConditions;
import com.jzq.jdk.uml.UmlScriptGenerateContext;
import com.jzq.jdk.uml.selector.UmlFunctionSelectors;
import org.springframework.beans.factory.Aware;

import java.io.IOException;

public class UmlMain {
    public static void main(String[] args) throws IOException {
        UmlScriptGenerateContext context = UmlScriptGenerateContext.builder()
                .umlJarSelector(UmlFunctionSelectors.newUmlJarSelector(j -> j.contains("spring")))
                .umlPackageSelector(UmlFunctionSelectors.newUmlPackageSelector(p -> p.contains("org.springframework")))
                .umlClassSelector(UmlFunctionSelectors.newUmlClassSelector(s -> !s.contains("$"), c -> c.isInterface() && !c.isAnnotation() && Aware.class.isAssignableFrom(c)))
                .umlScriptGenerateCondition(UmlScriptGenerateConditions.newDefaultCondition(c -> c.getName().startsWith("org.springframework")))
                .build();
        new Uml().generate(context);
    }
}
