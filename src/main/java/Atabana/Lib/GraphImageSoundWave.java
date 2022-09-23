package Atabana.Lib;

import java.awt.*;

/**
 * Graph image for soundwave chart
 * Data type is Integer array
 * X axis is center aligned
**/

public class GraphImageSoundWave extends GraphImage{
    @Override
    protected void createGraphImage() throws Exception {
        Graphics2D chart = super.graph.createGraphics();
        int max = data.stream().map(e -> (int) e).mapToInt(Integer::new).max().getAsInt();
        int min = data.stream().map(e -> (int) e).mapToInt(Integer::new).min().getAsInt();

            //Draw graph
        chart.setColor(Color.BLUE);
        int x;
        int y;
        int xPrev = 0;
        int yPrev = (int) (graphHeight * ((double)(max - (int) data.get(0)) / (max - min)));

        for (int i = 1; i < data.size(); i++) {
            x = (int) (graphWidth * ((double) i / data.size()));
            y = (int) (graphHeight * ((double)(max - (int) data.get(i)) / (max - min)));
            chart.drawLine(xPrev, yPrev, x, y);
            xPrev = x;
            yPrev = y;
        }
            //Prepare text
        chart.setColor(Color.WHITE);
        chart.drawString(String.valueOf(max), 10, 10);
        chart.drawString(String.valueOf(min), 10, graphHeight - 10);
        chart.drawString((String) params.get("GraphName"), graphWidth / 2 - 200, 15);

        chart.setColor(Color.GRAY);
        if ((min >= 0) && (max > 0 )) {
            //Average line
            chart.setColor(Color.GRAY);
            y = (int) (graphHeight * ((max - (double) params.get("Average")) / (max - min)));
            chart.drawLine(0, y, graphWidth, y);
            chart.drawString("Average: " + params.get("Average"), 10, y - 10);
        } else {
            //Zero line
            chart.setColor(Color.YELLOW);
            y = (int) (graphHeight * ((double) max / (max - min)));
            chart.drawLine(0, y, graphWidth, y);
            chart.drawString("Zero", 10, y - 10);
        }
            //Draw borders
        chart.setColor(Color.LIGHT_GRAY);
        chart.drawLine(0, 0, graphWidth, 0);
        chart.drawLine(0, graphHeight-1, graphWidth, graphHeight-1);

        chart.dispose();
        isReady = true;
    }
}
