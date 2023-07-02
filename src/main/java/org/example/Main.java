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
        FirstFileFrame firstFileFrame = new FirstFileFrame();
        JPanel root = firstFileFrame.getFirstRootPanel();
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(root);
        frame.pack();

        // Set the desired size of the frame
        int width = 600;  // Adjust to your preferred width
        int height = 400; // Adjust to your preferred height
        frame.setSize(width, height);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

}
