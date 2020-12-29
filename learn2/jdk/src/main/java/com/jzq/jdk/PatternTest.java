package com.jzq.jdk;

import java.util.regex.Pattern;

public class PatternTest {
    public static void main(String[] args) {

        boolean b1 = Pattern.matches("\\\\", "\\");

        // \143是java的转义，得到字符c。\\0143是匹配由八进制143表示的字符
        boolean b2 = Pattern.matches("\143", "" + 'c');
        boolean b3 = Pattern.matches("\\0143", "" + 'c');


        boolean b4 = Pattern.matches("\t", "" + '\u0009');
        boolean b5 = Pattern.matches("\\t", "" + '\u0009');

        System.out.println();
    }
}
