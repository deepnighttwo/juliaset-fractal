/**
 * This source code belongs to Moon Zang, the author.
 * To use it for commercial/business purpose, please contact DeepNightTwo@gmail.com
 * @author  Moon Zang
 * 
 */

package com.deepnighttwo.util;

import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.deepnighttwo.ui.guibuilder.xml.ReflectSetValueException;

public final class ReflectUtility {

    public static void setValueReflect(Class<?> clazz, Object inst, String propName, Object para)
            throws ReflectSetValueException {
        StringBuilder methodNameBuilder = new StringBuilder("set");
        methodNameBuilder.append(propName.substring(0, 1).toUpperCase());
        methodNameBuilder.append(propName.substring(1, propName.length()));

        String methodName = methodNameBuilder.toString();
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (methodName.equals(method.getName())) {
                Object paraValue = convertParameterValue(method.getParameterTypes()[0], para);
                try {
                    method.invoke(inst, new Object[] {
                        paraValue
                    });
                    return;
                } catch (IllegalArgumentException e) {
                    throw new ReflectSetValueException(e);
                } catch (IllegalAccessException e) {
                    throw new ReflectSetValueException(e);
                } catch (InvocationTargetException e) {
                    throw new ReflectSetValueException(e);
                }
            }
        }
    }

    public static Object convertParameterValue(Class<?> paraClazz, Object value) {
        if (paraClazz.isInstance(value)) {
            return value;
        }
        if (Integer.class == paraClazz || int.class == paraClazz) {
            return Integer.valueOf(value.toString());
        } else if (Double.class == paraClazz || double.class == paraClazz) {
            return Double.valueOf(value.toString());
        } else if (Long.class == paraClazz || long.class == paraClazz) {
            return Long.valueOf(value.toString());
        } else if (Short.class == paraClazz || short.class == paraClazz) {
            return Short.valueOf(value.toString());
        } else if (Boolean.class == paraClazz || boolean.class == paraClazz) {
            return Boolean.valueOf(value.toString());
        } else if (Color.class == paraClazz) {
            String[] ele = value.toString().split(";");
            return new Color(Integer.valueOf(ele[0]), Integer.valueOf(ele[1]), Integer.valueOf(ele[2]));
        } else if (String[].class == paraClazz) {
            return value.toString().split(";");
        }
        return null;
    }
}
