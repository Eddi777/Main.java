import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;

public class ChartCreatorLine {
    private final int[] data;
    private int graphHeight;
    private int graphWidth;
    private double averageValue;

    private String graphName;
    private BufferedImage graph;

    private boolean isGraphReady = false;

    public ChartCreatorLine(ArrayList<?> data, String name, int graphWidth, int graphHeight) {
        this(data, name, graphWidth, graphHeight, 0);
    }

    public ChartCreatorLine(ArrayList<?> data, String name, int graphWidth, int graphHeight, double average) {
        this.data = data.stream().map(e -> (Integer) e).mapToInt(Integer::new).toArray();
        this.graphHeight = graphHeight;
        this.graphWidth = graphWidth;
        this.graphName = name;
        this.averageValue = average;
        graph = new BufferedImage(this.graphWidth, this.graphHeight, BufferedImage.TYPE_INT_RGB);
    }

    public BufferedImage getGraph() {
        if (!isGraphReady) {
            createGraph();
        }
        return graph;
    }

    private void createGraph() {
        Graphics2D chart = graph.createGraphics();
        int max = Arrays.stream(data).max().getAsInt();
        int min = Arrays.stream(data).min().getAsInt();

        //Draw graph
        chart.setColor(Color.blue);
        int x;
        int y;
        int xPrev = 0;
        int yPrev = (int) (graphHeight * ((double) (max - data[0]) / (max - min)));

        for (int i = 1; i < data.length; i++) {
            x = (int) (graphWidth * ((double) i / data.length));
            y = (int) (graphHeight * ((double) (max - data[i]) / (max - min)));
            chart.drawLine(xPrev, yPrev, x, y);
            xPrev = x;
            yPrev = y;
        }
            //Prepare text
        chart.setColor(Color.WHITE);
        chart.drawString(String.valueOf(max), 10, 10);
        chart.drawString(String.valueOf(min), 10, graphHeight - 50);
        chart.drawString(graphName, graphWidth / 2 - 200, 15);

        if ((min >= 0) && (max > 0 )) {
                //Draw average line
            chart.setColor(Color.GRAY);
            y = (int) (graphHeight * ((max - averageValue) / (max - min)));
            chart.drawLine(0, y, graphWidth, y);
        } else {
                //Draw zero line
            chart.setColor(Color.WHITE);
            y = (int) (graphHeight * ((double) max / (max - min)));
            chart.drawLine(0, y, graphWidth, y);
        }


        chart.dispose();
        isGraphReady = true;
    }

    public void saveGraphToFile(Path filename) throws IOException {
        File file = new File("img_graph.jpg");
        ImageIO.write(graph, "jpg", file);
    }

    public void showGraph() {
        ImageIcon imageIcon = new ImageIcon(getGraph());
        JFrame jFrame = new JFrame();

        jFrame.setLayout(new FlowLayout());

        jFrame.setSize(graphWidth, graphHeight);
        jFrame.setTitle("Wave graph");
        JLabel jLabel = new JLabel();

        jLabel.setIcon(imageIcon);
        jFrame.add(jLabel);

        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setResizable(false);

        jFrame.setVisible(true);
    }

}
