import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class GraphBuilder {

    private int graphHeight;
    private int graphWidth;
    private BufferedImage graph;
    private ArrayList<GraphObject> graphs;

    private int NumOfGraphs;

    public GraphBuilder(BufferedImage graph, int graphHeight, int graphWidth) {
        this.graphHeight = graphHeight;
        this.graphWidth = graphWidth;
        this.graph = graph;
    }
    public GraphBuilder(ArrayList<GraphObject> graphObjects, int graphHeight, int graphWidth) {
        this.graphHeight = graphHeight;
        this.graphWidth = graphWidth;
        this.graphs = graphObjects;
    }

    public void showGraph() {
        ImageIcon imageIcon = new ImageIcon(graph);
        JFrame jFrame = new JFrame();

        jFrame.setLayout(new FlowLayout());

        jFrame.setSize(graphWidth, graphHeight + 44);
        jFrame.setTitle("ATABANA GRAPH");
        JLabel jLabel = new JLabel();

        jLabel.setIcon(imageIcon);
        jFrame.add(jLabel);

        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setResizable(false);

        jFrame.setVisible(true);
    }

    public void showGraphs() {
            //—борка отдельных картинок в одну
        int height = graphHeight * graphs.size();
        BufferedImage image = new BufferedImage(
                graphWidth,
                height,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D chart = image.createGraphics();
        int i = 0;
        for (GraphObject item: graphs) {
            chart.drawImage(item.graph, 0, i * graphHeight,null);
            i++;
        }
        chart.dispose();

        ImageIcon imageIcon = new ImageIcon(image);
        JFrame jFrame = new JFrame();

        jFrame.setLayout(new FlowLayout());

        jFrame.setSize(graphWidth, height + 44);
        jFrame.setTitle("ATABANA GRAPHS");
        JLabel jLabel = new JLabel();

        jLabel.setIcon(imageIcon);
        jFrame.add(jLabel);

        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setResizable(false);

        jFrame.setVisible(true);



    }
}
