package com.deepnighttwo.gen.task.impl;

import com.deepnighttwo.gen.imagegen.JuliaSetArgs;
import com.deepnighttwo.gen.task.IGenPostTask;
import com.deepnighttwo.gen.task.ITaskArg;
import com.deepnighttwo.ui.guibuilder.xml.ReflectSetValueException;
import com.deepnighttwo.util.ReflectUtility;

/**
 * Based on a given JuliaSetArgs. change the given arg-name/arg-value
 * 
 * @author mengzang
 * 
 */
public class CommonTaskArg implements ITaskArg {

    private static final Class<?> clz = JuliaSetArgs.class;

    public CommonTaskArg(JuliaSetArgs args, String argName, Object argValue, IGenPostTask genPostTask) {
        this.argName = argName;
        this.argValue = argValue;
        this.args = args;
        this.genPostTask = genPostTask;
    }

    public String argName;

    public Object argValue;

    public JuliaSetArgs args;

    public IGenPostTask genPostTask;

    public JuliaSetArgs getArgsForGen() {
        if (args == null) {
            return null;
        }
        JuliaSetArgs temp = args;
        args = null;
        if (argName == null) {
            return temp;
        }

        try {
            ReflectUtility.setValueReflect(clz, temp, argName, argValue);
            temp.ajustJuliaArgsForImageGen(argName);
        } catch (ReflectSetValueException e) {
            e.printStackTrace();
            return null;
        }
        return temp;
    }

    public void setArgs(JuliaSetArgs args) {
        this.args = args;
    }

    @Override
    public IGenPostTask getGenPostTask() {
        return genPostTask;
    }

    @Override
    public void setGenPostTask(IGenPostTask genPostTask) {
        this.genPostTask = genPostTask;
    }

    @Override
    public void taskFinished() {
    }

}
