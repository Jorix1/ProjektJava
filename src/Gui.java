import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class Gui {
    private static final Color BACKGROUND_COLOR = Color.white;
    private static final Color NODES_COLOR = Color.black;
    private static final Color EDGES_COLOR = Color.red;
    private static final int DIAMITER = 8;
    private static final int RADIUS = DIAMITER / 2;
    JCheckBox pokazujEtykiety;
    JCheckBox pokazWagi;
    JFrame frame;
    GraphPanel graphPanel;
    private double MULTIPLICTION = 1;

    public Gui(Cords cords, Graph graph, Config config) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame = new JFrame("Graph View");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(screenSize.width, screenSize.height);
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);


        JMenuBar menuBar = new JMenuBar();
        JMenu menuPlik = new JMenu("Plik");

        JMenuItem otworzTekstowy = new JMenuItem("Wczytaj plik z krawędziami (.txt)");
        JMenuItem otworzNodeTxt = new JMenuItem("Wczytaj plik z wierzchołkami (.txt)");
        JMenuItem otworzNodeBin = new JMenuItem("Wczytaj plik z wierzchołkami (.bin)");
        JMenuItem zapiszWynikTxt = new JMenuItem("Zapisz współrzędne w formacie .txt");
        JMenuItem zapiszWynikBin = new JMenuItem("Zapisz współrzędne w formacie .bin");

        menuPlik.add(otworzTekstowy);
        menuPlik.add(otworzNodeTxt);
        menuPlik.add(otworzNodeBin);
        menuPlik.add(zapiszWynikTxt);
        menuPlik.add(zapiszWynikBin);

        menuBar.add(menuPlik);

        JMenu menuConfig = new JMenu("Config");
        JMenuItem liczbaIteracji = new JMenuItem("Liczba iteracji: " + config.getIterations());
        JMenuItem maxBordX = new JMenuItem("maskymalna szerokość planszy: " + config.getMaxXSize());
        JMenuItem maxBordY = new JMenuItem("maskymalna wysokość planszy: " + config.getMaxYSize());
        JMenuItem multiplikator = new JMenuItem("Mnożnik współrzędnych: " + MULTIPLICTION);

        menuConfig.add(liczbaIteracji);
        menuConfig.add(maxBordX);
        menuConfig.add(maxBordY);
        menuConfig.add(multiplikator);

        menuBar.add(menuConfig);

        JMenu menuZmienne = new JMenu("Zmienne");
        JMenuItem odpychanieFuch = new JMenuItem();
        JMenuItem przyciaganieFuch = new JMenuItem();

        menuZmienne.add(odpychanieFuch);
        menuZmienne.add(przyciaganieFuch);

        menuBar.add(menuZmienne);
        menuZmienne.setVisible(false);


        frame.setJMenuBar(menuBar);

        graphPanel = new GraphPanel(cords, graph);
        frame.add(graphPanel, BorderLayout.CENTER);


        JPanel toolPanel = new JPanel();
        toolPanel.setLayout(new BoxLayout(toolPanel, BoxLayout.Y_AXIS));
        toolPanel.setBorder(BorderFactory.createTitledBorder("Panel Narzędziowy"));
        toolPanel.setPreferredSize(new Dimension(screenSize.width / 5, screenSize.height / -100));
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

        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String wybranyAlgo = (String) algoBox.getSelectedItem();

                if ("Tutte's Algorithm".equals(wybranyAlgo)) {
                    System.out.println("Odpalam Tutte...");
                    Tutte tutteAlgo = new Tutte();
                    Set<Integer> fixedNodes = new HashSet<>();
                    fixedNodes.add(1);
                    fixedNodes.add(2);
                    fixedNodes.add(3);
                    fixedNodes.add(4);
                    tutteAlgo.execute(graphPanel.cords, graphPanel.graph, fixedNodes);

                } else if ("Fruchterman-Reingold".equals(wybranyAlgo)) {
                    System.out.println("Odpalam Fruchterman-Reingold...");
                    FuchterMann fuchterAlgo = new FuchterMann(config);


                    fuchterAlgo.executeAlgo(graphPanel.cords, graphPanel.graph);

                } else if ("Spectral Algorithm".equals(wybranyAlgo)) {
                    System.out.println("Odplama Spectral...");
                    Spectral specAlgo = new Spectral(config.getIterations(), config.getMaxXSize(), config.getMaxYSize());
                    specAlgo.executeAlgo(graphPanel.cords, graphPanel.graph);

                }
                graphPanel.repaint();
            }
        });

        algoBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String wybranyAlgo = (String) algoBox.getSelectedItem();

                if ("Fruchterman-Reingold".equals(wybranyAlgo)) {
                    odpychanieFuch.setText("Stała odpychania to: " + config.getCoulombaConst());
                    przyciaganieFuch.setText("Stała przyciągania to: " + config.getHooksConst());
                    menuZmienne.setVisible(true);
                } else {
                    menuZmienne.setVisible(false);
                }

                toolPanel.revalidate();
                toolPanel.repaint();
            }
        });

        // wczytywanie oraz zapisywanie
        otworzTekstowy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File(".\\src\\DaneIn"));

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
        otworzNodeTxt.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    JOptionPane.showMessageDialog(frame, "Podaj plik z krawędziami", "Wczytaj", JOptionPane.INFORMATION_MESSAGE);

                    File selectedFile = choseFile();

                    if (selectedFile == null) return;
                    Graph nowyGraph = InputOutput.readFileEdge(selectedFile);
                    graphPanel.graph = nowyGraph;

                    JOptionPane.showMessageDialog(frame, "Podaj plik z wierzchołkami format Txt", "Wczytaj", JOptionPane.INFORMATION_MESSAGE);

                    selectedFile = choseFile();
                    System.out.println(selectedFile);
                    if (selectedFile == null) return;
                    Cords nowyCords = new Cords(nowyGraph.getNumNodes());
                    InputOutput.readFileNodeTxt(selectedFile, nowyCords);
                    graphPanel.cords = nowyCords;


                    JOptionPane.showMessageDialog(frame, "Pomyślnie wczytano graf!", "Sukces", JOptionPane.INFORMATION_MESSAGE);

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Błąd podczas wczytywania pliku:\n" + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
                graphPanel.repaint();


            }
        });
        otworzNodeBin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    JOptionPane.showMessageDialog(frame, "Podaj plik z krawędziami", "Wczytaj", JOptionPane.INFORMATION_MESSAGE);

                    File selectedFile = choseFile();

                    if (selectedFile == null) return;
                    Graph nowyGraph = InputOutput.readFileEdge(selectedFile);
                    graphPanel.graph = nowyGraph;

                    JOptionPane.showMessageDialog(frame, "Podaj plik z wierzchołakmi format Bin", "Wczytaj", JOptionPane.INFORMATION_MESSAGE);

                    selectedFile = choseFile();
                    System.out.println(selectedFile);
                    if (selectedFile == null) return;
                    Cords nowyCords = new Cords(nowyGraph.getNumNodes());
                    InputOutput.readFileNodeBinn(selectedFile, nowyCords);
                    graphPanel.cords = nowyCords;


                    JOptionPane.showMessageDialog(frame, "Pomyślnie wczytano graf!", "Sukces", JOptionPane.INFORMATION_MESSAGE);

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Błąd podczas wczytywania pliku:\n" + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
                graphPanel.repaint();


            }
        });
        zapiszWynikTxt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (graphPanel.cords == null || graphPanel.cords.getN() == 0) {
                    JOptionPane.showMessageDialog(frame, "Brak współrzędnych do zapisania!", "Błąd", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File(".\\src\\DaneOut"));
                fileChooser.setSelectedFile(new File("wynik_cords.txt"));

                int result = fileChooser.showSaveDialog(frame);

                if (result == JFileChooser.APPROVE_OPTION) {
                    File targetFile = fileChooser.getSelectedFile();
                    try {
                        InputOutput.writeCordsTxt(graphPanel.cords, targetFile);
                        JOptionPane.showMessageDialog(frame, "Współrzędne zostały pomyślnie zapisane!", "Sukces", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(frame, "Błąd podczas zapisu pliku:\n" + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Błąd podczas wczytywania pliku:\n Plik nie zatweirdzony do zapisu\n", "Błąd", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        zapiszWynikBin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (graphPanel.cords == null || graphPanel.cords.getN() == 0) {
                    JOptionPane.showMessageDialog(frame, "Brak współrzędnych do zapisania!", "Błąd", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File(".\\src\\DaneOut"));
                fileChooser.setSelectedFile(new File("wynik_cords.txt"));

                int result = fileChooser.showSaveDialog(frame);

                if (result == JFileChooser.APPROVE_OPTION) {
                    File targetFile = fileChooser.getSelectedFile();
                    try {
                        InputOutput.wiriteCordsBinary(graphPanel.cords, targetFile);
                        JOptionPane.showMessageDialog(frame, "Współrzędne zostały pomyślnie zapisane!", "Sukces", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(frame, "Błąd podczas zapisu pliku:\n" + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Błąd podczas wczytywania pliku:\n Plik nie zatweirdzony do zapisu\n", "Błąd", JOptionPane.ERROR_MESSAGE);

                }
            }
        });

        // konfig app l iteracji oraz max rozmiar planszy
        liczbaIteracji.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int numberInterationNew = IpopUpWindow("Podaj liczbę itearcji algorytmu");
                if (numberInterationNew == 0)
                    JOptionPane.showMessageDialog(frame, "Podano złą liczbę iteracji liczba pozostaje taka sama ", "Info", JOptionPane.INFORMATION_MESSAGE);

                else {
                    config.setIterations(numberInterationNew);
                    liczbaIteracji.setText("Liczba iteracji: " + config.getIterations());
                }
            }
        });
        multiplikator.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double multi = DpopUpWindow("Podaj nową stałą przyciągania");

                if (Math.abs(multi) < 0.001) {
                    JOptionPane.showMessageDialog(frame, "Podana liczba: " + multi + " nie może zostać ustawiona jako stała", "Błąd", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    MULTIPLICTION = multi;
                    multiplikator.setText("Mnożnik współrzędnych: " + MULTIPLICTION);
                }
            }
        });
        maxBordX.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int maxXNew = IpopUpWindow("Podaj maxymalaną szerokość planszy");
                if (maxXNew == 0)
                    JOptionPane.showMessageDialog(frame, "Podano złą szerokość, pozostaje taka sama ", "Info", JOptionPane.INFORMATION_MESSAGE);

                else {
                    config.setMaxXSize(maxXNew);
                    maxBordX.setText("maskymalna szerokość planszy: " + config.getMaxXSize());
                }
            }
        });
        maxBordY.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int maxYNew = IpopUpWindow("Podaj maxymalaną szerokość planszy");
                if (maxYNew == 0)
                    JOptionPane.showMessageDialog(frame, "Podano złą szerokość, pozostaje taka sama ", "Info", JOptionPane.INFORMATION_MESSAGE);

                else {
                    config.setMaxYSize(maxYNew);
                    maxBordY.setText("maskymalna szerokość planszy: " + config.getMaxYSize());
                }
            }
        });

        // stałe fuchtermann
        przyciaganieFuch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double stalaPrzy = DpopUpWindow("Podaj nową stałą przyciągania");

                if (stalaPrzy == 0) {
                    JOptionPane.showMessageDialog(frame, "Podana liczba: " + stalaPrzy + " nie może zostać ustawiona jako stała", "Błąd", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    config.setHooksConst(stalaPrzy); // nie trzeba się gimnastykować już jest sprawdzanie w metodzie
                    przyciaganieFuch.setText("Stała przyciągania to: " + config.getHooksConst());
                }
            }
        });
        odpychanieFuch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double stalaOdp = DpopUpWindow("Podaj nową stałą odpychania");

                if (stalaOdp == 0) {
                    JOptionPane.showMessageDialog(frame, "Podana liczba: " + stalaOdp + " nie może zostać ustawiona jako stała", "Błąd", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    config.setCoulombaConst(stalaOdp); // nie trzeba się gimnastykować już jest sprawdzanie w metodzie
                    odpychanieFuch.setText("Stała odpychania to: " + config.getCoulombaConst());
                }
            }
        });

        frame.add(toolPanel, BorderLayout.EAST);
        frame.setVisible(true);
    }

    private File choseFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("."));
        int resultFileEdge = fileChooser.showOpenDialog(frame);
        if (resultFileEdge == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            return selectedFile;
        } else {
            JOptionPane.showMessageDialog(frame, "Podany plik nie spełnia norm!", "Błąd", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private int IpopUpWindow(String message) {
        String numberBase = JOptionPane.showInputDialog(message);
        int number = 0;
        try {
            number = Integer.parseInt(numberBase.trim());

            if (number <= 0) return 0;
            else return number;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Podano nie prawidłowy number", "Error", JOptionPane.ERROR_MESSAGE);
            return 0;
        }

    }

    private double DpopUpWindow(String message) {
        String numberBase = JOptionPane.showInputDialog(message);
        double number = 0;
        try {
            number = Double.parseDouble(numberBase.trim());
            return number;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Podano nie prawidłowy number", "Error", JOptionPane.ERROR_MESSAGE);
            return 0;
        }

    }

    private class GraphPanel extends JPanel {
        Cords cords;
        Graph graph;
        private Point lastMousePosition;
        private double zoom = 1.0;
        private double offsetX = 0.0;
        private double offsetY = 0.0;
        private int draggedNodeId = -1;

        GraphPanel(Cords cords, Graph graph) {
            this.cords = cords;
            this.graph = graph;
            setBackground(Color.white);

            addMouseWheelListener(new MouseWheelListener() {
                @Override
                public void mouseWheelMoved(MouseWheelEvent e) {
                    double expectedZoom = zoom;
                    double mouseX = e.getX();
                    double mouseY = e.getY();

                    double globalX = (mouseX - offsetX) / zoom;
                    double globalY = (mouseY - offsetY) / zoom;

                    if (e.getWheelRotation() < 0) {
                        expectedZoom *= 1.1;

                    } else {
                        expectedZoom *= 0.9;

                    }
                    expectedZoom = Math.max(0.05, Math.min(expectedZoom, 3.0));

                    if (expectedZoom != zoom) {
                        zoom = expectedZoom;

                        offsetX = mouseX - (globalX * zoom);
                        offsetY = mouseY - (globalY * zoom);
                        repaint();
                    }


                }
            });
            MouseAdapter mouseAdapter = new MouseAdapter() {
               @Override
            public void mousePressed(MouseEvent e) {
                lastMousePosition = e.getPoint();
                draggedNodeId = -1;

                double mouseX = (e.getX() - offsetX) / zoom / MULTIPLICTION;
                double mouseY = (e.getY() - offsetY) / zoom / MULTIPLICTION;

                for (int i = 1; i <= GraphPanel.this.cords.getN(); i++) {
                        double nodeX = GraphPanel.this.cords.getX(i);
                        double nodeY = GraphPanel.this.cords.getY(i);
                        double distance = Math.sqrt(Math.pow(mouseX - nodeX, 2) + Math.pow(mouseY - nodeY, 2));
                        double hitRadius = (RADIUS + 10) / zoom / MULTIPLICTION;

                    if (distance <= hitRadius) {
                         draggedNodeId = i;
                            break;
        }
    }
}

            @Override
            public void mouseDragged(MouseEvent e) {
                if (lastMousePosition == null) return;

                int dx = e.getX() - lastMousePosition.x;
                int dy = e.getY() - lastMousePosition.y;

                if (draggedNodeId != -1) {
                double changeX = dx / zoom / MULTIPLICTION;
                double changeY = dy / zoom / MULTIPLICTION;

                GraphPanel.this.cords.setX(draggedNodeId, GraphPanel.this.cords.getX(draggedNodeId) + changeX);
                GraphPanel.this.cords.setY(draggedNodeId, GraphPanel.this.cords.getY(draggedNodeId) + changeY);
                } else {
                    offsetX += dx;
                    offsetY += dy;
                        }

                lastMousePosition = e.getPoint();
                repaint();
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    lastMousePosition = null;
                    draggedNodeId = -1;
                }
            };
            addMouseListener(mouseAdapter);
            addMouseMotionListener(mouseAdapter);

        }

        private void drawEdges(Graphics2D g2D) {
            HashSet<String> edges = new HashSet<>();
            for (int key : graph.adjList.keySet()) {
                int keyX = (int) (cords.getX(key) * MULTIPLICTION);
                int keyY = (int) (cords.getY(key) * MULTIPLICTION);
                if (graph.adjList.get(key) == null) continue;

                for (AdjList.adjElement edge : graph.adjList.get(key)) {
                    String edgKey = Math.min(key, edge.nodeName) + "-" + Math.max(key, edge.nodeName);
                    if (!edges.contains(edgKey)) {
                        int edgeX = (int) (cords.getX(edge.nodeName) * MULTIPLICTION);
                        int edgeY = (int) (cords.getY(edge.nodeName) * MULTIPLICTION);
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

        private void drawNodes(Graphics2D g2D) {
            for (int i = 1; i <= cords.getN(); i++) {
                int x = (int) (cords.getX(i) * MULTIPLICTION);
                int y = (int) (cords.getY(i) * MULTIPLICTION);

                g2D.drawOval(x - RADIUS, y - RADIUS, DIAMITER, DIAMITER);
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

            g2d.translate(offsetX, offsetY);
            g2d.scale(zoom, zoom);

            g2d.setColor(EDGES_COLOR);
            drawEdges(g2d);

            g2d.setColor(NODES_COLOR);
            drawNodes(g2d);
        }

    }


}
