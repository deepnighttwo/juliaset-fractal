package com.deepnighttwo.util;

import java.awt.Color;

public class ColorSerialGenerator {

    private final static int minColorSerial = 5;

    public static Color[] generateColorSerial(Color startColor, Color endColor, double colorStep, int maxColorSerial,
            int colorGenerationOption) {
        switch (colorGenerationOption) {
            case 0:
                return createColorSerialAVG(startColor, endColor, colorStep, maxColorSerial);
            case 1:
                return createColorSerialChaningH(startColor, endColor, colorStep, maxColorSerial);
            case 2:
                return createTwoColor(startColor, endColor, maxColorSerial);
                // return createColorSerialDefault(maxColorSerial);
        }
        return null;
    }

    private static Color[] createTwoColor(Color startColor, Color endColor, int maxColorSerial) {
        Color[] color = new Color[maxColorSerial];
        color[0] = startColor;
        for (int i = 1; i < maxColorSerial; i++) {
            color[i] = endColor;
        }
        return color;
    }

    private static Color[] createColorSerialChaningH(Color startColor, Color endColor, double colorStep,
            int maxColorSerial) {
        Color[] colorSerial;

        int sr = startColor.getRed();
        int sg = startColor.getGreen();
        int sb = startColor.getBlue();
        float[] startHSB = Color.RGBtoHSB(sr, sg, sb, null);

        int er = endColor.getRed();
        int eg = endColor.getGreen();
        int eb = endColor.getBlue();
        float[] endHSB = Color.RGBtoHSB(er, eg, eb, null);

        double diff = Math.abs(startHSB[0] - endHSB[0]);

        int serial = (int) (diff / colorStep + 0.5);
        if (serial > maxColorSerial) {
            serial = maxColorSerial;
        } else if (serial < minColorSerial) {
            serial = minColorSerial;
        }

        double stepH = (startHSB[0] - endHSB[0]) / serial;

        double deltaH = 0;
        float middleS = (startHSB[1] + endHSB[1]) / 2;
        float middleB = (startHSB[2] + endHSB[2]) / 2;

        colorSerial = new Color[serial];
        colorSerial[0] = startColor;

        for (int i = 1; i < serial; i++) {
            deltaH -= stepH;
            colorSerial[i] = Color.getHSBColor((float) (startHSB[0] + deltaH), middleS, middleB);
        }
        return colorSerial;
    }

    private static Color[] createColorSerialAVG(Color startColor, Color endColor, double colorStep, int maxColorSerial) {
        Color[] colorSerial;

        int sr = startColor.getRed();
        int sg = startColor.getGreen();
        int sb = startColor.getBlue();
        float[] startHSB = Color.RGBtoHSB(sr, sg, sb, null);

        int er = endColor.getRed();
        int eg = endColor.getGreen();
        int eb = endColor.getBlue();
        float[] endHSB = Color.RGBtoHSB(er, eg, eb, null);

        double diff = Math.abs(startHSB[0] - endHSB[0]) + Math.abs(startHSB[1] - endHSB[1])
                + Math.abs(startHSB[2] - endHSB[2]);

        // int serial = maxColorSerial;
        int serial = (int) (diff / colorStep + 0.5);
        if (serial > maxColorSerial) {
            serial = maxColorSerial;
        } else if (serial < minColorSerial) {
            serial = minColorSerial;
        }

        double stepH = (startHSB[0] - endHSB[0]) / serial;
        double stepS = (startHSB[1] - endHSB[1]) / serial;
        double stepB = (startHSB[2] - endHSB[2]) / serial;

        double deltaH = 0;
        double deltaS = 0;
        double deltaB = 0;

        colorSerial = new Color[serial];
        colorSerial[0] = startColor;

        for (int i = 1; i < serial; i++) {
            deltaH -= stepH;
            deltaS -= stepS;
            deltaB -= stepB;

            colorSerial[i] = Color.getHSBColor((float) (startHSB[0] + deltaH), (float) (startHSB[1] + deltaS),
                    (float) (startHSB[2] + deltaB));
        }
        return colorSerial;
    }
}
