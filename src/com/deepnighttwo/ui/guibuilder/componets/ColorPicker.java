/**
 * This source code belongs to Mark Zang, the author. To use it for
 * commercial/business purpose, please contact DeepNightTwo@gmail.com
 * 
 * @author Mark Zang
 * 
 */

package com.deepnighttwo.ui.guibuilder.componets;

import java.awt.Color;

import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.deepnighttwo.ui.componets.ColorCellPanel;
import com.deepnighttwo.ui.guibuilder.PropertyLine;
import com.deepnighttwo.ui.guibuilder.xml.ReflectSetValueException;
import com.deepnighttwo.util.ReflectUtility;
import com.deepnighttwo.util.ResourceUtil;

public class ColorPicker extends PropertyLine {

    private Color color;

    private String buttonTxt;

    @Override
    public JComponent getContent() {

        final ColorCellPanel colorPicker = new ColorCellPanel(color, ResourceUtil.getString(buttonTxt));
        colorPicker.setListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                Color newColor = colorPicker.getSelectedColor();
                color = newColor;
                valueChanged(newColor);
            }

        });
        try {
            ReflectUtility.setValueReflect(beanClass, args, fieldName, color);
        } catch (ReflectSetValueException ex) {
            ex.printStackTrace();
        }
        return colorPicker;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getButtonTxt() {
        return buttonTxt;
    }

    public void setButtonTxt(String buttonTxt) {
        this.buttonTxt = buttonTxt;
    }

}
