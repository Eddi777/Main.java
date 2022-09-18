import Atabana.Lib.Analyser;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Map;

public class GraphObject {

    String name;
    Analyser analyser;
    ArrayList<?> graphData;
    Map<String, Object> graphParams;
    BufferedImage graph;

    public GraphObject(String name) {
        this.name = name;
    }
}
