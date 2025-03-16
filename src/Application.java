import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Application {
    private JFrame mainFrame;
    private JPanel controlPanel;
    private JComboBox<String> dropDownMenu;
    private OperateCamera operateCamera;
    private JProgressBar progressBar;
    public Application() {
        prepareUI();
    }
    public static void run() {
        Application appUI = new Application();
        appUI.showLayout();
    }

    private void prepareUI() {
        mainFrame = new JFrame("Tractor");
        mainFrame.setSize(700, 700);
        controlPanel = new JPanel();
        controlPanel.setLayout(new BorderLayout());

        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.add(controlPanel, BorderLayout.CENTER);
        mainFrame.setResizable(false);

        mainFrame.setVisible(true);
    }

    private void showLayout() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 0, 70));
        JButton showCamerasBtn = new JButton("Show Cameras");

        showCamerasBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (showCamerasBtn.getText()) {
                    case "Show Cameras":
                        operateCamera = new OperateCamera();
                        operateCamera.startCameras();
                        showCamerasBtn.setText("Hide Cameras");
                        break;
                    case "Hide Cameras":
                        operateCamera.stopCameras();
                        showCamerasBtn.setText("Show Cameras");
                        break;
                }
            }
        });

        JButton countValueBtn = new JButton("Count Values");
        Dimension buttonSize = new Dimension(200, 50);
        showCamerasBtn.setPreferredSize(buttonSize);
        countValueBtn.setPreferredSize(buttonSize);

        buttonPanel.add(showCamerasBtn);
        buttonPanel.add(countValueBtn);

        buttonPanel.setPreferredSize(new Dimension(250, 270));

        String[] items = Counter.grapes.keySet().toArray(new String[0]);
        dropDownMenu = new JComboBox<>(items);
        dropDownMenu.setPreferredSize(new Dimension(250, 50));
        dropDownMenu.setVisible(false);

        dropDownMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedItem = (String) dropDownMenu.getSelectedItem();
                Counter.typeOfGrape = selectedItem;
            }
        });

        progressBar = new JProgressBar(0, 100);
        progressBar.setPreferredSize(new Dimension(250, 20));
        progressBar.setStringPainted(true);
        progressBar.setVisible(false);


        countValueBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dropDownMenu.setVisible(true);
                progressBar.setVisible(true);
                simulateProgress();
                mainFrame.revalidate();
                mainFrame.repaint();
            }
        });

        JPanel wrapperPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 0.5;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;

        wrapperPanel.add(buttonPanel, gbc);

        gbc.gridy = 1;
        gbc.weighty = 0.3;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = new Insets(-100, 0, 0, 0);
        wrapperPanel.add(dropDownMenu, gbc);

        gbc.gridy = 2;
        gbc.weighty = 0.2;
        wrapperPanel.add(progressBar, gbc);

        controlPanel.add(wrapperPanel, BorderLayout.CENTER);

        mainFrame.revalidate();
        mainFrame.repaint();
    }

    private void simulateProgress() {
        new Thread(() -> {
            while (Counter.percentOfDoneWork() < 100) {
                if (Counter.typeOfGrape != null) {
                    int progress = Counter.percentOfDoneWork();
                    SwingUtilities.invokeLater(() -> progressBar.setValue(progress));
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignored) {}
            }
            SwingUtilities.invokeLater(() -> progressBar.setValue(100));
        }).start();
    }

}
