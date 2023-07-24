package org.example;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Utilities {

    static Lock newDataLock = new ReentrantLock();

    public static List<List<String>> readExcelFile(String filePath) throws IOException {
        List<List<String>> data = new ArrayList<>();
        FileInputStream inputStream = new FileInputStream(filePath);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        for (Row row : sheet) {
            String[] rowData = new String[row.getLastCellNum()];
            for (int i = 0; i < rowData.length; i++) {
                Cell cell = row.getCell(i);
                if (cell != null) {
                    rowData[i] = cell.toString().trim();
                }
            }
            data.add(Arrays.asList(rowData));
        }
        workbook.close();
        return data;
    }

    public static List<List<String>> readCSVFile(String filePath) throws IOException {
        List<List<String>> data = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] rowData = line.split(",");
            data.add(Arrays.asList(rowData));
        }
        reader.close();
        return data;
    }

    private static List<String> createNewRow(List<String> file1Record, List<String> file2Record, double similarityScore) {
        List<String> newRow = new ArrayList<>();
        newRow.addAll(file1Record);
        newRow.addAll(file2Record);
        newRow.add(String.valueOf(similarityScore));
        return newRow;
    }

    public static void processFileData(List<String> file1Record, List<List<String>> file2Data, List<List<String>> newData,
                                       int[] selectedColumnsList1, int[] selectedColumnsList2, double threshold) {
        double bestSimilarityScore = 0;
        List<String> bestMatch = null;

        for (List<String> file2Record : file2Data) {
            double similarityScore = calculateSimilarityScore(file1Record, file2Record, selectedColumnsList1, selectedColumnsList2);
            if (similarityScore > bestSimilarityScore && similarityScore > threshold) {
                bestSimilarityScore = similarityScore;
                bestMatch = file2Record;
            }
        }

        if (bestMatch != null) {
            List<String> newRow = createNewRow(file1Record, bestMatch, bestSimilarityScore);
            newDataLock.lock();
            try {
                newData.add(newRow);
            } finally {
                newDataLock.unlock();
            }
        }
    }

    private static double calculateSimilarityScore(List<String> record1, List<String> record2, int[] selectedColumnsList1,int[] selectedColumnsList2) {
        double similaritySum = 0.0;
        int numColumns = 0;

        for (int columnIndex1 : selectedColumnsList1) {
            for(int columnIndex2: selectedColumnsList2) {
                String value1 = (columnIndex1 >= 0 && columnIndex1 < record1.size() && record1.get(columnIndex1) != null)
                        ? record1.get(columnIndex1) : "";
                String value2 = (columnIndex2 >= 0 && columnIndex2 < record2.size() && record2.get(columnIndex2) != null)
                        ? record2.get(columnIndex2) : "";

                if (isNumeric(value1) && isNumeric(value2)) {
                    double numericSimilarity = calculateNumericSimilarity(value1, value2);
                    similaritySum += numericSimilarity;
                    numColumns++;
                } else {
                    double textSimilarity = calculateTextSimilarity(value1, value2);
                    similaritySum += textSimilarity;
                    numColumns++;
                }
            }
        }

        if (numColumns > 0) {
            return similaritySum / numColumns;
        } else {
            return 0.0;
        }
    }

    private static double calculateNumericSimilarity(String value1, String value2) {
        // Calculate the normalized Levenshtein distance between the two numeric values
        int editDistance = calculateLevenshteinDistance(value1, value2);
        int maxLength = Math.max(value1.length(), value2.length());
        double similarity = 1.0 - (double) editDistance / maxLength;
        return similarity;
    }

    private static double calculateTextSimilarity(String value1, String value2) {
        int editDistance = calculateLevenshteinDistance(value1, value2);
        int maxLength = Math.max(value1.length(), value2.length());
        double similarity = 1.0 - (double) editDistance / maxLength;
        return similarity;
    }

    private static int calculateLevenshteinDistance(String str1, String str2) {
        int[][] dp = new int[str1.length() + 1][str2.length() + 1];

        for (int i = 0; i <= str1.length(); i++) {
            for (int j = 0; j <= str2.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    int substitutionCost = (str1.charAt(i - 1) == str2.charAt(j - 1)) ? 0 : 1;
                    dp[i][j] = Math.min(Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1), dp[i - 1][j - 1] + substitutionCost);
                }
            }
        }

        return dp[str1.length()][str2.length()];
    }

    private static boolean isNumeric(String str) {
        return str != null && str.matches("-?\\d+(\\.\\d+)?");
    }

    public static void writeExcelFile(String filePath, List<List<String>> data, List<List<String>> file1Data) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Results");

        // Add the first row from the input file as the first row in the output file
        if (!file1Data.isEmpty()) {
            List<String> firstRow = file1Data.get(0);
            Row headerRow = sheet.createRow(0);
            int cellIndex = 0;
            for (String cellData : firstRow) {
                Cell cell = headerRow.createCell(cellIndex++);
                cell.setCellValue(cellData);
            }
        }

        int rowIndex = 1; // Start from the second row

        // Add the remaining rows from the data list
        for (List<String> rowData : data) {
            Row row = sheet.createRow(rowIndex++);
            int cellIndex = 0;
            for (String cellData : rowData) {
                Cell cell = row.createCell(cellIndex++);
                cell.setCellValue(cellData);
            }
        }
        System.out.println("FINISH");

        FileOutputStream outputStream = new FileOutputStream(filePath);
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

    private static List<Integer> parseColumnSelection(String columnSelection) {
        String[] columns = columnSelection.split(",");
        List<Integer> selectedColumns = new ArrayList<>();
        for (String column : columns) {
            try {
                int columnIndex = Integer.parseInt(column.trim());
                selectedColumns.add(columnIndex);
            } catch (NumberFormatException e) {
                System.out.println("Invalid column number: " + column);
            }
        }
        return selectedColumns;
    }
}
