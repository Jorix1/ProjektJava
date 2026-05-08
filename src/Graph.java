import java.util.*;

public class Graph {
    /* tutaj są takie funkcje jak alokacja pamięci

    * sprawdzanie czy wierzchołki są połąćzone
    * dodawanie krawędzi
    *
    *
    *
    *  */
    // AdjAll lista wszystkich nodów oraz ich list sąsiedctwa
    // HashMapa to jest mapa nazwa wierchołka oraz jego linkedLista jets to: nazwa wierchołka, waga -> następny dla tego wierchołka
    public static class AdjListAll{
        int numNodes;
        HashMap<Integer,LinkedList<AdjList.adjElement>> adjList;
        public AdjListAll(){
            adjList = new HashMap<>();
        }
        public void setNumNodes(int numNodes){
            this.numNodes = numNodes;
        }
        public void AddElementLL(int primeNode, int secondaryNode, double weight){

            this.adjList.computeIfAbsent(primeNode, k ->new LinkedList<>()).add(new AdjList.adjElement(secondaryNode, weight));

        }
    }

}
