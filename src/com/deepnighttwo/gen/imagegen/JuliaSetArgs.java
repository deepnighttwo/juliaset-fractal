/**
 * This source code belongs to Mark Zang, the author. To use it for
 * commercial/business purpose, please contact DeepNightTwo@gmail.com
 * 
 * @author Mark Zang
 * 
 */

package com.deepnighttwo.gen.imagegen;

import java.awt.Color;
import java.util.Arrays;

import com.deepnighttwo.util.ColorSerialGenerator;

public strictfp class JuliaSetArgs {

    private double realStart;
    private double realEnd;
    private double imgStart;
    private double imgEnd;

    private int imageWidth;
    private int imageHeight;

    private int precision;
    private double constReal;
    private double constImaginary;

    // Color configuration
    private Color[] colorSerial;

    private Color startColor;
    private Color endColor;
    private int colorStyle;
    private int colorStep;

    // performance turning related parameters
    private int threadCount;
    private int paintUnit = 1024;
    private int threshold = 100000;

    public void copy(JuliaSetArgs args) {
        this.realStart = args.realStart;
        this.realEnd = args.realEnd;
        this.imgStart = args.imgStart;
        this.imgEnd = args.imgEnd;
        this.imageWidth = args.imageWidth;
        this.imageHeight = args.imageHeight;
        this.precision = args.precision;
        this.constReal = args.constReal;
        this.constImaginary = args.constImaginary;
        this.colorSerial = args.colorSerial;
        this.threadCount = args.threadCount;
        this.paintUnit = args.paintUnit;
        this.colorStep = args.colorStep;
        this.endColor = args.endColor;
        this.startColor = args.startColor;
        this.threshold = args.threshold;
        this.colorStyle = args.colorStyle;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public double getRealStart() {
        return realStart;
    }

    public void setRealStart(double realStart) {
        this.realStart = realStart;
    }

    public double getRealEnd() {
        return realEnd;
    }

    public void setRealEnd(double realEnd) {
        this.realEnd = realEnd;
    }

    public double getImgStart() {
        return imgStart;
    }

    public void setImgStart(double imgStart) {
        this.imgStart = imgStart;
    }

    public double getImgEnd() {
        return imgEnd;
    }

    public void setImgEnd(double imgEnd) {
        this.imgEnd = imgEnd;
    }

    public Color[] getColorSerial() {
        return colorSerial;
    }

    public void setColorSerial(Color[] colorSerial) {
        this.colorSerial = colorSerial;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    public int getPrecision() {
        return precision;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }

    public double getConstReal() {
        return constReal;
    }

    public void setConstReal(double constReal) {
        this.constReal = constReal;
    }

    public double getConstImaginary() {
        return constImaginary;
    }

    public void setConstImaginary(double constImaginary) {
        this.constImaginary = constImaginary;
    }

    public Color getStartColor() {
        return startColor;
    }

    public void setStartColor(Color startColor) {
        this.startColor = startColor;
    }

    public Color getEndColor() {
        return endColor;
    }

    public void setEndColor(Color endColor) {
        this.endColor = endColor;
    }

    public int getColorStyle() {
        return colorStyle;
    }

    public void setColorStyle(int colorStyle) {
        this.colorStyle = colorStyle;
    }

    public int getColorStep() {
        return colorStep;
    }

    public void setColorStep(int colorStep) {
        this.colorStep = colorStep;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    public int getPaintUnit() {
        return paintUnit;
    }

    public void setPaintUnit(int paintUnit) {
        this.paintUnit = paintUnit;
    }

    @Override
    public String toString() {
        return "constReal="
                + constReal
                + ", constImaginary="
                + constImaginary
                + "  JuliaSetArgs [colorSerial=" // +
                                                 // colorSerial.length//Arrays.toString(colorSerial)
                + ", colorStep=" + colorStep + ", colorStyle=" + colorStyle + ", endColor=" + endColor
                + ", imageHeight=" + imageHeight + ", imageWidth=" + imageWidth + ", imgEnd=" + imgEnd + ", imgStart="
                + imgStart + ", precision=" + precision + ", realEnd=" + realEnd + ", realStart=" + realStart
                + ", startColor=" + startColor + ", threadCount=" + threadCount + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(colorSerial);
        result = prime * result + colorStep;
        result = prime * result + colorStyle;
        long temp;
        temp = Double.doubleToLongBits(constImaginary);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(constReal);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((endColor == null) ? 0 : endColor.hashCode());
        result = prime * result + imageHeight;
        result = prime * result + imageWidth;
        temp = Double.doubleToLongBits(imgEnd);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(imgStart);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + paintUnit;
        result = prime * result + precision;
        temp = Double.doubleToLongBits(realEnd);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(realStart);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((startColor == null) ? 0 : startColor.hashCode());
        result = prime * result + threadCount;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        JuliaSetArgs other = (JuliaSetArgs) obj;
        if (!Arrays.equals(colorSerial, other.colorSerial))
            return false;
        if (colorStep != other.colorStep)
            return false;
        if (colorStyle != other.colorStyle)
            return false;
        if (Double.doubleToLongBits(constImaginary) != Double.doubleToLongBits(other.constImaginary))
            return false;
        if (Double.doubleToLongBits(constReal) != Double.doubleToLongBits(other.constReal))
            return false;
        if (endColor == null) {
            if (other.endColor != null)
                return false;
        } else if (!endColor.equals(other.endColor))
            return false;
        if (imageHeight != other.imageHeight)
            return false;
        if (imageWidth != other.imageWidth)
            return false;
        if (Double.doubleToLongBits(imgEnd) != Double.doubleToLongBits(other.imgEnd))
            return false;
        if (Double.doubleToLongBits(imgStart) != Double.doubleToLongBits(other.imgStart))
            return false;
        if (paintUnit != other.paintUnit)
            return false;
        if (precision != other.precision)
            return false;
        if (Double.doubleToLongBits(realEnd) != Double.doubleToLongBits(other.realEnd))
            return false;
        if (Double.doubleToLongBits(realStart) != Double.doubleToLongBits(other.realStart))
            return false;
        if (startColor == null) {
            if (other.startColor != null)
                return false;
        } else if (!startColor.equals(other.startColor))
            return false;
        if (threadCount != other.threadCount)
            return false;
        if (threshold != other.threshold)
            return false;
        return true;
    }

    public void ajustJuliaArgsForImageGen(String changedProperty) {

        double proportion = getImageHeight() * 1.0 / getImageWidth();

        setRealStart(-1.0 * getRealEnd());
        setImgStart(-1 * proportion * getRealEnd());
        setImgEnd(proportion * getRealEnd());
        if (changedProperty.equals("startColor") || changedProperty.equals("endColor")
                || changedProperty.equals("colorStyle") || changedProperty.equals("colorStep")
                || changedProperty.equals("precision") || changedProperty.equals("init")) {
            float colorStep = (float) 0.001 * getColorStep();
            setColorSerial(ColorSerialGenerator.generateColorSerial(getStartColor(), getEndColor(), colorStep,
                    getPrecision(), getColorStyle()));
        }
    }

    public void adjustToDeminsion(int width, int height) {
        if (this.imageWidth == width && this.imageHeight == height) {
            return;
        }
        setImageWidth(width);
        setImageHeight(height);
        double proportion = height * 1.0 / width;
        setRealStart(-1.0 * getRealEnd());
        setImgStart(-1 * proportion * getRealEnd());
        setImgEnd(proportion * getRealEnd());
    }

}
