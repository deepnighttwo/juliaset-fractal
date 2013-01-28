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

import javax.swing.JComboBox;
import javax.swing.JComponent;

import com.deepnighttwo.ui.guibuilder.PropertyLine;
import com.deepnighttwo.ui.guibuilder.xml.ReflectSetValueException;
import com.deepnighttwo.util.ReflectUtility;
import com.deepnighttwo.util.ResourceUtil;

public class MultiSelection extends PropertyLine {

    private String[] labels;

    public JComponent getContent() {
        String[] showLabels = new String[labels.length];
        for (int i = 0; i < labels.length; i++) {
            showLabels[i] = ResourceUtil.getString(labels[i]);
        }
        final JComboBox colorGenerationSolution = new JComboBox(showLabels);

        colorGenerationSolution.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int newValue = colorGenerationSolution.getSelectedIndex();
                valueChanged(newValue);
            }

        });
        try {
            ReflectUtility.setValueReflect(beanClass, args, fieldName, 0);
        } catch (ReflectSetValueException ex) {
            ex.printStackTrace();
        }
        return colorGenerationSolution;
    }

    public String[] getLabels() {
        return labels;
    }

    public void setLabels(String[] labels) {
        this.labels = labels;
    }

}
