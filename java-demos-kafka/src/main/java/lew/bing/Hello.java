package lew.bing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Hello {

    public static void main(String[] args) {
        String s = "Let us GO to THE park!";
        Pattern pattern = Pattern.compile("[A-Z]");
        Matcher matcher = pattern.matcher(s.substring(1));
        List<String> matches = new ArrayList<>();
        while (matcher.find()) {
            matches.add(matcher.group());
        }
        for (String next: matches) {
            s = s.replace(next,next.toLowerCase());
        }
        System.out.println(s);

        // 初始大小
        String[] str2 = new String[]{"a","ff"};
//        List<String> strings = Arrays.asList(str2);
        // 大小固定，会报错
//        strings.add("d");
    }

}
