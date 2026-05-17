import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;
import java.awt.event.*;
import java.io.File;

public class Gui {
    private static final Color BACKGROUND_COLOR = Color.white;
    private static final Color NODES_COLOR = Color.black;
    private static final Color EDGES_COLOR = Color.red;
    private static final int DIAMITER = 8;
    private static final int RADIUS = DIAMITER/2;
    JCheckBox pokazujEtykiety;
    JCheckBox pokazWagi;
   

    private class GraphPanel extends JPanel {
        Cords cords;
        Graph graph;
        private Point lastMousePosition;
        private double zoom = 1.0;
        private double offsetX = 0.0;
        private double offsetY =0.0;

        GraphPanel(Cords cords,  Graph graph ) {
            this.cords = cords;
            this.graph = graph;
            setBackground(Color.white);

            addMouseWheelListener(new MouseWheelListener(){
                @Override
                public void mouseWheelMoved(MouseWheelEvent e){
                    if(e.getWheelRotation() < 0){
                        zoom*=1.1;
                    }else{
                        zoom*=0.9;
                    }
                    repaint();
                }
            });
            MouseAdapter mouseAdapter = new MouseAdapter(){
                @Override
                public void mousePressed(MouseEvent e){
                    lastMousePosition = e.getPoint();
                }
                @Override
                public void mouseDragged(MouseEvent e){
                    int dx = e.getPoint().x - lastMousePosition.x;
                    int dy = e.getPoint().y - lastMousePosition.y;
                    offsetX +=dx;
                    offsetY +=dy;
                    lastMousePosition = e.getPoint();
                    repaint();
                }
                @Override
                public void mouseReleased(MouseEvent e){
                    lastMousePosition = null;
                }
            };
            addMouseListener(mouseAdapter);
            addMouseMotionListener(mouseAdapter);

        }
        private void drawEdges(Graphics2D g2D){
            HashSet<String> edges = new HashSet<>();
            for(int key : graph.adjList.keySet()){
                int keyX = (int) cords.getX(key);
                int keyY = (int) cords.getY(key);
                if(graph.adjList.get(key) == null) continue;

                for(AdjList.adjElement edge : graph.adjList.get(key)){
                    String edgKey = Math.min(key, edge.nodeName) + "-" + Math.max(key,edge.nodeName);
                    if(!edges.contains(edgKey)) {
                        int edgeX = (int) cords.getX(edge.nodeName);
                        int edgeY = (int) cords.getY(edge.nodeName);
                        g2D.drawLine(keyX, keyY, edgeX, edgeY);
                        edges.add(edgKey);

                        if (pokazWagi != null && pokazWagi.isSelected()) {
                        int midX = (keyX + edgeX) / 2;
                        int midY = (keyY + edgeY) / 2;
                        g2D.drawString(String.valueOf(edge.weight), midX, midY - 2);
                        }
                    }
                }
            }

        }
        private void drawNodes(Graphics2D g2D){
            for(int i = 1; i <=  cords.getN(); i++){
                int x =  (int) cords.getX(i);
                int y = (int) cords.getY(i);

                g2D.drawOval(x- RADIUS, y- RADIUS, DIAMITER, DIAMITER);
                if (pokazujEtykiety != null && pokazujEtykiety.isSelected()) {
                g2D.drawString(String.valueOf(i), x + DIAMITER, y - RADIUS);
                }
            }
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g;

            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.translate(getWidth() / 2, getHeight() / 2);
            g2d.translate(offsetX, offsetY);
            g2d.scale(zoom, zoom);

            g2d.setColor(EDGES_COLOR);
            drawEdges(g2d);

            g2d.setColor(NODES_COLOR);
            drawNodes(g2d);
        }

    }

    JFrame frame;
    GraphPanel graphPanel;

    public Gui(Cords cords, Graph graph, Config config) {
        frame = new JFrame("Graph View");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1100, 750);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        
        JMenuBar menuBar = new JMenuBar();
        JMenu menuPlik = new JMenu("Plik");

        JMenuItem otworzTekstowy = new JMenuItem("Wczytaj tekstowy (.txt)");
        JMenuItem otworzBinarny = new JMenuItem("Wczytaj binarny (.bin)");
        JMenuItem zapiszWynik = new JMenuItem("Zapisz współrzędne");

        menuPlik.add(otworzTekstowy);
        menuPlik.add(otworzBinarny);
        menuPlik.add(zapiszWynik);
        menuBar.add(menuPlik);

        frame.setJMenuBar(menuBar);

        graphPanel = new GraphPanel(cords, graph);
        frame.add(graphPanel, BorderLayout.CENTER);
        

