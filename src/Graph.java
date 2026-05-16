import java.util.*;

public class Graph {
    /* tutaj są takie funkcje jak alokacja pamięci

    * sprawdzanie czy wierzchołki są połąćzone
    * dodawanie krawędzi
    *
    *
    *
    *  */

    // HashMapa to jest mapa nazwa wierchołka oraz jego linkedLista jets to: nazwa wierchołka, waga -> następny dla tego wierchołka

    private int numNodes;
    HashMap<Integer,LinkedList<AdjList.adjElement>> adjList;
    public Graph(){
        adjList = new HashMap<>();
    }
    public void setNumNodes(int numNodes){
        this.numNodes = numNodes;
    }
    public int getNumNodes(){
        return numNodes;
    }
    public void AddElementLL(int primeNode, int secondaryNode, double weight){

        this.adjList.computeIfAbsent(primeNode, k ->new LinkedList<>()).add(new AdjList.adjElement(secondaryNode, weight));

    }
    public LinkedList<AdjList.adjElement> getLinkedList(int primeNode){
        return this.adjList.get(primeNode);
    }




}
