import javax.swing.*;
import java.awt.*;
import java.util.concurrent.CountDownLatch;

public class SwagaExceptionHandler {
    public static void showErrorDialog(String e) {
        if (SwingUtilities.isEventDispatchThread()) {
            showModalDialogOnEDT(e);
        } else {
            showModalDialogFromOtherThread(e);
        }
    }

    private static void showModalDialogOnEDT(String e) {
        JDialog dialog = createErrorDialog(e);
        dialog.setModal(true);
        dialog.setVisible(true);
    }

    private static void showModalDialogFromOtherThread(String e) {
        final CountDownLatch latch = new CountDownLatch(1);

        SwingUtilities.invokeLater(() -> {
            JDialog dialog = createErrorDialog(e);

            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                    latch.countDown();
                }
            });

            for (Component comp : dialog.getContentPane().getComponents()) {
                if (comp instanceof JButton) {
                    ((JButton) comp).addActionListener(event -> latch.countDown());
                }
            }

            dialog.setVisible(true);
        });

        try {
            latch.await();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    private static JDialog createErrorDialog(String e) {
        JDialog dialog = new JDialog((Frame) null, "Помилка", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(null);

        JLabel messageLabel;
        JTextArea solutionArea;

        if (e.startsWith("Blocked")) {
            messageLabel = new JLabel("<html><b>Error:</b> " + e + "</html>");
            solutionArea = new JTextArea("Clean your tube!");
        } else {
            messageLabel = new JLabel("<html><b>Error:</b> Unknown error</html>");
            solutionArea = new JTextArea("Come on, you are smart!\nDo it by yourself!");
        }

        messageLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        solutionArea.setEditable(false);
        solutionArea.setBackground(dialog.getBackground());
        solutionArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        dialog.add(messageLabel, BorderLayout.NORTH);
        dialog.add(new JScrollPane(solutionArea), BorderLayout.CENTER);

        JButton closeButton = new JButton("Try Again");
        closeButton.addActionListener(event -> dialog.dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Make dialog close on ESC key
        dialog.getRootPane().registerKeyboardAction(
                e1 -> dialog.dispose(),
                KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW
        );

        return dialog;
    }
}