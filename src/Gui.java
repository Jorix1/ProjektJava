import javax.swing.*;
import java.awt.*;
import java.util.HashSet;

public class Gui {
    private static final Color BACKGROUND_COLOR = Color.white;
    private static final Color NODES_COLOR = Color.black;
    private static final Color EDGES_COLOR = Color.red;
    private static final int DIAMITER = 8;
    private static final int RADIUS = DIAMITER/2;


    private class GraphPanel extends JPanel {
        Cords cords;
        Graph graph;

        GraphPanel(Cords cords,  Graph graph ) {
            this.cords = cords;
            this.graph = graph;
            setBackground(Color.white);

        }
        private void drawEdges(Graphics2D g2D){
            HashSet<int[]> edges = new HashSet<>();
            for(int key : graph.adjList.keySet()){
                int keyX = (int) cords.getX(key);
                int keyY = (int) cords.getY(key);

                for(AdjList.adjElement edge : graph.adjList.get(key)){
                    int[] edg = {key, edge.nodeName};
                    if(!edges.contains(edg)) {
                        int edgeX = (int) cords.getX(edge.nodeName);
                        int edgeY = (int) cords.getY(edge.nodeName);
                        g2D.drawLine(keyX, keyY, edgeX, edgeY);
                        edges.add(edg);
                    }
                }
            }

        }
        private void drawNodes(Graphics2D g2D){
            for(int i = 1; i <=  cords.getN(); i++){
                int x =  (int) cords.getX(i);
                int y = (int) cords.getY(i);

                g2D.drawOval(x- RADIUS, y- RADIUS, DIAMITER, DIAMITER);
            }
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g;

            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.translate(getWidth() / 2, getHeight() / 2);

            g2d.setColor(EDGES_COLOR);
            drawEdges(g2d);

            g2d.setColor(NODES_COLOR);
            drawNodes(g2d);
        }

    }

    JFrame frame;
    GraphPanel graphPanel;
    public Gui(Cords cords, Graph graph) {
        frame = new JFrame("Graph View");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);


        graphPanel = new GraphPanel(cords, graph);
        frame.add(graphPanel, BorderLayout.CENTER);
        frame.setVisible(true);

    }


}
