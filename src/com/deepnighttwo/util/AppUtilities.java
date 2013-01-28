package com.deepnighttwo.util;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.util.Locale;

import javax.swing.JDialog;
import javax.swing.JFrame;

public class AppUtilities {

    public static void showCenterFrame(JFrame frame) {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screen = toolkit.getScreenSize();
        frame.setBounds((screen.width - frame.getWidth()) / 2, (screen.height - frame.getHeight()) / 2,
                frame.getWidth(), frame.getHeight());
        frame.setVisible(true);
    }

    public static void showCenterDialog(JDialog dialog) {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screen = toolkit.getScreenSize();
        dialog.setBounds((screen.width - dialog.getWidth()) / 2, (screen.height - dialog.getHeight()) / 2,
                dialog.getWidth(), dialog.getHeight());
        dialog.setVisible(true);
    }

    public static void setFontForComponent(Component Comp, Font font) {
        if (font == null) {
            try {
                if (Locale.getDefault().toString().toLowerCase().contains("zh")) {
                    font = new Font("Microsoft YaHei", Font.PLAIN, 12);
                }
            } catch (Throwable e) {
            }
        }
        if (font == null) {
            return;
        }
        if (Comp != null) {
            try {
                Comp.setFont(font);
            } catch (Exception e) {
                return;
            }
        }
        if (Comp instanceof Container) {
            Component[] components = ((Container) Comp).getComponents();
            for (int i = 0; i < components.length; i++) {
                Component child = components[i];
                if (child != null) {
                    setFontForComponent(child, font);
                }
            }
        }
        return;
    }
}
