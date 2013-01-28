/**
 * This source code belongs to Mark Zang, the author. To use it for
 * commercial/business purpose, please contact DeepNightTwo@gmail.com
 * 
 * @author Mark Zang
 * 
 */

package com.deepnighttwo.ui.componets;

import javax.swing.JSpinner;
import javax.swing.SpinnerModel;

@SuppressWarnings("serial")
public class MyJSpinner extends JSpinner {

    public MyJSpinner() {
    }

    public MyJSpinner(SpinnerModel model) {
        super(model);
    }

    public double getDoubleValue() {
        Object objValue = this.getValue();
        if (objValue instanceof Double) {
            return ((Double) objValue).doubleValue();
        }
        return Double.valueOf(objValue.toString());
    }

    public int getIntValue() {
        Object objValue = this.getValue();
        if (objValue instanceof Integer) {
            return ((Integer) objValue).intValue();
        }
        return Integer.valueOf(objValue.toString());
    }

}