        JPanel toolPanel = new JPanel();
        toolPanel.setLayout(new BoxLayout(toolPanel, BoxLayout.Y_AXIS));
        toolPanel.setBorder(BorderFactory.createTitledBorder("Panel Narzędziowy"));
        toolPanel.setPreferredSize(new Dimension(250, 750));
        String[] algorytmy = {"Tutte's Algorithm", "Fruchterman-Reingold", "Spectral Algorithm"};

        JComboBox<String> algoBox = new JComboBox<>(algorytmy);
        toolPanel.add(algoBox);
        
        toolPanel.add(Box.createVerticalStrut(10));

        pokazujEtykiety = new JCheckBox("Pokaż numery węzłów");
        pokazujEtykiety.setSelected(false);
        pokazujEtykiety.setAlignmentX(Component.LEFT_ALIGNMENT);
        toolPanel.add(pokazujEtykiety);

        pokazWagi = new JCheckBox("Pokaż wagi krawędzi");
        pokazWagi.setSelected(false);
        pokazWagi.setAlignmentX(Component.LEFT_ALIGNMENT);
        toolPanel.add(pokazWagi);

        toolPanel.add(Box.createVerticalStrut(10));

        JButton runButton = new JButton("Uruchom");
        toolPanel.add(runButton);
        toolPanel.add(Box.createVerticalStrut(20));
        
        pokazujEtykiety.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                graphPanel.repaint();
            }
        });

        pokazWagi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                graphPanel.repaint();
            }
        });

        runButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                String wybranyAlgo = (String) algoBox.getSelectedItem();

                if("Tutte's Algorithm".equals(wybranyAlgo)){
                    System.out.println("Odpalam Tutte...");
                    Tutte tutteAlgo = new Tutte();
                    Set<Integer> fixedNodes = new HashSet<>();
                    fixedNodes.add(1);
                    fixedNodes.add(2);
                    fixedNodes.add(3);
                    fixedNodes.add(4);
                    tutteAlgo.execute(graphPanel.cords, graphPanel.graph, fixedNodes);

                }else if("Fruchterman-Reingold".equals(wybranyAlgo)){
                    System.out.println("Odpalam Fruchterman-Reingold...");
                    FuchterMann fuchterAlgo = new FuchterMann(config);
                    fuchterAlgo.executeAlgo(graphPanel.cords, graphPanel.graph);

                }else if("Spectral Algorithm".equals(wybranyAlgo)){
                    System.out.println("Odplama Spectral...");
                    Spectral specAlgo = new Spectral(config.getIterations(), config.getMaxXSize(), config.getMaxYSize());
                    specAlgo.executeAlgo(graphPanel.cords,graphPanel.graph);
                }
                graphPanel.repaint();
            }
        });
  otworzTekstowy.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(".")); 
        
        int result = fileChooser.showOpenDialog(frame);
        
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            System.out.println("Wybrano plik: " + selectedFile.getAbsolutePath());
            
            try {
                Graph nowyGraph = InputOutput.readFileEdge(selectedFile);
                
                graphPanel.graph = nowyGraph;
                
                graphPanel.cords = new Cords(nowyGraph.getNumNodes());
                
                graphPanel.repaint();
                
                JOptionPane.showMessageDialog(frame, "Pomyślnie wczytano graf! Wybierz algorytm i kliknij URUCHOM.", "Sukces", JOptionPane.INFORMATION_MESSAGE);
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Błąd podczas wczytywania pliku:\n" + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
});
    zapiszWynik.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (graphPanel.cords == null || graphPanel.cords.getN() == 0) {
                    JOptionPane.showMessageDialog(frame, "Brak współrzędnych do zapisania!", "Błąd", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File("."));
                fileChooser.setSelectedFile(new File("wynik_cords.txt"));

                int result = fileChooser.showSaveDialog(frame);

                if (result == JFileChooser.APPROVE_OPTION) {
                    File targetFile = fileChooser.getSelectedFile();
                    try (java.io.PrintWriter writer = new java.io.PrintWriter(targetFile)) {
                        int n = graphPanel.cords.getN();
                        writer.println(n);
                        for (int i = 1; i <= n; i++) {
                            writer.println(i + " " + graphPanel.cords.getX(i) + " " + graphPanel.cords.getY(i));
                        }
                        JOptionPane.showMessageDialog(frame, "Współrzędne zostały pomyślnie zapisane!", "Sukces", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(frame, "Błąd podczas zapisu pliku:\n" + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                }
            }
        });
        
        frame.add(toolPanel, BorderLayout.EAST);
        frame.setVisible(true);
    }


}
