/**
 * This source code belongs to Mark Zang, the author. To use it for
 * commercial/business purpose, please contact DeepNightTwo@gmail.com
 * 
 * @author Mark Zang
 * 
 */

package com.deepnighttwo.ui.argseditor;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JPanel;

import com.deepnighttwo.gen.imagegen.JuliaSetArgs;
import com.deepnighttwo.gen.imagegen.JuliaSetImage;
import com.deepnighttwo.gen.task.IGenPostTask;
import com.deepnighttwo.gen.task.ITaskArg;
import com.deepnighttwo.gen.task.impl.CommonTaskArg;

/**
 * Panel to hold Julia image
 * 
 * @author mzang
 * 
 */
public strictfp class JuliaSetPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JuliaSetImage juliaImage;

    // variables for enlarge
    private static final double minUnit = 0.0000000001;

    private Dimension size;

    private BlockingQueue<ITaskArg> taskQueue = new LinkedBlockingQueue<ITaskArg>();

    private JuliaSetArgs argsEnlarge;

    private JuliaSetArgs currArgs;

    private JuliaSetArgs currArgsEnlarge;

    private static final double ROOM_IN = Math.pow(1.3, 0.5);

    private static final double ROOM_OUT = 1 / ROOM_IN;

    private IGenPostTask logTask;

    private boolean logFlag = false;

    public JuliaSetPanel(boolean adjustImageSize, JuliaSetArgs currPanelArgs, IGenPostTask logTask) {
        this.logTask = logTask;
        if (adjustImageSize == true) {
            size = new Dimension();
            this.addComponentListener(new ComponentAdapter() {

                @Override
                public void componentResized(ComponentEvent e) {
                    size.height = getHeight();
                    size.width = getWidth();
                    currArgs.adjustToDeminsion(size.width, size.height);
                    addJuliaArgsTask(currArgs);
                }

            });
            size.height = this.getHeight();
            size.width = this.getWidth();
        }

        this.currArgs = currPanelArgs;
        currArgsEnlarge = new JuliaSetArgs();
        argsEnlarge = new JuliaSetArgs();
        juliaImage = new JuliaSetImage(taskQueue);
        init();

    }

    public void setLogFlag(boolean logFlag) {
        this.logFlag = logFlag;
    }

    public JuliaSetArgs getCurrentArgs() {
        return currArgs;
    }

    public void addJuliaArgsTask(JuliaSetArgs args) {
        addJuliaTask(new CommonTaskArg(args, null, null, null), false);
    }

    public void addJuliaTask(ITaskArg arg, boolean clearQueue) {
        if (clearQueue == true) {
            taskQueue.clear();
        }

        if (logFlag == true) {
            if (arg.getGenPostTask() == null) {
                arg.setGenPostTask(logTask);
            } else {
                arg.getGenPostTask().setNext(logTask);
            }
        }
        taskQueue.add(arg);
    }

    public void addRawJuliaTask(ITaskArg task, boolean clearQueue) {
        if (clearQueue == true) {
            taskQueue.clear();
        }
        taskQueue.add(task);
    }

    public void clearTaskQueue() {
        taskQueue.clear();
    }

    private void init() {
        this.addMouseWheelListener(new MouseAdapter() {

            public void mouseWheelMoved(MouseWheelEvent e) {
                boolean roomIn = e.getWheelRotation() < 0;
                enlargeImageCenterByPoint(e.getPoint(), roomIn);
            }

        });

        this.addMouseListener(new MouseAdapter() {

            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if (currArgsEnlarge.equals(currArgs) == false) {
                        currArgsEnlarge.copy(currArgs);
                        argsEnlarge.copy(currArgs);
                    }
                    currArgs.adjustToDeminsion(size.width, size.height);
                    Point p = e.getPoint();
                    enlargeImage(p, 1);
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    currArgsEnlarge.copy(currArgs);
                    argsEnlarge.copy(currArgs);
                    currArgs.adjustToDeminsion(size.width, size.height);
                    addJuliaArgsTask(currArgs);
                }
            }
        });
    }

    private void enlargeImageCenterByPoint(Point point, boolean roomIn) {
        if (currArgsEnlarge.equals(currArgs) == false) {
            currArgsEnlarge.copy(currArgs);
            argsEnlarge.copy(currArgs);
        }
        int imageWidth = argsEnlarge.getImageWidth();
        int imageHeight = argsEnlarge.getImageHeight();
        int x = point.x;
        int y = point.y;

        double enlarge = roomIn ? ROOM_IN : ROOM_OUT;

        int newCx = (int) (((imageWidth / 2) - x) / enlarge + x + 0.5);
        int newCy = (int) (((imageHeight / 2) - y) / enlarge + y + 0.5);

        enlargeImage(new Point(newCx, newCy), enlarge);
    }

    private void enlargeImage(Point p, double enlargeTimes) {

        double realStartEnlarge = argsEnlarge.getRealStart();
        double realEndEnlarge = argsEnlarge.getRealEnd();

        double imgStartEnlarge = argsEnlarge.getImgStart();
        double imgEndEnlarge = argsEnlarge.getImgEnd();

        if (Math.abs(realStartEnlarge - realEndEnlarge) < minUnit
                && Math.abs(imgStartEnlarge - imgEndEnlarge) < minUnit) {
            // too small to count, reset.
            argsEnlarge.copy(currArgs);
        }

        int x = p.x;
        int y = p.y;

        int[] xArea = getEnlargeRange(x, argsEnlarge.getImageWidth(), enlargeTimes);
        int[] yArea = getEnlargeRange(y, argsEnlarge.getImageHeight(), enlargeTimes);

        double tmprealStartEnlarge = realStartEnlarge
                + ((realEndEnlarge - realStartEnlarge) / argsEnlarge.getImageWidth()) * (xArea[0]);

        double tmprealEndEnlarge = realStartEnlarge
                + ((realEndEnlarge - realStartEnlarge) / argsEnlarge.getImageWidth()) * (xArea[1]);

        double tmpimgStartEnlarge = imgStartEnlarge
                + ((imgEndEnlarge - imgStartEnlarge) / argsEnlarge.getImageHeight()) * (yArea[0]);

        double tmpimgEndEnlarge = imgStartEnlarge + ((imgEndEnlarge - imgStartEnlarge) / argsEnlarge.getImageHeight())
                * (yArea[1]);

        argsEnlarge.setRealStart(tmprealStartEnlarge);
        argsEnlarge.setRealEnd(tmprealEndEnlarge);
        argsEnlarge.setImgStart(tmpimgStartEnlarge);
        argsEnlarge.setImgEnd(tmpimgEndEnlarge);

        taskQueue.clear();
        addJuliaArgsTask(argsEnlarge);

    }

    private int[] getEnlargeRange(int center, int original, double enlargeTimes) {
        int newSize = (int) (original / enlargeTimes + 0.5) / 2;
        int[] ret = new int[2];
        ret[0] = center - newSize;
        ret[1] = center + newSize;
        return ret;
    }

    public JuliaSetImage getJuliaImage() {
        return juliaImage;
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(juliaImage.getImage(), 0, 0, null);
    }

}
