package com.jzq.jdk;

import java.io.*;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

public class UmlTest {

    private final Set<Class<?>> DEFAULT_EXCLUDE_CLASS;
    private final boolean UP = true;
    private final boolean DOWN = false;

    private final String EXTENDS = " <|-- ";
    private final String IMPLEMENTS = " <|.. ";

    public UmlTest() {
        DEFAULT_EXCLUDE_CLASS = new HashSet<Class<?>>();
        DEFAULT_EXCLUDE_CLASS.add(Object.class);
        DEFAULT_EXCLUDE_CLASS.add(Comparable.class);
        DEFAULT_EXCLUDE_CLASS.add(Cloneable.class);
        DEFAULT_EXCLUDE_CLASS.add(Serializable.class);
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
//        new UmlTest().listAllPath(Collections.singletonList("java.util.concurrent"), Collections.emptyList(), false, false);

        Pattern pattern1 = Pattern.compile("java\\.util\\.concurrent\\.[a-zA-Z]*");
        Pattern pattern2 = Pattern.compile(".*");
        new UmlTest().generate(pattern1, pattern2);
    }

    public void generate(Collection<Class<?>> classes, Pattern pattern) throws IOException {
        Deque<Class<?>> toHandle = new ArrayDeque<>(classes);
        HashSet<Class<?>> handled = new HashSet<>();
        HashSet<String> relations = new HashSet<>();

        while (!toHandle.isEmpty()) {
            Class<?> clazz = toHandle.pollFirst();
            handled.add(clazz);

            Class<?> superclass = clazz.getSuperclass();
            Class<?>[] interfaces = clazz.getInterfaces();

            if (superclass != null &&
                    !DEFAULT_EXCLUDE_CLASS.contains(superclass) &&
                    pattern.matcher(superclass.getName()).matches()) {
                if (!handled.contains(superclass)) {
                    toHandle.addLast(superclass);
                }
                String relation = superclass.getSimpleName() + EXTENDS + clazz.getSimpleName();
                relations.add(relation);
            }

            for (Class<?> inter : interfaces) {
                if (!DEFAULT_EXCLUDE_CLASS.contains(inter) && pattern.matcher(inter.getName()).matches()) {
                    if (!handled.contains(inter)) {
                        toHandle.addLast(inter);
                    }
                    String relation = inter.getSimpleName() + (clazz.isInterface() ? IMPLEMENTS : EXTENDS) + clazz.getSimpleName();
                    relations.add(relation);
                }
            }
        }

        write(handled, relations);
    }

    private void write(HashSet<Class<?>> classes, HashSet<String> relations) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(new File("output.txt")));
        for (Class<?> clazz : classes) {
            String prefix;
            if (clazz.isInterface()) {
                prefix = "interface ";
            } else if (Modifier.isAbstract(clazz.getModifiers())) {
                prefix = "abstract class ";
            } else if (clazz.isAnnotation()) {
                prefix = "annotation ";
            } else if (clazz.isEnum()) {
                prefix = "enum ";
            } else {
                prefix = "class ";
            }
            writer.write(prefix + clazz.getSimpleName() + "\n");
        }
        writer.write("\n");

        for (String relation : relations) {
            writer.write(relation + "\n");
        }
        writer.flush();
    }

    public void generate(Pattern pattern1, Pattern pattern2) throws IOException, ClassNotFoundException {
        generate(listClasses(pattern1), pattern2);
    }

    public HashSet<Class<?>> listClasses(Pattern pattern) throws IOException, ClassNotFoundException {
        String split = System.getenv("OS").startsWith("Windows") ? ";" : ":";
        String[] jarPaths = System.getProperty("java.class.path").split(split);
        HashSet<Class<?>> classes = new HashSet<>();

        for (String path : jarPaths) {
            if (path.endsWith(".jar")) {
                JarFile jarFile = new JarFile(path);
                Enumeration<JarEntry> enumFiles = jarFile.entries();
                while (enumFiles.hasMoreElements()) {
                    JarEntry entry = enumFiles.nextElement();
                    String fullName = entry.getName();
                    addClass(classes, fullName, pattern);
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
                            addClass(classes, subFileAbsolutePath.substring(len), pattern);
                        }
                    }
                }
            }
        }

        return classes;
    }

    private boolean addClass(HashSet<Class<?>> classes, String fullName, Pattern pattern) throws ClassNotFoundException {
        if (!fullName.endsWith(".class")) {
            return false;
        }

        fullName = fullName.substring(0, fullName.length() - 6).replace('/', '.').replace('\\', '.');
        if (fullName.contains("$")) {
            return false;
        }

        if (pattern.matcher(fullName).matches()) {
            classes.add(Class.forName(fullName));
            return true;
        }
        return false;
    }
}
