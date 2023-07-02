package org.example;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SecondFileFrame implements ActionListener {
    private JButton browseSecondFile;
    private JButton submitSecondFile;
    private JPanel secondRootPanel;

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    public SecondFileFrame() {
        browseSecondFile.addActionListener(this);
        submitSecondFile.addActionListener(this);
    }

    public JPanel getSecondRootPanel() {
        return secondRootPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Next")) {


        } else {
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter excelFilter = new FileNameExtensionFilter("Excel Files", "xlsx", "xls");
            FileNameExtensionFilter csvFilter = new FileNameExtensionFilter("CSV Files", "csv");

            fileChooser.addChoosableFileFilter(excelFilter);
            fileChooser.addChoosableFileFilter(csvFilter);
            fileChooser.setAcceptAllFileFilterUsed(false);

            int option = fileChooser.showOpenDialog(SecondFileFrame.this.getSecondRootPanel());

            if (option == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                String filePath = selectedFile.getPath();
                Globals.secondFilePath = filePath;
                System.out.println(filePath);
                try {
                    JOptionPane.showMessageDialog(null, "Data saved successfully!");
                    String[] path = Globals.secondFilePath.trim().split("\\\\");
                    String fileName = path[path.length - 1];

                    if (e.getActionCommand().equals("Browse Second File")) {
                        browseSecondFile.setText(fileName);
                        if (selectedFile.getName().endsWith(".xlsx") || selectedFile.getName().endsWith(".xls")) {
                            Globals.file2 = Utilities.readExcelFile(filePath);
                        } else if (selectedFile.getName().endsWith(".csv")) {
                            Globals.file2 = Utilities.readCSVFile(filePath);
                        }

                        List<String> secondList = Globals.file2.get(0);
                        for (String item : secondList) {
                            Globals.listModel2.addElement(item);
                            Globals.listModel3.addElement("Table 1- "+item);
                        }
                        Globals.ListFile2.setModel(Globals.listModel2);
                        Globals.Combained_List.setModel(Globals.listModel2);
                    }

                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error occurred while saving the data.");
                }
            }
        }
    }

}
