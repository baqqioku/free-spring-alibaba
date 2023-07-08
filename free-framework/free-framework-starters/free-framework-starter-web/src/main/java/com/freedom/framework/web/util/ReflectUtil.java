package com.freedom.framework.web.util;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ReflectUtil {

    /**
     * 判断是否是List或者ArrayList
     *
     * @param field
     * @return
     */
    public static boolean isList(Field field) {
        boolean flag = false;
        String simpleName = field.getType().getSimpleName();
        if (simpleName.contains("List")) {
            flag = true;
        }
        return flag;
    }

    /**
     * 判断是否是set
     *
     * @param field
     * @return
     */
    public static boolean isSet(Field field) {
        boolean flag = false;
        String simpleName = field.getType().getSimpleName();
        if (simpleName.contains("Set")) {
            flag = true;
        }
        return flag;
    }

    /**
     * 判断是否是Map或者HashMap
     *
     * @param field
     * @return
     */
    public static boolean isMap(Field field) {
        boolean flag = false;
        String simpleName = field.getType().getSimpleName();
        if ("Map".equals(simpleName) || "HashMap".equals(simpleName)) {
            flag = true;
        }
        return flag;
    }


    /**
     * 根据字段名查询对应字段的属性值。
     * @param parentCurrentName 字段名
     * @param clazz 对象
     * @return
     */
    public static Class getFieldType(String parentCurrentName, Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            String name = field.getName();
            Type genericType = field.getGenericType();
            if (parentCurrentName.equals(name)) {
                if (isList(field) || isSet(field)) {
                    if (genericType instanceof ParameterizedType) {
                        ParameterizedType parameterizedType = (ParameterizedType) genericType;
                        Type actualTypeArgument = parameterizedType.getActualTypeArguments()[0];
                        return (Class) actualTypeArgument;
                    }
                } else if (field.getType().isArray()) {
                    return field.getType().getComponentType();

                } else if (isMap(field)) {
                    if (genericType instanceof ParameterizedType) {
                        ParameterizedType parameterizedType = (ParameterizedType) genericType;
                        Type actualTypeArgument = parameterizedType.getActualTypeArguments()[1];
                        return (Class) actualTypeArgument;
                    }
                } else {
                    return field.getType();
                }
            }
        }
        return null;
    }
}

