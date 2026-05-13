import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class FuchterMann {

    private  double hooksConst = -0.4;
    private  double coulombaConst = 20;
    private  double temp = 5;

    private final int iterations;
    private final int maxXSize;
    private final int maxYSize;



    public FuchterMann(Config config){
        iterations = config.getIterations();
        maxXSize = config.getMaxXSize();
        maxYSize = config.getMaxYSize();
    }

    public double getHooksConst() {
        return hooksConst;
    }
    public double getColumbConst() {
        return coulombaConst;
    }
    public void setCoulombaConst(double coulombaConst) {
        this.coulombaConst = coulombaConst;
    }
    public void setHooksConst(double hooksConst) {
        this.hooksConst = hooksConst;
    }




    private double getNodesDistanceX(Cords cords, int NodeID1, int NodeID2){
        if(NodeID1 == NodeID2) return 0;
        double distance = 0;

        distance = cords.getX(NodeID1) - cords.getX(NodeID2);


        return distance;
    }

    private double getNodesDistanceY(Cords cords, int NodeID1, int NodeID2){
        if(NodeID1 == NodeID2) return 0;
        double distance = 0;

        distance = cords.getY(NodeID1) - cords.getY(NodeID2);


        return distance;
    }

    private double getNodesDistanceXYSq(double distanceX, double distanceY){
        double distance = 0;

        distance = distanceX * distanceX + distanceY * distanceY;

        return distance;
    }


    private double getAttractionForceX(double distanceX){

        return hooksConst * distanceX;
    }

    private double getAttractionForceY(double distanceY){

        return hooksConst * distanceY;
    }



    private double getRepulsionForceX(double distanceX, double distanceXYSq){
        double repulsionForce = 0;

        if(distanceXYSq > 0.0001) {
            repulsionForce = (getColumbConst() *distanceX) / distanceXYSq;
            return repulsionForce;

        }else return 0;
    }

    private double getRepulsionForceY(double distanceY, double distanceXYSq){
        double repulsionForce = 0;

        if(distanceXYSq > 0.0001) {
            repulsionForce = (getColumbConst() *distanceY) / distanceXYSq;
            return repulsionForce;

        }else return 0;
    }
    public void executeAlgo(Cords cords, Graph.AdjListAll graph){
        int numNodes =  graph.getNumNodes();
        for(int i = 0; i< iterations; i++){

            HashMap<Integer, Double[]> ForceList = new HashMap<>();
            for(int node1 = 0; node1 < numNodes; node1++){
                double FX = 0;
                double FY = 0;

                for(int node2 = 0; node2 < numNodes; node2++) {// częśc odpychanie
                    if(node1 == node2) continue;

                    double distnceX =  getNodesDistanceX(cords, node1, node2);
                    double distnceY =  getNodesDistanceY(cords, node1, node2);
                    double distanceXYSq = getNodesDistanceXYSq(distnceX, distnceY);

                    double repForceX = getRepulsionForceX(distnceX, distanceXYSq);
                    double repForceY = getRepulsionForceY(distnceY, distanceXYSq);

                    FX += repForceX;
                    FY += repForceY;

                }

                LinkedList<AdjList.adjElement> adjList = graph.getLinkedList(node1);
                for(AdjList.adjElement node2 : adjList){ // częśc przyciąganiem
                    if(node1 == node2.nodeName) continue;

                    double distnceX =  getNodesDistanceX(cords, node1, node2.nodeName);
                    double distnceY =  getNodesDistanceY(cords, node1, node2.nodeName);


                    double attrForceX = getAttractionForceX(distnceX);
                    double attrForceY = getAttractionForceY(distnceY);

                    FX += attrForceX;
                    FY += attrForceY;

                }
                FX *= temp;
                FY *= temp;

                ForceList.put(node1, new Double[]{FX,FY});

            }
            for(int node1 : ForceList.keySet()){

                double newNode1CordsX = cords.getX(node1) + ForceList.get(node1)[0];
                double newNode1CordsY = cords.getY(node1) + ForceList.get(node1)[1];

                if(Math.abs(newNode1CordsX)> maxXSize){
                    newNode1CordsX = newNode1CordsX > 0 ? maxXSize : -maxXSize;
                }
                if(Math.abs(newNode1CordsY)> maxYSize){
                    newNode1CordsY = newNode1CordsY > 0 ? maxYSize : -maxYSize;
                }
                cords.set(node1, newNode1CordsX, newNode1CordsY);

            }
            temp *= 0.95;
        }

    }
}
