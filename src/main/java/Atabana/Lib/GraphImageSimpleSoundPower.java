package Atabana.Lib;

import Atabana.Lib.GraphImage;

import java.awt.*;

/**
 * Graph image for sound power chart
 * Data type is Double array
 * X axis is top aligned
 * Y axis is 0 in the top goes to minus
 **/

public class GraphImageSimpleSoundPower extends GraphImage {

    @Override
    protected void createGraphImage() throws Exception {
        Graphics2D chart = super.graph.createGraphics();
        int max = (int) data.stream().map(e -> (double) e).mapToDouble(Double::new).max().getAsDouble();
        int min = (int) data.stream().map(e -> (double) e).mapToDouble(Double::new).min().getAsDouble();

        //Draw graph
        chart.setColor(Color.BLUE);
        int x;
        int y;
        int xPrev = 0;
        int yPrev = (int) (graphHeight * ((max - (double) data.get(0) / (max - min))));

        for (int i = 1; i < data.size(); i++) {
            x = (int) (graphWidth * ((double) i / data.size()));
            y = (int) ((graphHeight-10) * ((max - (double) data.get(i)) / (max - min)))+5;
            chart.drawLine(xPrev, yPrev, x, y);
            xPrev = x;
            yPrev = y;
        }

        //Prepare text
        chart.setColor(Color.WHITE);
        chart.drawString(String.valueOf(max), 10, 10);
        chart.drawString(String.valueOf(min), 10, graphHeight - 10);
        chart.drawString((String) params.get("GraphName"), graphWidth / 2 - 200, 15);
        //Draw average line
        chart.setColor(Color.GRAY);
        y = (int) (graphHeight * ((max - (double) params.get("Average")) / (max - min)));
        chart.drawLine(0, y, graphWidth, y);
        chart.drawString("Average: " + ((double) params.get("Average")), 10, y - 10);
        //Draw borders
        chart.setColor(Color.LIGHT_GRAY);
        chart.drawLine(0, 0, graphWidth, 0);
        chart.drawLine(0, graphHeight-1, graphWidth, graphHeight-1);
        //Draw zero line
        chart.setColor(Color.YELLOW);
        chart.drawLine(0, 0, graphWidth, 0);
        chart.dispose();
        isReady = true;
    }
}
