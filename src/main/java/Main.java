import Atabana.Atabana;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    //Main - remove before deploy
    public static void main(String[] args) throws Exception {

        //Path testFilePath = Paths.get("C:\\Users\\eduar\\OneDrive\\Documents\\Personal\\YandexMusic\\DataSet\\Sherlock.mp3");
        //Path testFile = Paths.get("C:\\Users\\eduar\\OneDrive\\Documents\\Personal\\YandexMusic\\DataSet\\archive\\Data\\genres_original\\rock\\rock.00014.wav");
         Path testFile = Paths.get("C:\\Users\\eduar\\IdeaProjects\\YandexMusic\\AudioFiles\\Digital Presentation_48000.wav");

        //Read the audiofile and create Atabana object
        String filename = testFile.getFileName().toString();
        InputStream inFile = Files.newInputStream(testFile);
        byte[] bytes = inFile.readAllBytes();
        Atabana music = new Atabana(filename, bytes);

        System.out.println(music.getFileData());
        System.out.println(music.getAnalyserParameters("Wave").toString());
            //Sound wave graph creation
        ArrayList<?> graphData= music.getAnalyserArray("Wave");
        ChartCreatorLine graph = new ChartCreatorLine(graphData,1000,400);
        graph.showGraph();

        System.out.println("/n Finished !");
    }

}
