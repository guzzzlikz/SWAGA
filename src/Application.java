import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class Application {
    private JFrame mainFrame;
    private JPanel controlPanel;
    private JComboBox<String> dropDownMenu;
    private OperateCamera operateCamera;
    private JProgressBar progressBar;
    private JPanel backgroundPanel;

    public Application() {
        prepareUI();
    }

    public static void run() {
        Application appUI = new Application();
        appUI.showLayout();
    }

    private void prepareUI() {
        mainFrame = new JFrame("Tractor");
        backgroundPanel = new JPanel();
        mainFrame.setSize(700, 700);
        controlPanel = new JPanel();
        controlPanel.setOpaque(false);
        controlPanel.setLayout(new BorderLayout());

        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.add(controlPanel, BorderLayout.CENTER);
        mainFrame.setResizable(false);

        mainFrame.setVisible(true);
    }

    private void showLayout() {
        JPanel backgroundPanel = new JPanel() {
            private Image backgroundImage;

            {
                try {
                    backgroundImage = ImageIO.read(new File("textures\\background.jpg"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };

        backgroundPanel.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new GridLayout(3, 1, 0, 70));
        JLabel showCamerasBtn = new JLabel("Show Cameras");
        showCamerasBtn.setIcon(new ImageIcon("textures\\button1Show.png"));

        showCamerasBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                switch (showCamerasBtn.getText()) {
                    case "Show Cameras":
                        showCamerasBtn.setIcon(new ImageIcon("textures\\button1ShowLock.png"));
                        break;
                    case "Hide Cameras":

                        showCamerasBtn.setIcon(new ImageIcon("textures\\button1HideLock.png"));
                        break;
                }
            }
            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseEntered(e);
                switch (showCamerasBtn.getText()) {
                    case "Show Cameras":
                        showCamerasBtn.setIcon(new ImageIcon("textures\\button1Show.png"));
                        break;
                    case "Hide Cameras":

                        showCamerasBtn.setIcon(new ImageIcon("textures\\button1Hide.png"));
                        break;
                }
            }
        });
        showCamerasBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                switch (showCamerasBtn.getText()) {
                    case "Show Cameras":
                        operateCamera = new OperateCamera();
                        operateCamera.startCameras();
                        showCamerasBtn.setText("Hide Cameras");
                        showCamerasBtn.setIcon(new ImageIcon("textures\\button1Hide.png"));
                        break;
                        case "Hide Cameras":
                            operateCamera.stopCameras();
                            showCamerasBtn.setText("Show Cameras");
                            showCamerasBtn.setIcon(new ImageIcon("textures\\button1Show.png"));
                            break;
                }
            }
        });

        JLabel countValueBtn = new JLabel("Count Values");
        countValueBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                dropDownMenu.setVisible(true);
                progressBar.setVisible(true);
                simulateProgress();
                mainFrame.revalidate();
                mainFrame.repaint();
                countValueBtn.setIcon(new ImageIcon("textures\\button2Lock.png"));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                countValueBtn.setIcon(new ImageIcon("textures\\button2.png"));
            }
        });
        countValueBtn.setIcon(new ImageIcon("textures\\button2.png"));
        Dimension buttonSize = new Dimension(320, 50);
        showCamerasBtn.setPreferredSize(buttonSize);
        countValueBtn.setPreferredSize(buttonSize);

        buttonPanel.add(showCamerasBtn);
        buttonPanel.add(countValueBtn);

        buttonPanel.setPreferredSize(new Dimension(320, 270));
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

        JPanel wrapperPanel = new JPanel(new GridBagLayout());
        wrapperPanel.setOpaque(false);
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
        mainFrame.setContentPane(backgroundPanel);
        mainFrame.add(controlPanel);
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
