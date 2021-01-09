package com.jzq.jdk.uml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Modifier;
import java.util.Collection;

public class Uml {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public void generate(UmlScriptGenerateContext context) throws IOException {
        logger.info("step 1: select class");
        Collection<Class<?>> classes = new UmlElementSelector().select(context);

        logger.info("step 2: generate uml script");
        UmlScriptGenerator generator = new UmlScriptGenerator();
        generator.generate(classes, context);

        logger.info("step 3: write to file");
        write(generator.getClasses(), generator.getRelations(), getWriter());

        logger.info("finished");
    }

    private Writer getWriter() throws FileNotFoundException {
        return new OutputStreamWriter(new FileOutputStream(new File("output.txt")));
    }

    private void write(Collection<Class<?>> classes, Collection<ClassRelation> relations, Writer writer) throws IOException {
        for (Class<?> clazz : classes) {
            String prefix;
            if (clazz.isInterface()) {
                if (clazz.isAnnotation()) {
                    prefix = "annotation ";
                } else {
                    prefix = "interface ";
                }
            } else if (Modifier.isAbstract(clazz.getModifiers())) {
                prefix = "abstract class ";
            } else if (clazz.isEnum()) {
                prefix = "enum ";
            } else {
                prefix = "class ";
            }
            writer.write(prefix + generateClassName(clazz) + "\n");
        }
        writer.write("\n");

        for (ClassRelation relation : relations) {
            writer.write(generateClassName(relation.getA()) + relation.getRelation().getRelation()
                    + generateClassName(relation.getB()) + "\n");
        }
        writer.flush();
    }

    private String generateClassName(Class<?> c) {
//        String[] split = c.getName().split("\\.");
//        StringBuilder builder = new StringBuilder();
//        for (int i = 0; i < split.length; i++) {
//            if (i < split.length - 1) {
//                builder.append(split[i].charAt(0)).append(".");
//            } else {
//                builder.append(split[i]);
//            }
//        }
//        return builder.toString();

        return c.getName();
    }
}
