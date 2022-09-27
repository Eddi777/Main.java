import Atabana.Atabana;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Main {


    //Main - remove before deploy
    public static void main(String[] args) throws Exception {
        final int graphWidth = 1000;
        final int graphHeight = 200;




        Path testFile = Paths.get("C:\\Users\\eduar\\IdeaProjects\\YandexMusic\\AudioFiles\\Sherlock.mp3");
//        Path testFile = Paths.get("C:\\Users\\eduar\\IdeaProjects\\YandexMusic\\AudioFiles\\Виктор Цой - Пачка сигарет (SadSvit remix) (mp3-2020.com).mp3");
//        Path testFile = Paths.get("C:\\Users\\eduar\\IdeaProjects\\YandexMusic\\AudioFiles\\Minelli_-_Rampampam_73039421.mp3");
//        Path testFile = Paths.get("C:\\Users\\eduar\\IdeaProjects\\YandexMusic\\AudioFiles\\gitara-melodiya-rok-n-roll-korotkaya.mp3");

//        Path testFile = Paths.get("C:\\Users\\eduar\\IdeaProjects\\YandexMusic\\AudioFiles\\korotkaya-melodiya-veseloe-otkryitie-animirovannyiy-fonovyiy-zvuk-igryi-40627.mp3");
//        Path testFile = Paths.get("C:\\Users\\eduar\\OneDrive\\Documents\\Personal\\YandexMusic\\DataSet\\archive\\Data\\genres_original\\rock\\rock.00014.wav");
//        Path testFile = Paths.get("C:\\Users\\eduar\\IdeaProjects\\YandexMusic\\AudioFiles\\Digital Presentation_48000.wav");
//        Path testFile = Paths.get("C:\\Users\\eduar\\IdeaProjects\\YandexMusic\\AudioFiles\\Wav_868kb.wav");
//        Path testFile = Paths.get("C:\\Users\\eduar\\IdeaProjects\\YandexMusic\\AudioFiles\\Guitar.wav");



        //Read the audiofile and create Atabana object
        String filename = testFile.getFileName().toString();
        InputStream inFile = Files.newInputStream(testFile);
        byte[] bytes = inFile.readAllBytes();
        Atabana music = new Atabana(filename, bytes);
        System.out.println(music.getFileData());

        ArrayList<GraphObject> graphs = new ArrayList<>();
        music.setGraphSize(graphHeight, graphWidth);
        graphs.add(new GraphObject("Wave"));
        graphs.add(new GraphObject("ZeroCross"));
        graphs.add(new GraphObject("SimpleSoundPower"));
        graphs.add(new GraphObject("Spectrogram"));

        music.setWindow(2000, 2030);
        for (GraphObject item: graphs) {
            item.analyser = music.getAnalyser(item.name);
//            item.chartData = music.getAnalyserWindowArray(item.name);
//            item.chartParams = music.getAnalyserWindowParameters(item.name);
//            item.chartData = music.getAnalyserArray(item.name);
            item.chartParams = music.getAnalyserParameters(item.name);
            System.out.println(item.chartParams.toString());
            item.chart = music.getChartImage(
                    (String) item.chartParams.get("Graph"),
                    item.analyser);
//            item.chart = music.getWindowChartImage(
//                    (String) item.chartParams.get("Graph"),
//                    item.analyser);
        }

            //Sound wave graph creation
        GraphBuilder gb = new GraphBuilder(graphs, graphHeight, graphWidth);
        gb.setTitle(filename);
        gb.showGraphs();

        System.out.println(" ATABANA finished ! ");
    }



}
