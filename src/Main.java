import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        System.out.println("Java is looking in: " + new java.io.File(".").getAbsolutePath());
        Graph graph = new Graph();


        Config config = new Config("fuchtermann", 100, 1000, 1000);

        Cords cords = new Cords(0);

        Gui graphWindow = new Gui(cords, graph, config);

    }
}