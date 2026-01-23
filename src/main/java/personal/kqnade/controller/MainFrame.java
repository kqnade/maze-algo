package personal.kqnade.controller;

import personal.kqnade.model.*;
import personal.kqnade.view.*;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {
    private Maze maze;
    private MazeGenerator generator;
    private List<Searcher> searchers;
    private ComparisonPanel comparisonPanel;
    private ControlPanel controlPanel;
    private Timer timer;
    private int timerDelay = 100;

    public MainFrame() {
        setTitle("Maze Algorithm Comparison");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Recommended size from spec: 2000x600
        setSize(2000, 600);
        setLayout(new BorderLayout());

        generator = new MazeGenerator();
        searchers = new ArrayList<>();
        comparisonPanel = new ComparisonPanel();
        controlPanel = new ControlPanel();

        add(comparisonPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        // Initialize Timer
        timer = new Timer(timerDelay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onTimerTick();
            }
        });

        // Event Listeners
        controlPanel.getGenerateButton().addActionListener(e -> generateNewMaze());
        controlPanel.getStartButton().addActionListener(e -> startSearch());
        controlPanel.getPauseButton().addActionListener(e -> pauseSearch());
        controlPanel.getResetButton().addActionListener(e -> resetSearch());
        controlPanel.getSpeedSlider().addChangeListener(e -> updateSpeed(controlPanel.getSpeedSlider().getValue()));

        // Initial Generation
        generateNewMaze();
    }

    private void generateNewMaze() {
        timer.stop();
        maze = generator.generate(25, 25);
        
        // Clear previous panels
        comparisonPanel.removeAll();
        searchers.clear();
        
        // Create Searchers
        searchers.add(new DFSSearcher(maze));
        searchers.add(new BFSSearcher(maze));
        searchers.add(new AStarSearcher(maze));
        searchers.add(new DijkstraSearcher(maze));
        searchers.add(new GreedySearcher(maze));

        // Create Panels
        for (Searcher s : searchers) {
            MazePanel panel = new MazePanel(s.getStatistics().getAlgorithmName());
            panel.setMaze(maze);
            panel.setSearcher(s);
            // Adjust cell size to fit 5 panels in 2000px width
            // 2000 / 5 = 400px per panel. 25 cells * 15px = 375px.
            panel.setCellSize(15);
            comparisonPanel.addMazePanel(panel);
        }

        comparisonPanel.revalidate();
        comparisonPanel.repaint();
        controlPanel.setStatusText("Maze Generated");
    }

    private void startSearch() {
        if (!timer.isRunning()) {
            timer.start();
            controlPanel.setStatusText("Running...");
        }
    }

    private void pauseSearch() {
        if (timer.isRunning()) {
            timer.stop();
            controlPanel.setStatusText("Paused");
        }
    }

    private void resetSearch() {
        timer.stop();
        for (Searcher s : searchers) {
            s.initialize();
        }
        comparisonPanel.updateAll();
        controlPanel.setStatusText("Reset");
    }

    private void updateSpeed(int speed) {
        timerDelay = speed;
        timer.setDelay(timerDelay);
    }

    private void onTimerTick() {
        boolean allCompleted = true;
        for (Searcher s : searchers) {
            if (!s.isCompleted()) {
                s.step();
                allCompleted = false;
            }
        }
        comparisonPanel.updateAll();

        if (allCompleted) {
            timer.stop();
            controlPanel.setStatusText("Completed");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
