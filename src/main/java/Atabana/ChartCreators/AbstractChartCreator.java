package Atabana.ChartCreators;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Map;

public abstract class AbstractChartCreator implements ChartCreator{

    protected ArrayList<?> data;
    protected Map<String, Object> params;
    protected int graphHeight;
    protected int graphWidth;
    protected BufferedImage graph;
    protected boolean isReady = false;

    public void setChartCreator(ArrayList<?> data, Map<String, Object> params, int graphWidth, int graphHeight) {
        this.params = params;
        this.graphHeight = graphHeight;
        this.graphWidth = graphWidth;
        this.data = data;
        graph = new BufferedImage(this.graphWidth, this.graphHeight, BufferedImage.TYPE_INT_RGB);
        this.isReady = false;
    }

    public BufferedImage getChart() throws Exception {
        if (!isReady) {
            createChart();
        }
        return graph;
    }

    public void saveChartToFile(Path filename) throws Exception {
        if (!isReady) {
            createChart();
        }
        File file = new File(String.valueOf(filename));
        ImageIO.write(graph, "jpg", file);
    }
    protected abstract void createChart() throws Exception;
}
