/**
 * This source code belongs to Mark Zang, the author. To use it for
 * commercial/business purpose, please contact DeepNightTwo@gmail.com
 * 
 * @author Mark Zang
 * 
 */

package com.deepnighttwo.appversion;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.deepnighttwo.ui.argseditor.JuliaSetArgsPanel;
import com.deepnighttwo.util.AppUtilities;
import com.deepnighttwo.util.ResourceUtil;

public class MainApp extends JFrame {

    private static final long serialVersionUID = -2302275687814183650L;
    static JuliaSetArgsPanel c;

    public MainApp() {
        try {
            Class<?> lookAndFeel = Class.forName("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
            if (lookAndFeel != null) {
                UIManager.setLookAndFeel((LookAndFeel) lookAndFeel.newInstance());
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        JPanel container = (JPanel) this.getContentPane();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle(ResourceUtil.getString("Julia Set Window"));
        c = new JuliaSetArgsPanel();
        AppUtilities.setFontForComponent(c, null);
        this.pack();
        this.setBounds(0, 0, 850, 700);
        container.add(c);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                AppUtilities.showCenterFrame(new MainApp());
            }
        });

    }
}
