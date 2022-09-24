package Atabana.Lib;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

/**
 * Graph image for sound power chart
 * Data type is Double array
 * X axis is top aligned
 * Y axis is 0 in the top goes to minus
 **/

public class GraphImageSpectrogram extends GraphImage {

    int colorCoeff;

    @Override
    protected void createGraphImage() throws Exception {
        boolean logModeEnabled = true;
        Graphics2D chart = super.graph.createGraphics();


//        BufferedImage chart = new BufferedImage(super.graphWidth, super.graphHeight, BufferedImage.TYPE_INT_RGB );

        //Detect max & min values
        double max = data.stream().flatMapToDouble(item -> Arrays.stream((double[]) item)).max().getAsDouble();
        colorCoeff = (int) (256 / Math.log(max));
        double min = data.stream().flatMapToDouble(item -> Arrays.stream((double[]) item)).min().getAsDouble();
        int arrayLength = data.size(); //Length of data array
        int arrayHeight = ((double[]) data.get(0)).length; // height of data array


        //Normalization of the spectrogram
        // for future work look at https://knowm.org/exploring-bird-song-with-a-spectrogram-in-java/
        //Set max & miv value
//        double maxValue = output.stream().flatMapToDouble(item -> Arrays.stream((double[]) item)).max().getAsDouble();
//        double minValue = output.stream().flatMapToDouble(item -> Arrays.stream((double[]) item)).min().getAsDouble();
//        minValue = (minValue == 0) ? 0.00000001 : minValue;
//        //normalization of the output
//        for (int x = 0; x < output.size(); x++) {
//            for (int y = 0; y < ((double[]) output.get(0)).length; y++) {
//                if (((double[]) output.get(x))[y] <= 0) {
//                    ((double[]) output.get(x))[y] = minValue;
//                } else {
//                    ((double[]) output.get(x))[y] = Math.abs(
//                            Math.log10(((double[]) output.get(x))[y] / maxValue));
//                }
//            }
//        }



        //Draw graph
        int x;
        int xPrev = 0;
        int y;
        int yPrev;
        for (int xArray = 0; xArray < arrayLength; xArray++) {
            x = (int) (graphWidth * (((double) xArray) / arrayLength));
            yPrev = 0;
            for (int yArray = 0; yArray < arrayHeight; yArray++) {
                if (logModeEnabled) {
                    y = (int) ((graphHeight - 2) * (1 - Math.log10(yArray+1) / Math.log10(arrayHeight))) + 1;
                } else {
                    y = (int) ((graphHeight - 2) * (1 - ((double) yArray) / (double) arrayHeight)) + 1;
                }
                chart.setColor(getColor(((double[]) data.get(xArray))[yArray]));
                chart.fillRect(x, y, xPrev, yPrev);
                yPrev = y;
            }
            xPrev = x;
        }
//        graph = chart;
        chart.setColor(Color.WHITE);
        chart.drawString((String) params.get("GraphName"), graphWidth / 2 - 200, 15);
        isReady = true;
    }

    private Color getColor(double colorNumber) {
        double magnitude = Math.log(Math.abs(colorNumber)+1);
        return new Color(0, (int) magnitude*colorCoeff, (int) magnitude*colorCoeff);
    }
}
