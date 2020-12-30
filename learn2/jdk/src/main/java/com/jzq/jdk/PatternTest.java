package com.jzq.jdk;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternTest {
    public static void main(String[] args) {
        Pattern pattern1 = Pattern.compile("<body><h1>.*</h1></body>");
        Pattern pattern2 = Pattern.compile("(<body>(<h1>.*</h1>)</body>)");
        Pattern pattern3 = Pattern.compile("(<(?<b>body)>(<(?<h>h1)>.*</\\k<h>>)</\\k<b>>)");

        String text = "<body><h1>its h1</h1></body>";
        Matcher[] matchers = new Matcher[3];
        matchers[0] = pattern1.matcher(text);
        matchers[1] = pattern2.matcher(text);
        matchers[2] = pattern3.matcher(text);

        for (Matcher matcher : matchers) {
            matcher.find();
            System.out.println("groupCount: " + matcher.groupCount());
            for (int i = 0; i <= matcher.groupCount(); i++) {
                System.out.println(matcher.group(i));
            }
            System.out.println();
        }
    }
}
