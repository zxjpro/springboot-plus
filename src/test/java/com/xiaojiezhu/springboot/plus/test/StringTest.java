package com.xiaojiezhu.springboot.plus.test;

import com.xiaojiezhu.springbootplus.StringUtil;
import org.junit.Test;

/**
 * time 2019/3/14 17:06
 *
 * @author xiaojie.zhu <br>
 */
public class StringTest {


    @Test
    public void test(){
        String format = StringUtil.format("hello{1}{2}world{4}", "bb", 12, "汉", 5, "img://");
        System.out.println(format);

        String format1 = StringUtil.format("{0}why{0}a", "张三");
        System.out.println(format1);
    }

}
