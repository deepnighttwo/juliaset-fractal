package com.deepnighttwo.ui.guibuilder.componets;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.net.URL;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import com.deepnighttwo.gen.imagegen.JuliaSetArgs;
import com.deepnighttwo.gen.task.IGenPostTask;
import com.deepnighttwo.gen.task.impl.CommonTaskArg;
import com.deepnighttwo.gen.task.impl.ContinuousTaskArg;
import com.deepnighttwo.ui.guibuilder.PropertyLine;
import com.deepnighttwo.util.ResourceUtil;

// FIXME: don't use continuous task, just set the const value in this class. it will trigger the image generation task....maybe not work... think about this more.
public class Mandelbrot extends PropertyLine implements MouseMotionListener {

    private static Image mandelbrotImage;
    private JPanel mandellbrotPanel;
    private Point mousePoint;
    private static Dimension size;

    private JuliaSetArgs contArgs;

    public static final double unit = 4.0 / 500;

    public static final int centerX = 250;

    public static final int centerY = 150;

    public static final int ignore = 0, instant = 1, continuous = 2;

    private int mode = 1;

    private IGenPostTask syncConstValues;

    public static final int TRACK_SIZE = 5000;

    public final Point[] track = new Point[TRACK_SIZE];

    private int currTrackIdx;

    private int trackIdx = 0;

    static {
        URL bgImageURL = Mandelbrot.class.getResource("mandelbrot.gif");
        mandelbrotImage = Toolkit.getDefaultToolkit().getImage(bgImageURL);
        size = new Dimension(350, 300);
    }

    public Mandelbrot() {
        contArgs = new JuliaSetArgs();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (mode != continuous) {
            return;
        }

        if (trackIdx >= TRACK_SIZE) {
            return;
        }
        Point p = e.getPoint();
        track[trackIdx] = p;

        ContinuousTaskArg arg = new ContinuousTaskArg(contArgs, this, trackIdx);
        trackIdx++;
        argsPanel.addJuliaTask(arg, false);
        mandellbrotPanel.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (mode != instant) {
            return;
        }
        argsPanel.clearTaskQueue();

        Point p = e.getPoint();
        mousePoint = p;
        double constX = (p.getX() - centerX) * unit;
        double constY = (centerY - p.getY()) * unit;
        contArgs.setConstReal(constX);
        contArgs.setConstImaginary(constY);

        CommonTaskArg arg = new CommonTaskArg(contArgs, null, null, syncConstValues);

        argsPanel.addJuliaTask(arg, true);

        mandellbrotPanel.repaint();
    }

    @Override
    public JComponent getContent() {
        // container
        JPanel container = new JPanel();
        container.setLayout(new BorderLayout());

        // build mandellbrot panel
        mandellbrotPanel = new JPanel() {
            private static final long serialVersionUID = 1L;

            public void paint(Graphics g) {
                g.drawImage(mandelbrotImage, 0, 0, this);

                if (mode == instant) {
                    if (mousePoint != null) {
                        g.setColor(Color.gray);
                        g.drawLine(0, (int) mousePoint.getY(), size.width, (int) mousePoint.getY());
                        g.drawLine((int) mousePoint.getX(), 0, (int) mousePoint.getX(), size.height);
                    }
                } else if (mode == continuous) {
                    if (trackIdx == 0) {
                        return;
                    }
                    Point p1 = track[0];
                    g.setColor(Color.red);
                    for (int i = 1; i <= currTrackIdx; i++) {
                        Point p2 = track[i];
                        if (p1 == null || p2 == null) {
                            return;
                        }
                        g.drawLine(p1.x, p1.y, p2.x, p2.y);
                        p1 = p2;
                    }
                    g.setColor(Color.gray);
                    for (int i = currTrackIdx + 1; i < trackIdx; i++) {
                        Point p2 = track[i];
                        if (p1 == null || p2 == null) {
                            return;
                        }
                        g.drawLine(p1.x, p1.y, p2.x, p2.y);
                        p1 = p2;
                    }
                }
            }
        };

        mandellbrotPanel.setPreferredSize(size);
        mandellbrotPanel.setMinimumSize(size);
        mandellbrotPanel.setMaximumSize(size);
        mandellbrotPanel.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                switch (mode) {
                    case ignore:
                    break;
                    case instant:
                    case continuous:
                        contArgs.copy(Mandelbrot.this.argsPanel.getCurrentArgs());
                    break;
                }
            }

            public void mouseExited(MouseEvent e) {
                switch (mode) {
                    case ignore:
                    break;
                    case instant:
                        mousePoint = null;
                        mandellbrotPanel.repaint();
                    break;
                    case continuous:
                    break;
                }
            }

            public void mousePressed(MouseEvent e) {

                switch (mode) {
                    case ignore:
                    break;
                    case instant:
                    break;
                    case continuous:
                        Mandelbrot.this.argsPanel.clearTaskQueue();
                        trackIdx = 0;
                        for (int i = 0; i < TRACK_SIZE; i++) {
                            track[i] = null;
                        }
                        contArgs.copy(Mandelbrot.this.argsPanel.getCurrentArgs());
                    break;
                }

            }

        });
        mandellbrotPanel.addMouseMotionListener(this);

        // build button group
        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new FlowLayout());

        JRadioButton ignoreBtn = new JRadioButton(ResourceUtil.getString("Ignore"));
        JRadioButton instantBtn = new JRadioButton(ResourceUtil.getString("Instant"));
        JRadioButton continuousBtn = new JRadioButton(ResourceUtil.getString("Continuous"));
        ButtonGroup group = new ButtonGroup();
        group.add(ignoreBtn);
        group.add(instantBtn);
        group.add(continuousBtn);
        instantBtn.setSelected(true);

        ignoreBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Mandelbrot.this.argsPanel.clearTaskQueue();
                mode = ignore;
            }
        });

        instantBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Mandelbrot.this.argsPanel.clearTaskQueue();
                mode = instant;
            }
        });

        continuousBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Mandelbrot.this.argsPanel.clearTaskQueue();
                mode = continuous;
            }
        });

        btnPanel.add(ignoreBtn);
        btnPanel.add(instantBtn);
        btnPanel.add(continuousBtn);

        container.add(mandellbrotPanel, BorderLayout.CENTER);
        container.add(btnPanel, BorderLayout.SOUTH);

        return container;
    }

    public void setSyncConstValues(IGenPostTask syncConstValues) {
        this.syncConstValues = syncConstValues;
    }

    public IGenPostTask getSyncConstValues() {
        return this.syncConstValues;
    }

    public void setProcessedPoint(int currTrackIdx) {
        this.currTrackIdx = currTrackIdx;
        this.mandellbrotPanel.repaint();
    }
}
