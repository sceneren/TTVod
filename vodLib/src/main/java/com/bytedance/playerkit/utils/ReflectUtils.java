/*
 * Copyright (C) 2021 bytedance
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Create Date : 2021/12/3
 */

package com.bytedance.playerkit.utils;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;


public class ReflectUtils {

    public static Class<?> getInnerClass(Class<?> outerClazz, String innerClassName) {
        try {
            Class<?> innerClass = Class.forName(outerClazz.getName() + "$" + innerClassName);
            return innerClass;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T getField(Object o, Class<?> clazz, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            Object object = field.get(o);
            if (object != null) {
                return (T) object;
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T invokeMethod(Object o, Class<?> clazz, String methodName) {
        try {
            Method method = clazz.getMethod(methodName);
            return (T) method.invoke(o);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setStaticFiledValue(Class<?> owner, Class<?> filedClass, String filedName, Object staticFiled) {
        Field targetField = null;
        Field[] fields = owner.getDeclaredFields();
        for (Field field : fields) {
            if (field.getType() == filedClass && Modifier.isStatic(field.getModifiers())) {
                if (TextUtils.equals(field.getName(), filedName)) {
                    targetField = field;
                    break;
                }
            }
        }
        if (targetField != null) {
            targetField.setAccessible(true);
            try {
                targetField.set(null, staticFiled);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @NonNull
    public static <T> T newInstance(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true); // 允许访问私有构造函数
            return (T) constructor.newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException |
                 NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
