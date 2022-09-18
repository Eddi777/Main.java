import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Map;

public class ImageBuilder {

    private final ArrayList<?> data;
    private final Map<String, Object> params;
    private int graphHeight;
    private int graphWidth;
    private BufferedImage graph;
    private boolean isGraphReady = false;

    public ImageBuilder(ArrayList<?> data, Map<String, Object> params, int graphWidth, int graphHeight) {
        this.params = params;
        this.graphHeight = graphHeight;
        this.graphWidth = graphWidth;
        this.data = data;
        graph = new BufferedImage(this.graphWidth, this.graphHeight, BufferedImage.TYPE_INT_RGB);
    }

    public BufferedImage getGraph() throws Exception {
        if (!isGraphReady) {
            createGraph();
        }
        return graph;
    }

    private void createGraph() throws Exception {
        if (params.get("Values") == "int") {
            create2DIntGraph();
        } else if ((params.get("Values") == "double")) {
            create2DDoubleZeroAxisCraph();
        } else {
            throw new Exception("There is not graph mode available for analyser " + params.get("Analyser"));
        }
        isGraphReady = true;
    }

    private void create2DIntGraph() {
        Graphics2D chart = graph.createGraphics();
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

        if ((min >= 0) && (max > 0 )) {
            //Draw average line
            chart.setColor(Color.GRAY);
            y = (int) (graphHeight * ((max - (double) params.get("Average")) / (max - min)));
            chart.drawLine(0, y, graphWidth, y);
            chart.drawString("Average: " + params.get("Average"), 10, y - 10);
        } else {
            //Draw zero line
            chart.setColor(Color.GRAY);
            y = (int) (graphHeight * ((double) max / (max - min)));
            chart.drawLine(0, y, graphWidth, y);
            chart.drawString("Zero", 10, y - 10);
        }
        chart.dispose();
    }

    private void create2DDoubleZeroAxisCraph(){
        Graphics2D chart = graph.createGraphics();
        int max = 0;
        int min = (int) data.stream().map(e -> (double) e).mapToDouble(Double::new).min().getAsDouble();

        //Draw graph
        chart.setColor(Color.BLUE);
        int x;
        int y;
        int xPrev = 0;
        int yPrev = (int) (graphHeight * ((max - (double) data.get(0) / (max - min))));

        for (int i = 1; i < data.size(); i++) {
            x = (int) (graphWidth * ((double) i / data.size()));
            y = (int) (graphHeight * ((max - (double) data.get(i)) / (max - min)));
            chart.drawLine(xPrev, yPrev, x, y);
            xPrev = x;
            yPrev = y;
        }

        //Prepare text
        chart.setColor(Color.WHITE);
        chart.drawString("0", 10, 10);
        chart.drawString(String.valueOf(min), 10, graphHeight - 10);
        chart.drawString((String) params.get("GraphName"), graphWidth / 2 - 200, 15);
            //Draw average line
        chart.setColor(Color.GRAY);
        y = (int) (graphHeight * ((max - (double) params.get("Average")) / (max - min)));
        chart.drawLine(0, y, graphWidth, y);
        chart.drawString("Average: " + ((double) params.get("Average")), 10, y - 10);
            //Draw zero line
        chart.setColor(Color.WHITE);
        chart.drawLine(0, 0, graphWidth, 0);
        chart.dispose();
    }


    public void saveGraphToFile(Path filename) throws Exception {
        if (!isGraphReady) {
            createGraph();
        }
        String fileName = "img_graph.jpg";
        File file = new File(fileName);
        ImageIO.write(graph, "jpg", file);
    }
}
