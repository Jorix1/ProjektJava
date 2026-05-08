import java.io.*;
import java.security.AllPermission;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputOutput {

    /* plik input output posiada takie funkcje jak:
    zczytywanie krafu odczytuje dane z pliku input znaczy się pliku z krawędziami
    wczytywanie pliku z wierzchołkami
    zapisywanie wps do teksut
    zapisywanie wps do binarki
     */

    static final String regexTxt = "(\\d+)\\s+([-+]?\\d+(\\.\\d+)?)\\s+([-+]?\\d+(\\.\\d+)?)";
    static final String regexForExtract = "(\\w+)\\s+(\\d+)\\s+(\\d+)\\s+([-+]?\\d+(\\.\\d+)?)";
    static Pattern pattern = Pattern.compile(regexForExtract);
    static Pattern patternForNode = Pattern.compile(regexTxt);
    public Graph.AdjListAll readFileEdge(File file) throws IOException, IllegalArgumentException {

        try(BufferedReader reader = new BufferedReader(new FileReader(file))){
            Graph.AdjListAll AllLinkedList = new Graph.AdjListAll();
            String line;

            String edgeName;
            int node1;
            int node2;
            double weight;

            int lineNumber = 0;
            while((line = reader.readLine()) != null){
                lineNumber++;
                Matcher matcher = pattern.matcher(line);
                if(line.trim().isEmpty())continue;

                if(matcher.find()){
                     edgeName = matcher.group(1); // regex 1-nazwa 2-node1 3-node2
                     node1 = Integer.parseInt(matcher.group(2));
                     node2 = Integer.parseInt(matcher.group(3));
                     weight = Double.parseDouble(matcher.group(4));

                     if(AllLinkedList.adjList.containsKey(node1)){
                         AllLinkedList.AddElementLL(node1, node2, weight);
                     }else{
                         AllLinkedList.adjList.put(node1, new LinkedList<>());
                         AllLinkedList.AddElementLL(node1, node2, weight);
                     }
                     if(AllLinkedList.adjList.containsKey(node2)){
                         AllLinkedList.AddElementLL(node2, node1, weight);
                     }else{
                         AllLinkedList.adjList.put(node2, new LinkedList<>());
                         AllLinkedList.AddElementLL(node2, node1, weight);
                     }


                }else {
                    throw new IOException ("Not found any matches in file line number:"+lineNumber);
                }
            }
            return AllLinkedList;
        }


    }

//    public void readFileNodeTxt (File fileNodeTxt, List<Graph.Node> NodeList) throws IOException, IllegalArgumentException {
//        try(BufferedReader reader = new BufferedReader(new FileReader(fileNodeTxt))) {
//
//            int Id;
//            double X;
//            double Y;
//
//            String line;
//
//
//            int lineNumber = 0;
//            while ((line = reader.readLine()) != null) {
//                lineNumber++;
//                Matcher matcher = patternForNode.matcher(line);
//
//                if (matcher.find()) {
//                    Id = Integer.parseInt(matcher.group(1));
//                    X = Double.parseDouble(matcher.group(2));
//                    Y = Double.parseDouble(matcher.group(4));
//                    NodeList.add(new Graph.Node(Id, X, Y));
//
//                } else {
//                    throw new IOException("Error while loading line nubmer" + lineNumber);
//                }
//
//            }
//        }
//    }
//    public void readFileNodeBinn(File fileEdges, List<Main.Edge> EdgeList) throws Exception {
//
//    }
}
