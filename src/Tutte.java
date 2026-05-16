import java.util.*;

public class Tutte {
    private static final int MAX_ITERATIONS = 1000;
    private static final double TOLERANCE_SQ = 1e-12;

    public void execute(Cords cords, Graph graph, Set<Integer> fixedNodes) {
        int n = graph.getNumNodes();
        setFixedNodesOnCircle(cords, fixedNodes);
        
        for (int i = 1; i <= n; i++) {
            if (!fixedNodes.contains(i)) {
                cords.set(i, 0.0, 0.0);
            }
        }

        for (int iter = 0; iter < MAX_ITERATIONS; iter++) {
            double maxChangeSq = 0;
            
            for (int u = 1; u <= n; u++) {
                if (fixedNodes.contains(u)) continue;

                LinkedList<AdjList.adjElement> neighbours = graph.adjList.get(u);
                if (neighbours == null || neighbours.isEmpty()) continue;

                double sumX = 0;
                double sumY = 0;
                double weightSum = 0;

                for (AdjList.adjElement edge : neighbours) {
                    double w = edge.weight;
                    sumX += w * cords.getX(edge.nodeName);
                    sumY += w * cords.getY(edge.nodeName);
                    weightSum += w;
                }

                if (weightSum > 0) {
                    double oldX = cords.getX(u);
                    double oldY = cords.getY(u);

                    double newX = sumX / weightSum;
                    double newY = sumY / weightSum;

                    double dx = newX - oldX;
                    double dy = newY - oldY;
                    double changeSq = dx * dx + dy * dy;

                    if (changeSq > maxChangeSq) {
                        maxChangeSq = changeSq;
                    }

                    cords.set(u, newX, newY);
                }
            } // Koniec pętli for (u = 1; u <= n)

            
            if (maxChangeSq < TOLERANCE_SQ) {
                break;
            }
        } // Koniec pętli for (iter)
    }
    private void setFixedNodesOnCircle(Cords cords, Set<Integer> fixed){
        double radius =300;
        int m = fixed.size();
        if(m==0) return;

        double angleStep = 2.0 * Math.PI/m;
        int i =0;
        for(Integer node : fixed){
            double angle = i * angleStep;
            cords.set(node, radius * Math.cos(angle), radius * Math.sin(angle));
            i++;
        }

    }
}