package com.deepnighttwo.gen.task.impl;

import com.deepnighttwo.gen.imagegen.JuliaSetArgs;
import com.deepnighttwo.gen.task.IGenPostTask;
import com.deepnighttwo.gen.task.ITaskArg;
import com.deepnighttwo.ui.guibuilder.componets.Mandelbrot;

public class ContinuousTaskArg implements ITaskArg {

    private int iter = 0;

    private int iterCurr = 0;

    private double startX = 0, startY = 0, deltaX = 0, deltaY = 0;

    public JuliaSetArgs args;

    public IGenPostTask genPostTask;

    private static double step = 0.001;

    private int idx;

    private Mandelbrot mandelbrot;

    public ContinuousTaskArg(JuliaSetArgs args, Mandelbrot mandelbrot, int idx) {
        this.args = args;
        if (idx == 0) {
            iter = 1;
            deltaX = 0;
            deltaY = 0;
            startX = (mandelbrot.track[idx].getX() - Mandelbrot.centerX) * Mandelbrot.unit;
            startY = (Mandelbrot.centerY - mandelbrot.track[idx].getY()) * Mandelbrot.unit;

        } else {

            startX = (mandelbrot.track[idx - 1].getX() - Mandelbrot.centerX) * Mandelbrot.unit;
            startY = (Mandelbrot.centerY - mandelbrot.track[idx - 1].getY()) * Mandelbrot.unit;

            double targetX = (mandelbrot.track[idx].getX() - Mandelbrot.centerX) * Mandelbrot.unit;
            double targetY = (Mandelbrot.centerY - mandelbrot.track[idx].getY()) * Mandelbrot.unit;

            double distance = Math.sqrt(Math.pow(startX - targetX, 2) + Math.pow(startY - targetY, 2));
            iter = (int) (distance / step + 0.5);
            deltaX = (targetX - startX) / iter;
            deltaY = (targetY - startY) / iter;
        }
        this.genPostTask = mandelbrot.getSyncConstValues();
        this.idx = idx;
        this.mandelbrot = mandelbrot;
    }

    public JuliaSetArgs getArgsForGen() {

        if (iterCurr >= iter) {
            return null;
        }
        iterCurr++;
        startX += deltaX;
        startY += deltaY;

        args.setConstReal(startX);
        args.setConstImaginary(startY);

        return args;
    }

    public void setArgs(JuliaSetArgs args) {
        this.args = args;
    }

    @Override
    public IGenPostTask getGenPostTask() {
        return genPostTask;
    }

    @Override
    public void setGenPostTask(IGenPostTask genPostTask) {
        this.genPostTask = genPostTask;
    }

    @Override
    public void taskFinished() {
        mandelbrot.setProcessedPoint(idx);
    }

}
