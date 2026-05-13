import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Spectral {

    private final long seed1 = 42L; // seed jest przypisany by algorytm był deterministyczny czyli dla tych samych danych
    private final long seed2 = 123L;// zawsze zwracał te same wsp


    private int iterations;
    private int maxXSize;
    private int maxYSize;

    public Spectral(int iterations, int maxXSize, int maxYSize ){
        this.iterations = iterations;
        this.maxXSize = maxXSize;
        this.maxYSize = maxYSize;
    }

    private double[][] buildLaplaceMatrix(Graph.AdjListAll graph, int numNodes){
        double [][] laplaceMatrix =  new double[numNodes][numNodes];

        for(int i = 1; i<=numNodes; i++){
            LinkedList<AdjList.adjElement> adjList =  graph.getLinkedList(i);
            if(!adjList.isEmpty()){
                for(AdjList.adjElement e: adjList){
                    int vertex = e.nodeName;
                    double weight = e.weight;

                    laplaceMatrix[i][i] += weight;
                    laplaceMatrix[vertex][vertex] += weight;
                    laplaceMatrix[vertex][i] -= weight;
                    laplaceMatrix[i][vertex] -= weight;

                }
            }
        }
        return laplaceMatrix;
    }
    private double[][] buildShiftMatrix(double [][] laplaceMatrix, int numNodes){

        double maxDiag = 0.0;
        for(int i = 1; i<=numNodes; i++){
            if(laplaceMatrix[i][i] > maxDiag) maxDiag = laplaceMatrix[i][i];
        }

        double lambdaMax = maxDiag + 1;
        double [][] shiftMatrix =  new double[numNodes][numNodes];

        for(int i = 1; i<=numNodes; i++){
            for(int j = 1; j<=numNodes; j++){
                if(i ==j){
                    shiftMatrix[i][j] = lambdaMax-laplaceMatrix[i][j];
                }else{
                    shiftMatrix[i][j] = -laplaceMatrix[i][j];
                }
            }
        }

        return shiftMatrix;
    }
    private double[] getV0(int numberOfNodes){
        double [] v0 = new double[numberOfNodes];
        double val = 1/ Math.sqrt(numberOfNodes);
        for(int i = 1; i<=numberOfNodes; i++){
            v0[i] = val;
        }
        return v0;
    }

    private double[] powerIteration(double[][] shift, int numNodes, List<double []> deflatationVN, long seed ){
        double[] vN = new double[numNodes];
        Random rand = new Random(seed);

        for(int i = 1; i<=numNodes; i++){
            vN[i] = rand.nextDouble() * 100 +1;
        }

        double tmp[] = new double[numNodes +1];

        for(int i = 0; i< iterations; i++){
            for(double [] defElement:  deflatationVN){
                double dot = 0;
                for(int j = 1; j<=numNodes; j++) dot += vN[j-1]*defElement[j];
                for(int j = 1; j<=numNodes; j++) vN[j] -= dot * defElement[j];
            }

            for(int j = 1; j<=numNodes; j++){
                tmp[j] = 0;
                for(int u = 1; u<=numNodes; u++){
                    tmp[j] += vN[u]*shift[u][j];
                }
            }

            double norm = 0;
            for(int j = 1; j<=numNodes; j++){
                norm += vN[j]*tmp[j];
            }
            norm = Math.sqrt(norm);

            if(norm < 1e-11) break;

            for(int j = 1; j<=numNodes; j++){
                vN[j] = vN[j]/norm;
            }

        }

        return vN;
    }

    private void applyCoordinatesAndNormalize(Cords cords,double[] v1,double[] v2,int numNodes){
        double maxX = v1[0], maxY = v2[0];
        double minX = v1[0], minY = v2[0];

        for(int i = 1; i<=numNodes; i++){
            maxX = Math.min(maxX, v1[i]);
            minX = Math.min(minX, v2[i]);
            maxY = Math.max(maxY, v1[i]);
            minY = Math.min(minY, v2[i]);
        }

        double rangeX = maxX - minX;
        double rangeY = maxY - minY;

        if(rangeX > 1e-11) rangeX = 1;

        if(rangeY > 1e-11) rangeY = 1;

        for(int i = 1; i<=numNodes; i++){
            double normalizedX = ((v1[i] - minX)/rangeX - 0.5) * (2.0 * maxXSize);
            double normalizedY = ((v1[i] - minY)/rangeY - 0.5) * (2.0 * maxYSize);
            cords.set(i, normalizedX, normalizedY);
        }

    }

    public void executeAlgo(Cords cords, Graph.AdjListAll graph){
        int numberOfNodes = graph.getNumNodes();

        if(numberOfNodes <= 1) return ;

        double [][] laplace = buildLaplaceMatrix(graph, numberOfNodes);
        double [][] shift = buildShiftMatrix(laplace, numberOfNodes);

        double [] v0 = getV0(numberOfNodes);

        List<double []> deflatationV1 = new ArrayList<>();
        deflatationV1.add(v0);
        double [] v1 = powerIteration(shift, numberOfNodes, deflatationV1 ,seed1); // 42L

        List< double[]> deflatationV2 = new ArrayList<>();
        deflatationV2.add(v0);
        deflatationV2.add(v1);
        double [] v2 = powerIteration(shift, numberOfNodes, deflatationV2, seed2);

        applyCoordinatesAndNormalize(cords, v1, v2, numberOfNodes);

    }
}
