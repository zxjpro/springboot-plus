package com.xiaojiezhu.springbootplus;

import com.xiaojiezhu.springbootplus.cache.DataType;
import com.xiaojiezhu.springbootplus.lock.annotation.PLock;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * time 2019/3/15 12:34
 *
 * @author xiaojie.zhu <br>
 */
public abstract class AbstractMethodInvocation implements MethodInvocation{
    private static final Object[] EMPTY = new Object[]{};

    private static final Pattern PATTERN = Pattern.compile("\\{([\\d]{1})\\}");

    private MethodContext methodContext;

    public AbstractMethodInvocation(MethodContext methodContext) {
        this.methodContext = methodContext;
    }

    /**
     * 获取注解的属性
     * @param method
     * @return
     */
    protected abstract AnnotationAttribute getAnnotationAttribute(Method method);
    protected abstract Object invoke0(ProceedingJoinPoint point , MethodInfo methodInfo) throws Throwable;


    @Override
    public Object invoke(ProceedingJoinPoint point) throws Throwable {

        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        MethodInfo methodInfo = methodContext.get(methodSignature.getMethod());
        if(methodInfo == null){
            methodInfo = createMethodInfo(point.getTarget() , point.getArgs(), methodSignature);
            methodContext.put(methodSignature.getMethod() , methodInfo);
        }

        return invoke0(point , methodInfo);
    }

    protected MethodInfo createMethodInfo(Object target , Object[] args, MethodSignature methodSignature) {
        Method method = methodSignature.getMethod();
        AnnotationAttribute annotationAttribute = getAnnotationAttribute(method);
        if(args == null){
            args = EMPTY;
        }

        String p = annotationAttribute.getPattern();

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

        int needArgLength = 0;
        if(argIndexs.size() != 0){
            needArgLength = Collections.max(argIndexs) + 1;
        }
        if(args.length < needArgLength){
            throw new IllegalArgumentException("format string fail , " + annotationAttribute.getPattern() + " required as least " + needArgLength + " args");
        }

        Assert.notNull(annotationAttribute.getTimeUnit() , methodSignature.toLongString() + " " + PLock.class.getSimpleName() + "() annotation timeUnit atrribute not be null");
        if(annotationAttribute.getTime() <= 0){
            throw new IllegalArgumentException(methodSignature.toLongString() + " " + PLock.class.getSimpleName() + "() annotation expireTime must > 0");
        }
        long expireMs = DateUtil.ms(annotationAttribute.getTime(), annotationAttribute.getTimeUnit());


        MethodInfo methodInfo = new MethodInfo();
        methodInfo.setReturnType(method.getReturnType());
        methodInfo.setDataType(annotationAttribute.getDataType());
        methodInfo.setClassName(target.getClass().getName());
        methodInfo.setSimpleClassName(target.getClass().getSimpleName());
        methodInfo.setMethodName(methodSignature.getMethod().getName());
        methodInfo.setExpireMs(expireMs);
        methodInfo.setSignature(methodSignature.toLongString());
        methodInfo.setArgIndexs(argIndexs);
        methodInfo.setSplitString(splits);
        methodInfo.setSuffix(suffix);
        return methodInfo;
    }



    protected DataType dataType(Method method){
        Assert.notNull(method , "method不能为空");

        Class<?> returnType = method.getReturnType();

        if(int.class == returnType || Integer.class == returnType){
            return DataType.INT;
        }else if(short.class == returnType || Short.class == returnType){
            return DataType.SHORT;
        }else if(long.class == returnType || Long.class == returnType){
            return DataType.LONG;
        }else if(float.class == returnType || Float.class == returnType){
            return DataType.FLOAT;
        }else if(double.class == returnType || Double.class == returnType){
            return DataType.DOUBLE;
        }else if(CharSequence.class.isAssignableFrom(returnType)){
            return DataType.STRING;
        }else if(Date.class.isAssignableFrom(returnType)){
            return DataType.DATE;
        }else{
            return DataType.JSON;
        }
    }





    public static class AnnotationAttribute{
        private String pattern;
        private long time;
        private TimeUnit timeUnit;

        /**
         * 数据类型
         */
        private DataType dataType;

        public String getPattern() {
            return pattern;
        }

        public DataType getDataType() {
            return dataType;
        }

        public void setDataType(DataType dataType) {
            this.dataType = dataType;
        }

        public void setPattern(String pattern) {
            this.pattern = pattern;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public TimeUnit getTimeUnit() {
            return timeUnit;
        }

        public void setTimeUnit(TimeUnit timeUnit) {
            this.timeUnit = timeUnit;
        }
    }

}
