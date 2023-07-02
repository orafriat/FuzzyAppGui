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

public class FirstFileFrame implements ActionListener {
    private JButton browseFirstFile;
    private JButton submitFirstFile;
    private JPanel firstRootPanel;

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    public FirstFileFrame() {
        browseFirstFile.addActionListener(this);
        submitFirstFile.addActionListener(this);
    }

    public JPanel getFirstRootPanel() {
        return firstRootPanel;
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

            int option = fileChooser.showOpenDialog(FirstFileFrame.this.getFirstRootPanel());

            if (option == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                String filePath = selectedFile.getPath();
                Globals.firstFilePath = filePath;
                System.out.println(filePath);
                try {
                    JOptionPane.showMessageDialog(null, "Data saved successfully!");
                    String[] path = Globals.firstFilePath.trim().split("\\\\");
                    String fileName = path[path.length - 1];

                    if (e.getActionCommand().equals("Browse First File")) {
                        browseFirstFile.setText(fileName);
                        if (selectedFile.getName().endsWith(".xlsx") || selectedFile.getName().endsWith(".xls")) {
                            Globals.file1 = Utilities.readExcelFile(filePath);
                        } else if (selectedFile.getName().endsWith(".csv")) {
                            Globals.file1 = Utilities.readCSVFile(filePath);
                        }

                        List<String> firstList = Globals.file1.get(0);
                        for (String item : firstList) {
                            Globals.listModel1.addElement(item);
                            Globals.listModel3.addElement("Table 1- "+item);
                        }
                        Globals.ListFile1.setModel(Globals.listModel1);
                        Globals.Combained_List.setModel(Globals.listModel1);
                    }

                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error occurred while saving the data.");
                }
            }
        }
    }

}
