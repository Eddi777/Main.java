import Atabana.Analysers.Analyser;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Map;

public class GraphObject {

    String name;
    Analyser analyser;
    ArrayList<?> chartData;
    Map<String, Object> chartParams;
    BufferedImage chart;

    public GraphObject(String name) {
        this.name = name;
    }
}
