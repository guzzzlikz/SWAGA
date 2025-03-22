import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
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
    private static JComboBox<String> dropDownMenu;
    private OperateCamera operateCamera;
    private JProgressBar progressBar;
    private JPanel backgroundPanel;
    private static JTextField areaField;

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
        backgroundPanel = new JPanel() {
            private Image backgroundImage;
            {
                try {
                    backgroundImage = ImageIO.read(new File("textures" + File.separator + "background.jpg"));
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
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        JLabel showCamerasBtn = new JLabel("Show Cameras");
        showCamerasBtn.setIcon(new ImageIcon("textures" + File.separator + "button1Show.png"));
        showCamerasBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        showCamerasBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                switch (showCamerasBtn.getText()) {
                    case "Show Cameras":
                        showCamerasBtn.setIcon(new ImageIcon("textures" + File.separator + "button1ShowLock.png"));
                        break;
                    case "Hide Cameras":
                        showCamerasBtn.setIcon(new ImageIcon("textures" + File.separator + "button1HideLock.png"));
                        break;
                }
            }
            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                switch (showCamerasBtn.getText()) {
                    case "Show Cameras":
                        showCamerasBtn.setIcon(new ImageIcon("textures" + File.separator + "button1Show.png"));
                        break;
                    case "Hide Cameras":
                        showCamerasBtn.setIcon(new ImageIcon("textures" + File.separator + "button1Hide.png"));
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
                        showCamerasBtn.setIcon(new ImageIcon("textures" + File.separator + "button1Hide.png"));
                        break;
                    case "Hide Cameras":
                        operateCamera.stopCameras();
                        showCamerasBtn.setText("Show Cameras");
                        showCamerasBtn.setIcon(new ImageIcon("textures" + File.separator + "button1Show.png"));
                        break;
                }
            }
        });

        JLabel countValueBtn = new JLabel("Count Values");
        countValueBtn.setName("Count Values");
        countValueBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        countValueBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dropDownMenu.setVisible(true);
                progressBar.setVisible(true);
                areaField.setVisible(true);
                simulateProgress();
                mainFrame.revalidate();
                mainFrame.repaint();
                countValueBtn.setName("Hide Count Values");

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                countValueBtn.setIcon(new ImageIcon("textures" + File.separator + "button2Lock.png"));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                countValueBtn.setIcon(new ImageIcon("textures" + File.separator + "button2.png"));
            }
        });
        countValueBtn.setIcon(new ImageIcon("textures" + File.separator + "button2.png"));

        Dimension buttonSize = new Dimension(320, 50);
        showCamerasBtn.setPreferredSize(buttonSize);
        showCamerasBtn.setMaximumSize(buttonSize);
        countValueBtn.setPreferredSize(buttonSize);
        countValueBtn.setMaximumSize(buttonSize);
        buttonPanel.add(showCamerasBtn);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 70)));
        buttonPanel.add(countValueBtn);
        buttonPanel.setPreferredSize(new Dimension(320, 270));
        String[] items = new String[Counter.grapes.size() + 1];
        items[0] = "нічого не обрано";

        int index = 1;
        for (String s : Counter.grapes.keySet()) {
            items[index] = s;
            index++;
        }
        dropDownMenu = new JComboBox<>(items);
        dropDownMenu.setPreferredSize(new Dimension(250, 50));
        dropDownMenu.setMaximumSize(new Dimension(250, 50));
        dropDownMenu.setAlignmentX(Component.CENTER_ALIGNMENT);
        dropDownMenu.setVisible(false);

        dropDownMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedItem = (String) dropDownMenu.getSelectedItem();
                Counter.typeOfGrape = selectedItem;
                System.out.println(areaField.getText() + "соток");
            }
        });

        progressBar = new JProgressBar(0, 100);
        progressBar.setPreferredSize(new Dimension(250, 20));
        progressBar.setMaximumSize(new Dimension(250, 20));
        progressBar.setAlignmentX(Component.CENTER_ALIGNMENT);
        progressBar.setStringPainted(true);
        progressBar.setVisible(false);

        areaField = new JTextField(10);
        areaField.setVisible(false);
        ((AbstractDocument) areaField.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                    throws BadLocationException {
                String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
                String newText = currentText.substring(0, offset) + text + currentText.substring(offset + length);

                if (newText.isEmpty() || newText.matches("^[1-9]\\d*$")) {
                    super.replace(fb, offset, length, text, attrs);
                } else {
                    Toolkit.getDefaultToolkit().beep();
                }
            }

            @Override
            public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr)
                    throws BadLocationException {
                replace(fb, offset, 0, text, attr);
            }
        });

        JPanel flexContainer = new JPanel();
        flexContainer.setOpaque(false);
        flexContainer.setLayout(new BoxLayout(flexContainer, BoxLayout.Y_AXIS));

        flexContainer.add(buttonPanel);
        flexContainer.add(Box.createRigidArea(new Dimension(0, 30)));
        flexContainer.add(dropDownMenu);
        flexContainer.add(Box.createRigidArea(new Dimension(0, 20)));
        flexContainer.add(progressBar);
        flexContainer.add(Box.createRigidArea(new Dimension(0, 20)));
        flexContainer.add(areaField);

        JPanel centeringPanel = new JPanel(new GridBagLayout());
        centeringPanel.setOpaque(false);
        centeringPanel.add(flexContainer);

        controlPanel.add(centeringPanel, BorderLayout.CENTER);
        mainFrame.setContentPane(backgroundPanel);
        mainFrame.add(controlPanel);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    private void simulateProgress() {
        new Thread(() -> {
            while (true) {
                if (!areaField.getText().isEmpty() && !areaField.hasFocus()) {
                    Counter.setSotka(Double.parseDouble(areaField.getText()));
                    Counter.updateMap();
                    break;
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
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

    public static JComboBox<String> getDropDownMenu() {
        return dropDownMenu;
    }

    public static JTextField getAreaField() {
        return areaField;
    }
}