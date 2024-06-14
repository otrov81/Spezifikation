package app.toolbox;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Utility class for handling Excel operations using Apache POI library.
 */
public class ExcelPOI {

    /** File path for the Excel file */
    private static String filePath = "exported.xlsx";

    /**
     * Method to open the Excel file using the default application.
     * This method assumes the file already exists or will be created later.
     */
    public void openExcelFile() {
        try {
            // Open or create a new Excel workbook
            Workbook workbook = new XSSFWorkbook();

            // Close the workbook (no need to save changes as we haven't modified it)
            workbook.close();

            // Optional: Open Excel application (if not already opened) using cmd command
            // Note: This method is platform-dependent and works on Windows with Excel installed
            Runtime.getRuntime().exec("cmd /c start excel \"" + filePath + "\"");

        } catch (Exception e) {
            e.printStackTrace();
            // Handle exceptions here, such as displaying an error message
            // JOptionPane.showMessageDialog(this, "Error opening Excel file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Method to export data to an Excel file.
     *
     * @param headerValues List of header values (column names) for the Excel sheet
     * @param dataList     List of lists containing data rows to be exported
     */
    public static void exportExcel(List<String> headerValues, List<List<Object>> dataList) {
        try (Workbook workbook = new XSSFWorkbook()) {
            // Create a new sheet named "Data" in the workbook
            Sheet sheet = workbook.createSheet("Data");

            // Create header row in the Excel sheet
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headerValues.size(); i++) {
                // Set header values (column names)
                headerRow.createCell(i).setCellValue(headerValues.get(i));
            }

            // Populate data rows in the Excel sheet
            for (int rowIndex = 0; rowIndex < dataList.size(); rowIndex++) {
                List<Object> rowData = dataList.get(rowIndex);
                Row dataRow = sheet.createRow(rowIndex + 1); // Start from row 1 (index 0 is header)
                for (int colIndex = 0; colIndex < rowData.size(); colIndex++) {
                    Object cellValue = rowData.get(colIndex);

                    // Set cell value based on type (handle Long, String, etc.)
                    if (cellValue instanceof Long) {
                        dataRow.createCell(colIndex).setCellValue((Long) cellValue);
                    } else if (cellValue instanceof String) {
                        dataRow.createCell(colIndex).setCellValue((String) cellValue);
                    } else if (cellValue instanceof Integer) {
                        dataRow.createCell(colIndex).setCellValue((Integer) cellValue);
                    } else if (cellValue instanceof Date) {
                        // Example: Convert Date to Excel-compatible format
                        dataRow.createCell(colIndex).setCellValue((Date) cellValue);
                    } else {
                        // Handle other types if necessary
                        dataRow.createCell(colIndex).setCellValue(String.valueOf(cellValue));
                    }
                }
            }

            // Write the workbook to the file system
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
                System.out.println("Excel file successfully created at path: " + filePath);
            } catch (IOException e) {
                e.printStackTrace();
                // Handle IO exceptions during file writing
            }

        } catch (IOException e) {
            e.printStackTrace();
            // Handle IO exceptions during workbook creation or sheet manipulation
        }
    }

}
