package org.example;

import com.graphbuilder.curve.NURBSpline;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Globals {

    //First File Data
    public static List<List<String>> file1;
    public static JList<String> ListFile1;
    public static String firstFilePath;
    public static DefaultListModel<String> listModel1 = new DefaultListModel<>();

    public static List<List<String>> file2;
    public static JList<String> ListFile2;
    public static String secondFilePath;
    public static DefaultListModel<String> listModel2 = new DefaultListModel<>();



    public static JList<String> Combained_List;
    public static DefaultListModel<String> listModel3 = new DefaultListModel<>();

    public static int[] selectedColumnsList1;
    public static int[] selectedColumnsList2;
    public static int[] userChoiceList3;
    public static List<Integer> combinedList1 = new ArrayList<>();
    public static List<Integer> combinedList2 = new ArrayList<>();

    public static String threshold;

    public static String newFilePath = "newFile.xlsx";



}
