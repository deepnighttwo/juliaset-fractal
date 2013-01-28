/**
 * This source code belongs to Mark Zang, the author. To use it for
 * commercial/business purpose, please contact DeepNightTwo@gmail.com
 * 
 * @author Mark Zang
 * 
 */

package com.deepnighttwo.ui.guibuilder.componets;

import javax.swing.JComponent;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.deepnighttwo.ui.componets.MyJSpinner;
import com.deepnighttwo.ui.guibuilder.PropertyLine;
import com.deepnighttwo.ui.guibuilder.xml.ReflectSetValueException;
import com.deepnighttwo.util.ReflectUtility;

public class SpinnerInt extends PropertyLine {

    private int value;

    private int step;

    private int max;

    private int min;

    private MyJSpinner spinner;

    @Override
    public JComponent getContent() {
        final SpinnerNumberModel model = new SpinnerNumberModel(value, min, max, step);
        spinner = new MyJSpinner(model);
        model.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                int newValue = spinner.getIntValue();
                value = newValue;
                valueChanged(newValue);
            }

        });
        try {
            ReflectUtility.setValueReflect(beanClass, args, fieldName, value);
        } catch (ReflectSetValueException ex) {
            ex.printStackTrace();
        }
        return spinner;
    }

    public int getValue() {
        return value;
    }

    public void setValueForAll(int value) {
        try {
            ReflectUtility.setValueReflect(beanClass, args, fieldName, value);
        } catch (ReflectSetValueException ex) {
            ex.printStackTrace();
        }
        this.value = value;
        spinner.setValue(value);
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

}
