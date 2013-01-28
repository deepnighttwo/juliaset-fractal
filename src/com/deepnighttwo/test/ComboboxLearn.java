/**
 * This source code belongs to Mark Zang, the author. To use it for
 * commercial/business purpose, please contact DeepNightTwo@gmail.com
 * 
 * @author Mark Zang
 * 
 */

package com.deepnighttwo.test;

import java.awt.AWTEvent;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowStateListener;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JWindow;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

public class ComboboxLearn {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        toolkit.addAWTEventListener(new AWTEventListener() {

            @Override
            public void eventDispatched(AWTEvent event) {
                System.out.println(event);
            }
        }, sun.awt.SunToolkit.GRAB_EVENT_MASK);
        JComboBox box = new JComboBox(new Object[] {
            "AAA"
        });
        box.addPopupMenuListener(new PopupMenuListener() {

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
                System.out.println(e);
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                System.out.println(e);// Set a breakpoint here
            }

            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                System.out.println(e);
            }
        });
        JWindow d = new JWindow();
        d.setSize(500, 500);
        d.setVisible(true);
        d.addWindowFocusListener(new WindowFocusListener() {

            @Override
            public void windowGainedFocus(WindowEvent e) {
                System.out.println("asdfasdfasdf");
            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                System.out.println("asdfasdfasdfasdf");
            }

        });
        d.addWindowStateListener(new WindowStateListener() {

            @Override
            public void windowStateChanged(WindowEvent e) {
                System.out.println("asdfasdfasdf");
            }

        });
        JFrame f = new JFrame();
        f.getContentPane().setLayout(new FlowLayout());
        f.getContentPane().add(box);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(new Dimension(100, 100));

        f.setVisible(true);
    }
}
