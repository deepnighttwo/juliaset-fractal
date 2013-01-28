/**
 * This source code belongs to Mark Zang, the author. To use it for
 * commercial/business purpose, please contact DeepNightTwo@gmail.com
 * 
 * @author Mark Zang
 * 
 */

package com.deepnighttwo.appletversion;

import java.awt.Dimension;

import javax.swing.JApplet;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.deepnighttwo.ui.argseditor.JuliaSetArgsPanel;
import com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel;

public class ComplexNumberApplet extends JApplet {
    private static final long serialVersionUID = -7835487734487006960L;
    JuliaSetArgsPanel c;

    @Override
    public void destroy() {
        super.destroy();
    }

    @Override
    public void init() {
        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        c = new JuliaSetArgsPanel();
        this.setSize(new Dimension(850, 700));
        super.init();
    }

    @Override
    public void start() {
        this.getContentPane().add(c);
        super.start();
    }

    @Override
    public void stop() {
        c.setVisible(false);
        super.stop();
    }

}
