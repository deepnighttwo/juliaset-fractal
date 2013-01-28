/**
 * This source code belongs to Mark Zang, the author. To use it for
 * commercial/business purpose, please contact DeepNightTwo@gmail.com
 * 
 * @author Mark Zang
 * 
 */

package com.deepnighttwo.gen.imagegen;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.deepnighttwo.complexnumber.ComplexNumber;
import com.deepnighttwo.gen.task.IGenPostTask;
import com.deepnighttwo.gen.task.ITaskArg;
import com.deepnighttwo.util.ResourceUtil;

public class JuliaSetImage {

    // variables about image generation
    private ComplexNumber constNumber;
    private double stepReal;
    private double stepImg;

    private int threadCount = 0;

    // variables about image rendering
    private Color[] colors = null;
    private Image image;
    private Image[] imagePieces;
    private Graphics2D g;
    private JuliaSetArgs julia;
    private int currImagePieceIdx;

    // an array buffer pool for pixels to render. reuse the array but now create
    // new ones.
    Queue<int[]> pointsArrayBuff = new ConcurrentLinkedQueue<int[]>();

    // listener list for generation listeners
    private List<JuliaSetProgressListener> progressListeners = new ArrayList<JuliaSetProgressListener>();

    // worker list. workers are reusable.

    // a schedule thread to get JuliaSetArgs from task queue.
    private Thread scheduleThread;

    private ExecutorService workersPool = null;

    private List<Runnable> workers = new LinkedList<Runnable>();

    private volatile CountDownLatch genLock;

    private ReentrantLock taskLock = new ReentrantLock();

    private JuliaSetArgs lastArg = null;

    private long startTime;
    private long endTime;

    /**
     * create a new julia set image instance. initial status, worker lock, create threads and put them on waiting
     */
    public JuliaSetImage(final BlockingQueue<? extends ITaskArg> taskQueue) {
        julia = new JuliaSetArgs();
        scheduleThread = new JuliaSetFGenScheduleThread("juliaset-schedule-thread", taskQueue);
        scheduleThread.start();
    }

    class JuliaSetFGenScheduleThread extends Thread {
        final BlockingQueue<? extends ITaskArg> taskQueue;

        public JuliaSetFGenScheduleThread(String threadName, final BlockingQueue<? extends ITaskArg> taskQueue) {
            this.taskQueue = taskQueue;
        }

        public void run() {
            JuliaSetArgs args = null;
            ITaskArg task = null;
            while (true) {
                task = getGenTask(taskQueue, args, task);

                handleGenTask(task);

                task.taskFinished();
                args = null;
            }
        }

