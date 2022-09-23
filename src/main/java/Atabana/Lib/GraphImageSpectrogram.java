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

    @Override
    protected void createGraphImage() throws Exception {
        boolean logModeEnabled = true;
        Graphics2D chart = super.graph.createGraphics();


//        BufferedImage chart = new BufferedImage(super.graphWidth, super.graphHeight, BufferedImage.TYPE_INT_RGB );

        //Detect max & min values
        double max = data.stream().flatMapToDouble(item -> Arrays.stream((double[]) item)).max().getAsDouble();
        double min = data.stream().flatMapToDouble(item -> Arrays.stream((double[]) item)).min().getAsDouble();
        int arrayLength = data.size(); //Length of data array
        int arrayHeight = ((double[]) data.get(0)).length; // height of data array

        //Draw graph
        int x;
        int y;
        for (int xArray = 0; xArray < arrayLength; xArray++) {
            x = (int) (graphWidth * (((double) xArray) / arrayLength));

            for (int yArray = 0; yArray < arrayHeight; yArray++) {
                if (logModeEnabled) {
                    y = (int) ((graphHeight - 10) * (1 - Math.log10(yArray+1) / Math.log10(arrayHeight))) + 5;
                } else {
                    y = (int) ((graphHeight - 10) * (1 - ((double) yArray) / (double) arrayHeight)) + 5;
                }
//                chart.setRGB(x, y, getColor(((double[]) data.get(xArray))[yArray])


                chart.fillRect();
                );

            }
        }
//        graph = chart;
//        Prepare text
//        chart.setColor(Color.GREEN);
//        chart.drawString("0", 10, 10);
//        chart.drawString(String.valueOf(min), 10, graphHeight - 10);
//        chart.drawString((String) params.get("GraphName"), graphWidth / 2 - 200, 15);
//        //Draw average line
//        chart.setColor(Color.GRAY);
//        y = (int) (graphHeight * ((max - (double) params.get("Average")) / (max - min)));
//        chart.drawLine(0, y, graphWidth, y);
//        chart.drawString("Average: " + ((double) params.get("Average")), 10, y - 10);
//        //Draw borders
//        chart.setColor(Color.LIGHT_GRAY);
//        chart.drawLine(0, 0, graphWidth, 0);
//        chart.drawLine(0, graphHeight-1, graphWidth, graphHeight-1);
//        //Draw zero line
//        chart.setColor(Color.YELLOW);
//        chart.drawLine(0, 0, graphWidth, 0);
//        chart.dispose();
        isReady = true;
    }

    private int getColor(double colorNumber) {
        double magnitude = Math.log(Math.abs(colorNumber)+1);
        return new Color(0, (int) magnitude * 10, (int) magnitude * 20).getRGB();


//        if (colorNumber > 5000) {
//            return Color.RED.getRGB();
//        } else if (colorNumber > 0) {
//            return Color.PINK.getRGB();
//        } else if (colorNumber > -5000) {
//            return Color.CYAN.getRGB();
//        } else if (colorNumber < -5000) {
//            return Color.BLUE.getRGB();
//        }
//        return Color.WHITE.getRGB();
    }
}
