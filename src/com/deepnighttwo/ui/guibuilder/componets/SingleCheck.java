/**
 * This source code belongs to Mark Zang, the author. To use it for
 * commercial/business purpose, please contact DeepNightTwo@gmail.com
 * 
 * @author Mark Zang
 * 
 */

package com.deepnighttwo.ui.guibuilder.componets;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JComponent;

import com.deepnighttwo.ui.guibuilder.PropertyLine;
import com.deepnighttwo.ui.guibuilder.xml.ReflectSetValueException;
import com.deepnighttwo.util.ReflectUtility;

public class SingleCheck extends PropertyLine {

    private boolean value;

    @Override
    public JComponent getContent() {
        final JCheckBox checkBox = new JCheckBox(propLabel, value);
        checkBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                boolean newValue = checkBox.isSelected();
                value = newValue;
                valueChanged(newValue);
            }

        });
        try {
            ReflectUtility.setValueReflect(beanClass, args, fieldName, value);
        } catch (ReflectSetValueException ex) {
            ex.printStackTrace();
        }
        return checkBox;
    }

    @Override
    public JComponent getLabel() {
        return null;
    }

    public boolean isValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }
}
