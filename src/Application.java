import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class Application {
    private JFrame mainFrame;
    private JPanel controlPanel;
    private OperateCamera operateCamera;
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
        JButton showCamerasBtn = new JButton("Show Cameras");
        showCamerasBtn.setIcon(new ImageIcon("textures\\button1Show.png"));

        showCamerasBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (showCamerasBtn.getText()){
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

        JButton countValueBtn = new JButton("Count Values");
        Dimension buttonSize = new Dimension(200, 50);
        showCamerasBtn.setPreferredSize(buttonSize);
        countValueBtn.setPreferredSize(buttonSize);

        buttonPanel.add(showCamerasBtn);
        buttonPanel.add(countValueBtn);

        buttonPanel.setPreferredSize(new Dimension(250, 270));

        JPanel wrapperPanel = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;

        wrapperPanel.add(buttonPanel, gbc);

        controlPanel.add(wrapperPanel, BorderLayout.CENTER);
        backgroundPanel.add(buttonPanel);
        mainFrame.setContentPane(backgroundPanel);
        mainFrame.revalidate();
        mainFrame.repaint();
    }
}