        private void handleGenTask(ITaskArg task) {
            IGenPostTask genPostTask;
            JuliaSetArgs args = task.getArgsForGen();
            boolean generating = false;

            while (args != null) {

                if (args.getColorSerial() == null) {
                    break;
                }

                genLock = new CountDownLatch(args.getThreadCount());

                startTime = System.currentTimeMillis();
                lastArg = args;
                generating = generateImage(args);

                if (generating == true) {
                    try {
                        genLock.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    genPostTask = task.getGenPostTask();
                    while (genPostTask != null) {
                        genPostTask.postTask(JuliaSetImage.this);
                        genPostTask = genPostTask.getNext();
                    }
                    generationPost();
                }

                args = task.getArgsForGen();
            }
        }

        private ITaskArg getGenTask(final BlockingQueue<? extends ITaskArg> taskQueue, JuliaSetArgs args, ITaskArg arg) {
            try {
                if (args == null) {
                    do {
                        arg = taskQueue.poll(Long.MAX_VALUE, TimeUnit.MICROSECONDS);
                    } while (arg == null);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return arg;
        }

    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public JuliaSetArgs getLastJuliaSetArg() {
        return lastArg;
    }

    /**
     * generate image based on the arguments
     * 
     * @param julia
     *            the arguments that used to generate image.
     */
    private boolean generateImage(JuliaSetArgs newJulia) {
        boolean createImage = ((julia == null) || (julia.getImageWidth() != newJulia.getImageWidth()) || (julia
                .getImageHeight() != newJulia.getImageHeight()))
                && (newJulia.getImageWidth() > 0 && newJulia.getImageHeight() > 0);

        julia.copy(newJulia);
        if (initJuliaSystem(createImage)) {
            // System init correctly, go and generate image
            startGeneration();
            return true;
        }
        // fireImageGenerationFinishedEvent();
        return false;

    }

    /**
     * Create Memory image, reset system variables, count step length, set Graphic instance and so on.
     * 
     * @return
     */
    private boolean initJuliaSystem(boolean createImage) {
        if (createImage == true) {
            try {
                image = new BufferedImage(julia.getImageWidth(), julia.getImageHeight(), BufferedImage.TYPE_INT_RGB);
                g = (Graphics2D) image.getGraphics();
                g.setBackground(Color.WHITE);
                g.setColor(Color.BLUE);
                g.clearRect(0, 0, julia.getImageWidth(), julia.getImageHeight());

                List<Image> pieces = new ArrayList<Image>();
                int imageWidth = julia.getImageWidth();
                int paintUnit = julia.getPaintUnit();
                while (imageWidth > 0) {
                    if (imageWidth >= paintUnit) {
                        pieces.add(new BufferedImage(paintUnit, julia.getImageHeight(), BufferedImage.TYPE_INT_RGB));
                        imageWidth -= paintUnit;
                    } else {
                        pieces.add(new BufferedImage(imageWidth, julia.getImageHeight(), BufferedImage.TYPE_INT_RGB));
                        imageWidth -= paintUnit;
                    }
                }
                imagePieces = pieces.toArray(new Image[0]);
            } catch (Throwable t) {
                JOptionPane.showMessageDialog(new JFrame(), ResourceUtil.getString("OOM_ERR_MSG"),
                        ResourceUtil.getString("OOM_ERR_TITLE"), JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        colors = julia.getColorSerial();
        constNumber = JuliaSetImage.createComplexNumber(julia.getConstReal(), julia.getConstImaginary());

        // "how much unit use a pixel"
        stepReal = (julia.getRealEnd() - julia.getRealStart()) / julia.getImageWidth();

        stepImg = (julia.getImgEnd() - julia.getImgStart()) / julia.getImageWidth();

        if (threadCount != julia.getThreadCount()) {
            if (workersPool != null) {
                workersPool.shutdownNow();
            }
            threadCount = julia.getThreadCount();
            workersPool = Executors.newFixedThreadPool(threadCount);
            if (workers.size() < threadCount) {
                workers.add(new JuliaSetWorker());
            } else {
                for (int i = workers.size() - 1; i != threadCount - 1; i--) {
                    workers.remove(i);
                }
            }
        }

        return true;
    }

    /**
     * method to create a complex number. this is not useful currently. always use ComplexNumber.
     * 
     * @param real
     * @param imaginary
     * @param highPre
     * @return
     */
    private static ComplexNumber createComplexNumber(double real, double imaginary) {
        // if (highPre) {
        // return new ComplexNumberHigh(real, imaginary);
        // }
        return new ComplexNumber(real, imaginary);
    }

    /**
     * clear all generation related variables, call generation process finished listeners
     */
    public void generationPost() {
        currImagePieceIdx = 0;
    }

    /**
     * start generation. change status, initials worker context, notify workers.
     */
    private void startGeneration() {
        currImagePieceIdx = 0;
        for (int i = 0; i < threadCount; i++) {
            workersPool.submit(new JuliaSetWorker());
        }
    }

    /**
     * get the generated image instance.
     * 
     * @return generated image instance
     */
    public Image getImage() {
        return image;
    }

    /**
     * based on currentX, generate a task instance for worker. length of a task is taskX.
     * 
     * @return
     */
    private ImageGenerationTask getImageGenerateTask() {
        try {
            taskLock.lock();

            if (currImagePieceIdx >= imagePieces.length) {
                return null;
            }
            // use local variable to speedup.
            Image image = imagePieces[currImagePieceIdx];
            ImageGenerationTask task = new ImageGenerationTask(julia.getPaintUnit() * currImagePieceIdx,
                    image.getWidth(null), image);
            currImagePieceIdx++;
            return task;
        } finally {
            taskLock.unlock();
        }
    }

    /**
     * class to wrap a generation task
     * 
     * @author mzang
     * 
     */
    class ImageGenerationTask {
        // piece x location in the whole image
        public int offsetX;
        // task length
        public int lengthX;
        // current image
        public Image currImage;

        public ImageGenerationTask(int offsetX, int lengthX, Image currImage) {
            this.offsetX = offsetX;
            this.lengthX = lengthX;
            this.currImage = currImage;
        }
    }

    /**
     * generation worker
     * 
     * @author mzang
     * 
     */
    class JuliaSetWorker implements Runnable {

        // generation task description. where to start and where end. this is
        // the pixel location on screen. including start x and start y, not
        // including end x end y
        int offsetX, lengthX, startY, endY;
        // pixel location in coordinate system
        double locationX, locationY;

        /**
         * invoke before start a new image generation task
         */
        public void initWorkingContext() {
        }

        Image image;

        public void run() {
            try {
                initWorkingContext();
                // get a generation task
                while (getImageGenerationTaskFromManager() == true) {
                    Color[] colors = JuliaSetImage.this.colors;
                    Graphics g = image.getGraphics();
                    // use local variable to speedup
                    double x = locationX;
                    double y = locationY;

                    int yEnd = -1;
                    int yStart = 0;
                    int preColor = -1;

                    ComplexNumber c = JuliaSetImage.createComplexNumber(0, 0);
                    for (int i = 0; (i < lengthX); i++, x += stepReal) {
                        y = locationY;
                        preColor = -1;
                        yEnd = 0;
                        yStart = 0;
                        // count pixel color one by one
                        for (int j = startY; (j < endY); j++, y += stepReal) {
                            c.setValue(x, y);
                            // count color and store it in an array
                            int colori = processPoint(c);

                            // initial previous color
                            if (preColor == -1) {
                                preColor = colori;
                            }

                            if (preColor != colori) {
                                g.setColor(colors[preColor % colors.length]);
                                g.drawLine(i, yStart, i, yEnd);
                                yStart = yEnd + 1;
                                preColor = colori;
                            }
                            yEnd++;
                        }
                        // if the array is not empty, push it into working
                        // queue.
                        if (yEnd != yStart) {
                            g.setColor(colors[preColor % colors.length]);
                            g.drawLine(i, yStart, i, yEnd);
                        }
                    }
                    JuliaSetImage.this.g.drawImage(image, offsetX, 0, null);
                    fireProgressChangedEvent();
                }
            } catch (Throwable e) {
                e.printStackTrace();
            } finally {
                genLock.countDown();
            }
        }

        /**
         * get a generation task. if there is no task, call generationTaskFinished to tell manager that this thread is
         * finished, wait for a task.
         */
        private boolean getImageGenerationTaskFromManager() {
            ImageGenerationTask task;
            if ((task = getImageGenerateTask()) == null) {
                return false;
            }
            this.offsetX = task.offsetX;
            this.lengthX = task.lengthX;
            this.startY = 0;
            this.endY = julia.getImageHeight();
            this.locationX = julia.getRealStart() + offsetX * stepReal;
            this.locationY = julia.getImgStart() + startY * stepImg;
            this.image = task.currImage;
            return true;
        }

        /**
         * count color index for c
         * 
         * @param c
         *            the number
         * @return the color index for the number
         */
        private int processPoint(ComplexNumber c) {
            int i = 0;
            int precision = julia.getPrecision();
            int threshold = julia.getThreshold();
            ComplexNumber constNumberLoacl = constNumber;
            for (; i < precision; i++) {
                c.square();
                c.add(constNumberLoacl);
                if (c.size() > threshold) {
                    break;
                }
            }
            return i;
        }

        // private int processPoint(ComplexNumber c) {
        // int i = 0;
        // int precision = julia.getPrecision();
        // ComplexNumber startNum = new ComplexNumber(0, 0);
        // for (; i < precision; i++) {
        // startNum.square();
        // startNum.add(c);
        // if (startNum.size() > 1000000) {
        // break;
        // }
        // }
        // return i;
        // }

    }

    /**
     * add process listener
     * 
     * @param l
     *            process listener
     */
    public synchronized void addJuliaProgressListener(JuliaSetProgressListener l) {
        progressListeners.add(l);
    }

    /**
     * remove process listener
     * 
     * @param l
     *            process listener
     */
    public synchronized void removeJuliaProgressListener(JuliaSetProgressListener l) {
        progressListeners.remove(l);
    }

    /**
     * trigger process changed event.
     */
    private void fireProgressChangedEvent() {
        for (JuliaSetProgressListener l : progressListeners) {
            l.progressChanged();
        }
    }

}
