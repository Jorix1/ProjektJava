import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

public class FuchterMann {

    private  double hooksConst = -0.1;
    private  double coulombaConst = 1.5;
    private  double tempX = 100;
    private  double tempY = 100;

    private final int iterations;
    private final int maxXSize;
    private final int maxYSize;



    public FuchterMann(Config config){
        iterations = config.getIterations();
        maxXSize = config.getMaxXSize();
        maxYSize = config.getMaxYSize();
        tempX = maxXSize/4;
        tempY = maxYSize/4;
    }

    private void setRandomCords(Cords cords){
        Random random = new Random();
        for(int i = 1; i <= cords.getN(); i++){
            double randomX = random.nextDouble()*maxXSize;
            double randomY = random.nextDouble()*maxYSize;
            cords.set(i,randomX,randomY );
        }
    }


    public double getHooksConst() {
        return hooksConst;
    }
    public double getColumbConst() {
        return coulombaConst;
    }
    public void setCoulombaConst(double coulombaConst) {
        if(coulombaConst == 0){
            System.out.println("coulombaConst can't be  zero and must be positive number");

        } else if (coulombaConst < 0) {
            this.coulombaConst = -(coulombaConst);

        }
        this.coulombaConst = coulombaConst;
    }
    public void setHooksConst(double hooksConst) {
        if(hooksConst == 0) {
            System.err.println("hooksConst cant be equal 0 and it must be negative real number");

        } else if (hooksConst > 0) {
            this.hooksConst = -(hooksConst);
        }
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

    public  void executeAlgo(Cords cords, Graph graph){
        int numNodes =  graph.getNumNodes();
        setRandomCords(cords);
        for(int i = 0; i< iterations; i++){

            HashMap<Integer, Double[]> ForceList = new HashMap<>();
            for(int node1 = 1; node1 <= numNodes; node1++){
                double FX = 0;
                double FY = 0;

                for(int node2 = 1; node2 <= numNodes; node2++) {// częśc odpychanie
                    if(node1 == node2) continue;

                    double distnceX =  getNodesDistanceX(cords, node1, node2);
                    double distnceY =  getNodesDistanceY(cords, node1, node2);
                    double distanceXYSq = getNodesDistanceXYSq(distnceX, distnceY);
                    if(distanceXYSq < 0.0001) {
                        distnceX = Math.random() * 0.1;
                        distnceY = Math.random() * 0.1;
                        distanceXYSq = distnceX * distnceX + distnceY * distnceY;
                    }
                    double repForceX = getRepulsionForceX(distnceX, distanceXYSq);
                    double repForceY = getRepulsionForceY(distnceY, distanceXYSq);

                    FX += repForceX;
                    FY += repForceY;

                }

                LinkedList<AdjList.adjElement> adjList = graph.getLinkedList(node1);

                if (adjList == null || adjList.isEmpty()) continue;
                for(AdjList.adjElement node2 : adjList){ // częśc przyciąganiem

                    if(node1 == node2.nodeName) continue;

                    double distnceX =  getNodesDistanceX(cords, node1, node2.nodeName);
                    double distnceY =  getNodesDistanceY(cords, node1, node2.nodeName);


                    double attrForceX = getAttractionForceX(distnceX);
                    double attrForceY = getAttractionForceY(distnceY);

                    FX += attrForceX;
                    FY += attrForceY;

                }


                ForceList.put(node1, new Double[]{FX,FY});

            }
            for(int node1 : ForceList.keySet()){
                double newNode1CordsX = cords.getX(node1);
                double newNode1CordsY = cords.getY(node1);

                if(Math.abs(ForceList.get(node1)[0]) > tempX){
                    newNode1CordsX += ForceList.get(node1)[0] > 0 ? tempX : -tempX;
                }else {
                    newNode1CordsX += ForceList.get(node1)[0];
                }

                if(Math.abs(ForceList.get(node1)[1]) > tempY){
                    newNode1CordsY += ForceList.get(node1)[1] > 0 ? tempY : -tempY;
                }else {
                    newNode1CordsY += ForceList.get(node1)[1];
                }

                newNode1CordsY = cords.getY(node1) + ForceList.get(node1)[1];
                if(Math.abs(newNode1CordsX)> maxXSize){
                    newNode1CordsX = newNode1CordsX > 0 ? maxXSize : -maxXSize;
                }
                if(Math.abs(newNode1CordsY)> maxYSize){
                    newNode1CordsY = newNode1CordsY > 0 ? maxYSize : -maxYSize;
                }
                cords.set(node1, newNode1CordsX, newNode1CordsY);

            }
            tempX *= 0.95;
            tempY *= 0.95;
        }

    }
}
