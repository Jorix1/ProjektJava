import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    static void main(String[] args) throws IOException {
        System.out.println("Java is looking in: " + new java.io.File(".").getAbsolutePath());
        Graph graph = new Graph();
        graph =  InputOutput.readFileEdge(new File("src/graphDane1.txt"));

        Config config = new Config("fuchtermann",100, 100, 100, true );

        Cords cords = new Cords(graph.getNumNodes());
        FuchterMann algo = new FuchterMann(config);
        algo.executeAlgo(cords, graph);

        Gui graphWindow = new Gui(cords,graph);

    }
}