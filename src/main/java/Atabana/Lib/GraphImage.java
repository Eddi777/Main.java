package Atabana.Lib;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Map;

public abstract class GraphImage {

    protected ArrayList<?> data;
    protected Map<String, Object> params;
    protected int graphHeight;
    protected int graphWidth;
    protected BufferedImage graph;
    protected boolean isReady = false;

    public void setGraphImage(ArrayList<?> data, Map<String, Object> params, int graphWidth, int graphHeight) {
        this.params = params;
        this.graphHeight = graphHeight;
        this.graphWidth = graphWidth;
        this.data = data;
        graph = new BufferedImage(this.graphWidth, this.graphHeight, BufferedImage.TYPE_INT_RGB);
        this.isReady = false;
    }

    public BufferedImage getGraph() throws Exception {
        if (!isReady) {
            createGraphImage();
        }
        return graph;
    }

    public void saveGraphToFile(Path filename) throws Exception {
        if (!isReady) {
            createGraphImage();
        }
        File file = new File(String.valueOf(filename));
        ImageIO.write(graph, "jpg", file);
    }
    protected abstract void createGraphImage() throws Exception;
}
