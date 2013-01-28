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

public strictfp class SpinnerDouble extends PropertyLine {

    private double value;

    private double step;

    private double max;

    private double min;

    boolean trigger = true;

    private MyJSpinner spinner;

    @Override
    public JComponent getContent() {
        final SpinnerNumberModel model = new SpinnerNumberModel(value, min, max, step);
        spinner = new MyJSpinner(model);

        model.addChangeListener(new ChangeListener() {
            double oldValue = Double.MAX_VALUE;
            double miniChange = 0.5 * step;

            @Override
            public void stateChanged(ChangeEvent e) {
                double newValue = spinner.getDoubleValue();
                if (Math.abs(oldValue - newValue) > miniChange) {
                    oldValue = newValue;
                    value = newValue;
                    if (trigger == true) {
                        valueChanged(newValue);
                    } else {
                        trigger = true;
                        try {
                            ReflectUtility.setValueReflect(beanClass, args, fieldName, newValue);
                        } catch (ReflectSetValueException ex) {
                            ex.printStackTrace();
                        }
                    }
                }

            }

        });
        try {
            ReflectUtility.setValueReflect(beanClass, args, fieldName, value);
        } catch (ReflectSetValueException ex) {
            ex.printStackTrace();
        }
        return spinner;
    }

    public void setTrigger(boolean trigger) {
        this.trigger = trigger;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getStep() {
        return step;
    }

    public void setStep(double step) {
        this.step = step;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public MyJSpinner getSpinner() {
        return spinner;
    }

}
