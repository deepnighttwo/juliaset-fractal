/**
 * This source code belongs to Mark Zang, the author. To use it for
 * commercial/business purpose, please contact DeepNightTwo@gmail.com
 * 
 * @author Mark Zang
 * 
 */

package com.deepnighttwo.ui.guibuilder;

import javax.swing.JComponent;
import javax.swing.JLabel;

import com.deepnighttwo.gen.imagegen.JuliaSetArgs;
import com.deepnighttwo.gen.task.impl.CommonTaskArg;
import com.deepnighttwo.ui.argseditor.JuliaSetPanel;
import com.deepnighttwo.util.ResourceUtil;

public abstract class PropertyLine {

    protected String propLabel;

    // for set value
    protected String fieldName;

    protected Class<?> beanClass;

    protected JuliaSetPanel argsPanel;

    public JuliaSetArgs args;

    public JComponent getLabel() {
        String lblTxt = ResourceUtil.getString(getPropLabel());
        if (lblTxt == null || lblTxt.length() == 0) {
            return null;
        }
        return new JLabel(lblTxt);
    }

    public abstract JComponent getContent();

    protected void valueChanged(Object newValue) {
        argsPanel.addJuliaTask(new CommonTaskArg(args, fieldName, newValue, null), false);
    }

    public Class<?> getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    public JuliaSetPanel getArgsPanel() {
        return argsPanel;
    }

    public void setArgsPanel(JuliaSetPanel argsPanel) {
        this.argsPanel = argsPanel;
    }

    public String getPropLabel() {
        return propLabel;
    }

    public void setPropLabel(String propLabel) {
        this.propLabel = propLabel;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public JuliaSetArgs getArgs() {
        return args;
    }

    public void setArgs(JuliaSetArgs args) {
        this.args = args;
    }
}
