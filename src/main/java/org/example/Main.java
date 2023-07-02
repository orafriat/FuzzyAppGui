package org.example;

import javax.swing.*;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createGUI();
            }}
        );
    }

    private static void createGUI() {
        JFrame mainFrame = new JFrame("Main Frame");
        FirstFileFrame firstFileFrame = new FirstFileFrame(mainFrame);
        JPanel root = firstFileFrame.getFirstRootPanel();
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setContentPane(root);
        mainFrame.pack();

        // Set the desired size of the frame
        int width = 600;  // Adjust to your preferred width
        int height = 400; // Adjust to your preferred height
        mainFrame.setSize(width, height);

        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);

    }

}
