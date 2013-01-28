/**
 * This source code belongs to Mark Zang, the author. To use it for
 * commercial/business purpose, please contact DeepNightTwo@gmail.com
 * 
 * @author Mark Zang
 * 
 */

package com.deepnighttwo.test;

import java.awt.Color;

import javax.swing.JComponent;
import javax.swing.JFrame;

import com.deepnighttwo.ui.componets.ColorCellPanel;

public class testcolorcell {
    public static void main(String[] args) {
        JFrame frame = new JFrame("ColorChooserDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create and set up the content pane.
        JComponent newContentPane = new ColorCellPanel(Color.blue, "Select Color", null);
        newContentPane.setOpaque(true); // content panes must be opaque
        frame.setContentPane(newContentPane);

        // Display the window.
        frame.pack();
        frame.setVisible(true);

    }

}
