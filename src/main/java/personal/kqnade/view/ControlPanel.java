package personal.kqnade.view;

import javax.swing.*;
import java.awt.FlowLayout;

public class ControlPanel extends JPanel {
    private JButton generateButton;
    private JButton startButton;
    private JButton pauseButton;
    private JButton resetButton;
    private JSlider speedSlider;
    private JLabel statusLabel;

    public ControlPanel() {
        setLayout(new FlowLayout());

        generateButton = new JButton("Generate Maze");
        startButton = new JButton("Start");
        pauseButton = new JButton("Pause");
        resetButton = new JButton("Reset");
        
        speedSlider = new JSlider(JSlider.HORIZONTAL, 10, 500, 100);
        speedSlider.setInverted(true); // Lower value = faster speed (less delay)
        
        statusLabel = new JLabel("Ready");

        add(generateButton);
        add(startButton);
        add(pauseButton);
        add(resetButton);
        add(new JLabel("Speed:"));
        add(speedSlider);
        add(statusLabel);
    }

    public JButton getGenerateButton() {
        return generateButton;
    }
    public JButton getStartButton() {
        return startButton;
    }
    public JButton getPauseButton() {
        return pauseButton;
    }
    public JButton getResetButton() {
        return resetButton;
    }
    public JSlider getSpeedSlider() {
        return speedSlider;
    }
    
    public void setStatusText(String text) {
        statusLabel.setText(text);
    }
}
