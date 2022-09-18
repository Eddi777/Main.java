import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GraphBuilder {

    private int graphHeight;
    private int graphWidth;
    private BufferedImage graph;

    public GraphBuilder(BufferedImage graph, int graphHeight, int graphWidth) {
        this.graphHeight = graphHeight;
        this.graphWidth = graphWidth;
        this.graph = graph;
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
}
