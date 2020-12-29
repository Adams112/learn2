package com.jzq.jdk;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class UmlTest {


    public static void main(String[] args) throws IOException, ClassNotFoundException {
        new UmlTest().listAllPath(Collections.singletonList("java.util.concurrent"), Collections.emptyList(), false, false);
    }


    public HashSet<Class<?>> listAllPath(List<String> basePackages, List<String> excludes, boolean recursive,
                                                boolean includeInner)
            throws IOException, ClassNotFoundException {
        String[] jarPaths = System.getProperty("java.class.path").split(";");
        HashSet<Class<?>> classes = new HashSet<>();

        for (String path : jarPaths) {
            if (path.endsWith(".jar")) {
                JarFile jarFile = new JarFile(path);
                Enumeration<JarEntry> enumFiles = jarFile.entries();
                while (enumFiles.hasMoreElements()) {
                    JarEntry entry = enumFiles.nextElement();
                    String fullName = entry.getName();
                    addClass(classes, fullName, basePackages, excludes, recursive, includeInner);
                }
            } else {
                File file = new File(path);
                Deque<File> directories = new ArrayDeque<>();
                if (file.isDirectory()) {
                    directories.addLast(file);
                }
                int len = path.length() + 1;

                while (!directories.isEmpty()) {
                    File curFile = directories.pollFirst();
                    File[] files = curFile.listFiles();
                    for (File subFile : files) {
                        if (subFile.isDirectory()) {
                            directories.addLast(subFile);
                        } else if (subFile.isFile()) {
                            String subFileAbsolutePath = subFile.getAbsolutePath();
                            addClass(classes, subFileAbsolutePath.substring(len), basePackages, excludes, recursive,
                                    includeInner);
                        }
                    }
                }
            }

        }
        return classes;
    }

    private boolean addClass(HashSet<Class<?>> classes, String fullName, List<String> basePackages, List<String> excludes,
                             boolean recursive, boolean includeInner) throws ClassNotFoundException {
        if (!fullName.endsWith(".class")) {
            return false;
        }

        fullName = fullName.substring(0, fullName.length() - 6).replace('/', '.').replace('\\', '.');
        if (!includeInner && fullName.contains("$")) {
            return false;
        }

        boolean matched = false;
        for (String basePackage : basePackages) {
                if (fullName.startsWith(basePackage) &&
                        (recursive || !fullName.substring(basePackage.length() + 1).contains("."))) {
                    matched = true;
                    break;
                }
        }
        if (!matched) {
            return false;
        }

        boolean excluded = false;
        for (String exclude : excludes) {
            if (fullName.startsWith(exclude)) {
                excluded = true;
                break;
            }
        }
        if (!excluded) {
            classes.add(Class.forName(fullName));
        }
        return !excluded;
    }
}
