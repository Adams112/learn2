package com.jzq.jdk.uml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class UmlElementSelector {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public Collection<Class<?>> select(UmlScriptGenerateContext context) throws IOException {
        String split = System.getenv("OS").startsWith("Windows") ? ";" : ":";
        String[] jarPaths = System.getProperty("java.class.path").split(split);
        HashSet<Class<?>> classes = new HashSet<>();
        for (String jarFullPath : jarPaths) {
            if (!context.getUmlJarSelector().qualified(jarFullPath))
                continue;
            if (jarFullPath.endsWith(".jar")) {
                logger.info("going through jar: {}", jarFullPath);
                JarFile jarFile = new JarFile(jarFullPath);
                Enumeration<JarEntry> classFullNames = jarFile.entries();
                while (classFullNames.hasMoreElements()) {
                    String classFullName = fileToClassName(classFullNames.nextElement().getName());
                    addClass(classes, classFullName, context);
                }
            } else {
                logger.info("going through directory: {}", jarFullPath);
                File file = new File(jarFullPath);
                Deque<File> directories = new ArrayDeque<>();
                if (file.isDirectory()) {
                    directories.addLast(file);
                }
                int len = jarFullPath.length() + 1;

                while (!directories.isEmpty()) {
                    File curFile = directories.pollFirst();
                    File[] files = curFile.listFiles();
                    for (File subFile : files) {
                        if (subFile.isDirectory()) {
                            directories.addLast(subFile);
                        } else if (subFile.isFile()) {
                            String subFileAbsolutePath = subFile.getAbsolutePath();
                            String classFullName = fileToClassName(subFileAbsolutePath.substring(len));
                            addClass(classes, classFullName, context);
                        }
                    }
                }
            }
        }
        logger.info("found {} classes", classes.size());
        return classes;
    }

    private void addClass(Collection<Class<?>> classes, String classFullName, UmlScriptGenerateContext context) {
        if (classFullName != null && !classFullName.endsWith("package-info")) {
            String packageName = classToPackage(classFullName);
            if (context.getUmlPackageSelector().qualified(packageName)
                    && context.getUmlClassSelector().qualifiedBeforeLoadClass(classFullName)) {
                Class<?> c = null;
                Throwable t = null;
                try {
                    c = Class.forName(classFullName);
                } catch (Throwable e) {
                    t = e;
                }
                if (c == null) {
                    logger.info("add class {} failed[{}]", classFullName, t.getMessage());
                } else if (context.getUmlClassSelector().qualifiedAfterLoadClass(c)) {
                    logger.info("add class {} success", classFullName);
                    classes.add(c);
                }
            }
        }
    }

    private String fileToClassName(String fileName) {
        if (fileName == null || !fileName.endsWith(".class"))
            return null;
        return fileName.substring(0, fileName.length() - 6).replace('\\', '.')
                .replace('/', '.');
    }

    private String classToPackage(String className) {
        int index = className.indexOf('.');
        return index >= 0 ? className.substring(0, className.lastIndexOf('.')) : "";
    }

    public static void main(String[] args) throws IOException {

    }
}
