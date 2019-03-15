package com.xiaojiezhu.springbootplus;

import java.util.concurrent.TimeUnit;

/**
 * time 2019/3/15 9:54
 *
 * @author xiaojie.zhu <br>
 */
public class DateUtil {

    public static long ms(long time , TimeUnit timeUnit){
        long t = -1;
        switch (timeUnit){
            case MILLISECONDS:{
                t = time;
                break;
            }
            case SECONDS:{
                t = time * 1000;
                break;
            }
            case MINUTES:{
                t = time * 60 * 1000;
                break;
            }
            case HOURS:{
                t = time * 60 * 60 * 1000;
                break;
            }
            case DAYS:{
                t = time * 24 * 60 * 60 * 1000;
                break;
            }
            default:{
                throw new IllegalArgumentException("不支持的时间格式:" + timeUnit);
            }
        }

        return t;
    }
}
