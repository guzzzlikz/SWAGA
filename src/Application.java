import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Application {
    private JFrame mainFrame;
    private JPanel controlPanel;

    public Application() {
        prepareUI();
    }

    public static void run() {
        Application appUI = new Application();
        appUI.showLayout();
    }

    private void prepareUI(){
        mainFrame = new JFrame("Tractor");
        mainFrame.setSize(700, 700);
        controlPanel = new JPanel();
        controlPanel.setLayout(new BorderLayout());

        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.add(controlPanel, BorderLayout.CENTER);

        mainFrame.setVisible(true);
    }

    private void showLayout(){

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1, 0, 70));
        JButton showCamerasBtn = new JButton("Show Cameras");

        showCamerasBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        JButton countValueBtn = new JButton("Count Values");
        Dimension buttonSize = new Dimension(200, 50);
        showCamerasBtn.setPreferredSize(buttonSize);
        countValueBtn.setPreferredSize(buttonSize);


        buttonPanel.add(showCamerasBtn);
        buttonPanel.add(countValueBtn);


        buttonPanel.setPreferredSize(new Dimension(250, 170));

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

        mainFrame.revalidate();
        mainFrame.repaint();
    }
}
