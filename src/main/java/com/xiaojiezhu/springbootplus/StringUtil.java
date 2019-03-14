package com.xiaojiezhu.springbootplus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * time 2019/3/14 17:00
 *
 * @author xiaojie.zhu <br>
 */
public class StringUtil {

    private static final Pattern PATTERN = Pattern.compile("\\{([\\d]{1})\\}");

    public static String format(String pattern , Object ... args){
        String p = pattern;

        // 存放对应参数的索引
        List<Integer> argIndexs = new ArrayList<>();
        List<String> splits = new ArrayList<>();

        Matcher matcher = PATTERN.matcher(p);

        int lastFindIndex = 0;

        while (matcher.find()){
            String tag = matcher.group();
            int argIndex = Integer.parseInt(matcher.group(1));
            argIndexs.add(argIndex);


            int index = p.indexOf(tag, lastFindIndex);
            splits.add(p.substring(lastFindIndex , index));
            lastFindIndex = index += 3;
        }

        String suffix = p.substring(lastFindIndex);

        int needArgLength = Collections.max(argIndexs) + 1;
        if(args.length < needArgLength){
            throw new IllegalArgumentException("format string fail , " + pattern + " required as least " + needArgLength + " args");
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < splits.size(); i++) {
            sb.append(splits.get(i));
            String value = String.valueOf(args[argIndexs.get(i)]);
            sb.append(value);
        }

        sb.append(suffix);


        return sb.toString();
    }


}
