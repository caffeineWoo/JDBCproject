package test;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI() {
        // GUI 프레임 생성
        JFrame frame = new JFrame("MySQL Query Tool");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1600, 700);

        // 패널 생성
        renewGUI mainPanel = new renewGUI();
        frame.add(mainPanel);
        frame.setVisible(true);
    }
}
