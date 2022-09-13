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

    private BufferedImage graph;

    private int posStart;
    private int posEnd;
    private boolean isGraphReady = false;

    public ChartCreatorLine(ArrayList<?> data, int graphWidth, int graphHeight) {
        int[] array = new int[data.size()];
        for (int i = 0; i < data.size(); i++) {
            array[i] = ((Integer) data.get(i));
        }
        this.data = array;
        this.posStart = 0;
        this.posEnd = this.data.length;
        this.graphHeight = graphHeight;
        this.graphWidth = graphWidth;
        graph = new BufferedImage(this.graphWidth, this.graphHeight, BufferedImage.TYPE_INT_RGB);
    }

    public void setCanvasSize(int width, int height) {
        graphHeight = height;
        graphWidth = width;
        graph = new BufferedImage(this.graphWidth, this.graphHeight, BufferedImage.TYPE_INT_RGB);
        isGraphReady = false;
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
        int yPrev = (int) (graphHeight * ((double) (data[posStart] - min) / ((double) (max - min))));

        for (int i = posStart + 1; i < posEnd; i++) {
            x = (int) (graphWidth * (((double) i - (double) posStart) / ((double) posEnd - (double) posStart)));
            y = (int) (graphHeight * ((double) (data[i] - min) / ((double) (max - min))));
            chart.drawLine(xPrev, yPrev, x, y);
            xPrev = x;
            yPrev = y;
        }
        //Prepare axis
        chart.setColor(Color.WHITE);
        y = (int) (graphHeight * ((double) - min / (double) (max - min)));
        chart.drawLine(0, y, graphWidth, y);
        chart.drawString("Wave line", 15, 15);

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
