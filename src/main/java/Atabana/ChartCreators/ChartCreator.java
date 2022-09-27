package Atabana.ChartCreators;

import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Map;

public interface ChartCreator {

    void setChartCreator(ArrayList<?> data, Map<String, Object> params, int graphWidth, int graphHeight);
    BufferedImage getChart() throws Exception;
    void saveChartToFile(Path filename) throws Exception;
}
