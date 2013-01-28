/**
 * This source code belongs to Mark Zang, the author. To use it for
 * commercial/business purpose, please contact DeepNightTwo@gmail.com
 * 
 * @author Mark Zang
 * 
 */

package com.deepnighttwo.ui.guibuilder.test;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.TitledBorder;

import com.deepnighttwo.gen.imagegen.JuliaSetArgs;
import com.deepnighttwo.ui.guibuilder.EditorSection;
import com.deepnighttwo.ui.guibuilder.PropertyLine;
import com.deepnighttwo.ui.guibuilder.xml.XMLGUIBuilder;
import com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel;

public class TestXMLBuilder {

    public static void main(String[] args) {

        // Queue<TaskArg> taskQueue = new LinkedBlockingQueue<TaskArg>();

        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        Insets noSpace = new Insets(0, 0, 0, 0);
        Insets space = new Insets(5, 0, 0, 0);

        JFrame f = new JFrame();
        f.setSize(new Dimension(500, 300));
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocation(new Point(100, 100));
        Container c = f.getContentPane();

        c.setLayout(new GridBagLayout());
        GridBagConstraints gbco = new GridBagConstraints();
        gbco.gridx = 0;
        gbco.gridy = 0;
        gbco.fill = GridBagConstraints.HORIZONTAL;
        gbco.weightx = 1.0;
        gbco.weighty = 0.0;

        List<EditorSection> sections = XMLGUIBuilder.parseGUIXML("com/deepnighttwo/julia/argseditor/juliaset.xml");

        final JuliaSetArgs bean = new JuliaSetArgs();

        for (EditorSection section : sections) {
            JPanel sectionPanel = new JPanel();
            sectionPanel.setBorder(new TitledBorder(section.getSectionLabel()));
            sectionPanel.setLayout(new GridBagLayout());

            List<PropertyLine> props = section.getProps();
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1.0;
            gbc.weighty = 0.0;
            for (PropertyLine prop : props) {
                // prop.setBeanInst(bean);
                prop.setArgs(bean);

                Component label = prop.getLabel();
                Component content = prop.getContent();
                if (label == null && content != null) {
                    gbc.gridx = 0;
                    gbc.insets = noSpace;
                    gbc.weightx = 1.0;
                    gbc.weighty = 0.0;
                    gbc.gridwidth = 2;
                    sectionPanel.add(content, gbc);
                } else if (label != null && content == null) {
                    gbc.gridx = 0;
                    gbc.insets = noSpace;
                    gbc.weightx = 1.0;
                    gbc.weighty = 0.0;
                    gbc.gridwidth = 2;
                    sectionPanel.add(label, gbc);
                } else {
                    gbc.gridwidth = 1;

                    gbc.gridx = 0;
                    gbc.insets = space;
                    gbc.weightx = 0.0;
                    gbc.weighty = 0.0;
                    sectionPanel.add(label, gbc);

                    gbc.gridx = 1;
                    gbc.insets = noSpace;
                    gbc.weightx = 1.0;
                    gbc.weighty = 0.0;
                    sectionPanel.add(content, gbc);
                }

                gbc.gridy++;
            }

            c.add(sectionPanel, gbco);
            gbco.gridy++;
        }
        f.setVisible(true);
    }
}
