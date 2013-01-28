/**
 * This source code belongs to Mark Zang, the author. To use it for
 * commercial/business purpose, please contact DeepNightTwo@gmail.com
 * 
 * @author Mark Zang
 * 
 */

package com.deepnighttwo.ui.componets;

import java.applet.Applet;
import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.deepnighttwo.ui.componets.LightPopup.PopupHider;

public class ColorCellPanel extends JPanel implements ChangeListener {

    private static final long serialVersionUID = 1L;
    private Color selectedColor;

    private JPanel colorCell;
    private JColorChooser colorChooser;
    private JPanel popupPanel;
    private ChangeListener listener;

    public ColorCellPanel(Color initColor, String buttonText, ChangeListener l) {
        selectedColor = initColor;
        this.listener = l;
        init(buttonText);
    }

    public ColorCellPanel(Color initColor, String buttonText) {
        selectedColor = initColor;
        init(buttonText);
    }

    private void init(String buttonText) {
        colorCell = new JPanel();
        colorCell.setOpaque(true);
        colorCell.setBackground(selectedColor);

        colorChooser = new JColorChooser(selectedColor);
        colorChooser.getSelectionModel().addChangeListener(this);
        colorChooser.setBorder(BorderFactory.createTitledBorder(buttonText));

        colorCell.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                chooseColor();
            }

        });

        popupPanel = new JPanel();
        popupPanel.setLayout(new BorderLayout());
        popupPanel.add(colorChooser, BorderLayout.CENTER);

        Border raisedbevel = BorderFactory.createRaisedBevelBorder();
        Border loweredbevel = BorderFactory.createLoweredBevelBorder();

        Border compound = BorderFactory.createCompoundBorder(raisedbevel, loweredbevel);

        setPreferredSize(new Dimension(30, 30));
        setLayout(new BorderLayout());
        setBorder(compound);
        add(colorCell, BorderLayout.CENTER);

    }

    private synchronized void chooseColor() {
        Point p = getLocationOnScreen();
        p.translate(0, getHeight());
        LightPopup popup = new LightPopup(p, popupPanel);

        popup.show();

    }

    @Override
    public void stateChanged(ChangeEvent e) {
        selectedColor = colorChooser.getColor();
        colorCell.setBackground(selectedColor);
        listener.stateChanged(e);
    }

    public Color getSelectedColor() {
        return selectedColor;
    }

    public void setSelectedColor(Color selectedColor) {
        this.selectedColor = selectedColor;
    }

    public ChangeListener getListener() {
        return listener;
    }

    public void setListener(ChangeListener listener) {
        this.listener = listener;
    }

}

@SuppressWarnings("serial")
class LightPopup extends JPanel {
    private Point location;
    private Component content;
    private PopupHider hider;
    private Popup popup;

    public LightPopup(Point location, Component content) {
        this.location = location;
        this.content = content;
        init();
    }

    private void init() {
        hider = new PopupHider();
        PopupFactory factory = PopupFactory.getSharedInstance();
        popup = factory.getPopup(this, content, location.x, location.y);
        registorListener();
    }

    public void show() {
        popup.show();
    }

    class PopupHider implements AWTEventListener {

        @Override
        public void eventDispatched(AWTEvent event) {
            if (event instanceof MouseEvent) {

                MouseEvent me = (MouseEvent) event;
                Component src = me.getComponent();
                if (!(event instanceof MouseEvent)) {
                    return;
                }
                switch (me.getID()) {
                    case MouseEvent.MOUSE_PRESSED:
                        if (!isInPopupPanel(src)) {
                            // should not happen
                            handleHide();
                        }
                    break;
                }
            } else if (event instanceof WindowEvent) {
                switch (event.getID()) {
                    case WindowEvent.WINDOW_LOST_FOCUS:
                        handleHide();
                    break;
                    case WindowEvent.WINDOW_ACTIVATED:
                        handleHide();
                    break;
                    case WindowEvent.WINDOW_GAINED_FOCUS:
                        handleHide();
                    break;
                }
            } else if (event instanceof KeyEvent) {
                if (((KeyEvent) event).getKeyCode() == KeyEvent.VK_ESCAPE
                        || ((KeyEvent) event).getKeyCode() == KeyEvent.VK_ENTER) {
                    handleHide();
                }
            }
        }
    }

    private void registorListener() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        toolkit.addAWTEventListener(hider, AWTEvent.MOUSE_EVENT_MASK | AWTEvent.WINDOW_EVENT_MASK
                | sun.awt.SunToolkit.GRAB_EVENT_MASK | AWTEvent.KEY_EVENT_MASK);

    }

    private void handleHide() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        toolkit.removeAWTEventListener(hider);
        popup.hide();
    }

    boolean isInPopupPanel(Component src) {
        for (Component c = src; c != null; c = c.getParent()) {
            if (c instanceof Applet || c instanceof Window) {
                break;
            } else if (c == content) {
                return true;
            }
        }
        return false;
    }

}
