/**
 * This source code belongs to Mark Zang, the author. To use it for
 * commercial/business purpose, please contact DeepNightTwo@gmail.com
 * 
 * @author Mark Zang
 * 
 */
package com.deepnighttwo.test;

import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.deepnighttwo.ui.componets.MyJSpinner;
import com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel;

public class TestComponents {

    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel p = (JPanel) frame.getContentPane();
        p.setLayout(new FlowLayout());
        p.add(new MyJSpinner());

        JPanel pl = new JPanel();
        pl.add(new JLabel("asdfasdfssdfasdfasdfasdfasdfasdfasdfaadfdasdf"));
        JScrollPane scrollControlPanel = new JScrollPane();
        scrollControlPanel.setViewportView(pl);
        p.add(scrollControlPanel);

        frame.setBounds(100, 100, 300, 300);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                frame.setVisible(true);
            }
        });
    }

}
