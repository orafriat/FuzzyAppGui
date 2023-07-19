package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class UserSelectionFrame {
    private JList<String> list1;
    private JList<String> list2;
    private JButton mergeButton1;
    private JList<String> list3;
    private JButton generateFileButton;
    private JTextField thresholdTextField;
    private JPanel thirdRootPanel;

    private JFrame mainFrame; // Reference to the main JFrame


    public UserSelectionFrame(JFrame mainFrame) {
        this.mainFrame = mainFrame;
        list1.setModel(Globals.listModel1);
        list2.setModel(Globals.listModel2);
        list3.setModel(Globals.listModel3);
        mergeButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int[] valueList1 = list1.getSelectedIndices();
                Globals.selectedColumnsList1 = valueList1;

                int[] valueList2 = list2.getSelectedIndices();
                Globals.selectedColumnsList2 = valueList2;

            }
        });

        generateFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int[] valueList3 = list3.getSelectedIndices();
                Globals.userChoiceList3 = valueList3;
                Globals.threshold = thresholdTextField.getText();
                List<List<String>> newData = new ArrayList<>();

                // Add the first row from file1Data to newData
                if (!Globals.file1.isEmpty()) {
                    newData.add(Globals.file1.get(0));
                }

                int threadPoolSize = Math.max(Globals.file1.size() - 1, Globals.file1.size()); // Use the smaller size as the thread pool size
                ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(threadPoolSize);

                for (int i = 1; i < Globals.file1.size(); i++) {
                    final int currentIndex = i;
                    executor.execute(() -> Utilities.processFileData(Globals.file1.get(currentIndex), Globals.file2, newData, Globals.selectedColumnsList1, Globals.selectedColumnsList2, Double.parseDouble(Globals.threshold)));
                }

                executor.shutdown();
                try {
                    executor.awaitTermination(1, TimeUnit.HOURS);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }

                try {
                    Utilities.writeExcelFile(Globals.newFilePath, newData, Globals.file1);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }


                System.out.println(valueList3[0]);

            }
        });
    }

    public JPanel getThirdRootPanel() {
        return thirdRootPanel;
    }


}
