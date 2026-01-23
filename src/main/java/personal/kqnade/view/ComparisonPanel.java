package personal.kqnade.view;

import javax.swing.JPanel;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

public class ComparisonPanel extends JPanel {
    private List<MazePanel> mazePanels;

    public ComparisonPanel() {
        mazePanels = new ArrayList<>();
        setLayout(new GridLayout(1, 5)); // Horizontal layout
    }

    public void addMazePanel(MazePanel panel) {
        mazePanels.add(panel);
        add(panel);
    }

    public void updateAll() {
        for (MazePanel panel : mazePanels) {
            panel.repaint();
        }
    }
}
