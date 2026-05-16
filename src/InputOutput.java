import java.io.*;
import java.security.AllPermission;
import java.util.*;
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
    public static Graph readFileEdge(File file) throws IOException, IllegalArgumentException {

        try(BufferedReader reader = new BufferedReader(new FileReader(file))){
            Graph AllLinkedList = new Graph();
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
            AllLinkedList.setNumNodes(AllLinkedList.adjList.size());
            return AllLinkedList;
        }


    }

    public  void readFileNodeTxt (File fileNodeTxt, Cords cords) throws IOException, IllegalArgumentException {
        try(BufferedReader reader = new BufferedReader(new FileReader(fileNodeTxt))) {

            int Id;
            double X;
            double Y;

            String line;


            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                Matcher matcher = patternForNode.matcher(line);

                if (matcher.find()) {
                    Id = Integer.parseInt(matcher.group(1));
                    X = Double.parseDouble(matcher.group(2));
                    Y = Double.parseDouble(matcher.group(4));
                    cords.set(Id, X, Y);


                } else {
                    throw new IOException("Error while loading line nubmer" + lineNumber);
                }
                System.out.printf("Poprawnie odczytano: "+lineNumber+" linii");

            }

        }
    }
    public void readFileNodeBinn(File file, Cords cords) throws Exception {
        try(DataInputStream reader = new DataInputStream(new BufferedInputStream(new FileInputStream(file)))){

            int lineNumber = 0;

            int Id;
            double X;
            double Y;

            while (reader.available() > 0) {
                lineNumber++;

                Id = reader.readInt();
                X = reader.readDouble();
                Y = reader.readDouble();
                cords.set(Id, X, Y);

            }
            System.out.printf("Poprawnie odczytano: "+lineNumber+" linii");
        }catch(FileNotFoundException e){
            System.err.println("File not found");
        }
    }

    public void writeCordsTxt(Cords cords, File file) throws IOException{
        try(FileWriter writer = new FileWriter(file)){
            for(int i = 0; i < cords.getN(); i++){
                writer.write(i +", "+cords.getX(i)+", "+cords.getY(i)+"\n");
            }
            System.out.print("Poprawnie zapisano dane do pliku .txt");

        }catch(IOException e){
            System.err.println("Error writing output file");
        }

    }
    public void wiriteCordsBinary(Cords cords, File file) throws IOException{
        final int NumberOfCords =  cords.getN();


        try(DataOutputStream writer = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)))){

            for(int i = 0; i < NumberOfCords; i++){
                writer.writeInt(i);
                writer.writeDouble(cords.getX(i));
                writer.writeDouble(cords.getY(i));
            }

        }catch(IOException e){
            System.err.println("Error writing output file in binary");

        }
    }
}
