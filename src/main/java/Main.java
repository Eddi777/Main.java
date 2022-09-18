import Atabana.Atabana;
import Atabana.Lib.Analyser;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {

    //Main - remove before deploy
    public static void main(String[] args) throws Exception {
        final int graphWidth = 1000;
        final int graphHeight = 300;




//        Path testFilePath = Paths.get("C:\\Users\\eduar\\OneDrive\\Documents\\Personal\\YandexMusic\\DataSet\\Sherlock.mp3");
//        Path testFile = Paths.get("C:\\Users\\eduar\\OneDrive\\Documents\\Personal\\YandexMusic\\DataSet\\archive\\Data\\genres_original\\rock\\rock.00014.wav");
//        Path testFile = Paths.get("C:\\Users\\eduar\\IdeaProjects\\YandexMusic\\AudioFiles\\Digital Presentation_48000.wav");
//        Path testFile = Paths.get("C:\\Users\\eduar\\IdeaProjects\\YandexMusic\\AudioFiles\\Wav_868kb.wav");
        Path testFile = Paths.get("C:\\Users\\eduar\\IdeaProjects\\YandexMusic\\AudioFiles\\Guitar.wav");



        //Read the audiofile and create Atabana object
        String filename = testFile.getFileName().toString();
        InputStream inFile = Files.newInputStream(testFile);
        byte[] bytes = inFile.readAllBytes();
        Atabana music = new Atabana(filename, bytes);
        System.out.println(music.getFileData());


        ArrayList<GraphObject> graphs = new ArrayList<>();
        graphs.add(new GraphObject("Wave"));
        graphs.add(new GraphObject("ZeroCross"));
        graphs.add(new GraphObject("SimpleSoundPower"));

        for (GraphObject item: graphs) {
            item.graphData = music.getAnalyserArray(item.name);
            item.graphParams = music.getAnalyserParameters(item.name);
            System.out.println(item.graphParams.toString());
            item.graph = new ImageBuilder(item.graphData, item.graphParams, graphWidth, graphHeight).getGraph();
        }

            //Sound wave graph creation
        new GraphBuilder(graphs, graphHeight, graphWidth).showGraphs();

        System.out.println(" ATABANA finished !");
    }



}
