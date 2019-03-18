package com.xiaojiezhu.springbootplus.cache;

/**
 * time 2019/3/18 11:41
 *
 * @author xiaojie.zhu <br>
 */
public enum  DataType {

    /**
     * 自动识别，无法识别 Object 类型，此类型必须手工指定
     */
    AUTO,

    INT,

    SHORT,

    LONG,

    FLOAT,

    DOUBLE,

    STRING,

    DATE,
    /**
     * java bean 或 map ,list ,set 等框架集合，将会以 json 的形式序列化
     */
    JSON
}
