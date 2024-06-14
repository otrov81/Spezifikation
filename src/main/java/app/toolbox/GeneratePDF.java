package app.toolbox;

import app.model.SpeziDetail;
import app.model.SpeziPdf;
import app.notifications.Notifications;
import app.service.DBService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class to generate PDF reports using JasperReports.
 */
public class GeneratePDF {

    private JFrame frame;

    @Autowired
    private DBService dbService;

    /**
     * Generates a PDF report based on given parameters and data.
     *
     * @param frame      Parent JFrame used for dialogs and notifications
     * @param dbService  Database service to retrieve data
     * @param reportName Name of the JasperReports .jrxml file (without extension)
     * @param sa         Parameter for querying details
     * @param txtSchluessel Parameter for querying details
     * @param spcd       Parameter for querying details
     */
    public void generatePDF(JFrame frame, DBService dbService, String reportName, String sa, String txtSchluessel, String spcd) {
        this.dbService = dbService;
        this.frame = frame;

        try {
            // Variables to hold extracted values
            String adresskopf1 = "";
            String adresskopf2 = "";
            String adressdetail1 = "";
            String adressdetail2 = "";
            String bankkopf1 = "";
            String bankkopf2 = "";
            String bankkopf3 = "";
            String bankkopf4 = "";
            String bankdetail1 = "";
            String bankdetail2 = "";
            String bankdetail3 = "";
            String bankdetail4 = "";
            String detailinfo = "";

            // Load JasperDesign from .jrxml file
            InputStream inputStream = getClass().getResourceAsStream("/pdf/" + reportName + ".jrxml");
            JasperDesign jasperDesign = JRXmlLoader.load(inputStream);

            // Compile JasperReport from JasperDesign
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

            // Retrieve data from database
            List<SpeziPdf> dataList = dbService.searchByIcon(reportName);

            // Extract necessary data from each SpeziPdf object
            for (SpeziPdf speziPdf : dataList) {
                String dataString = speziPdf.toString();
                HashMap<String, String> map = extractValues(dataString);

                // Example of how to retrieve values from the map
                adresskopf1 = map.get("adresskopf1");
                adresskopf2 = map.get("adresskopf2");
                adressdetail1 = map.get("adressdetail1");
                adressdetail2 = map.get("adressdetail2");
                bankkopf1 = map.get("bankkopf1");
                bankkopf2 = map.get("bankkopf2");
                bankkopf3 = map.get("bankkopf3");
                bankkopf4 = map.get("bankkopf4");
                bankdetail1 = map.get("bankdetail1");
                bankdetail2 = map.get("bankdetail2");
                bankdetail3 = map.get("bankdetail3");
                bankdetail4 = map.get("bankdetail4");
                detailinfo = map.get("detailinfo");
            }

            // Set parameters for the report
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("adresskopf1", adresskopf1);
            parameters.put("adresskopf2", adresskopf2);
            parameters.put("adressdetail1", adressdetail1);
            parameters.put("adressdetail2", adressdetail2);
            parameters.put("bankkopf1", bankkopf1);
            parameters.put("bankkopf2", bankkopf2);
            parameters.put("bankkopf3", bankkopf3);
            parameters.put("bankkopf4", bankkopf4);
            parameters.put("bankdetail1", bankdetail1);
            parameters.put("bankdetail2", bankdetail2);
            parameters.put("bankdetail3", bankdetail3);
            parameters.put("bankdetail4", bankdetail4);
            parameters.put("detailinfo", detailinfo);

            // Retrieve additional details from database
            List<SpeziDetail> selectDetailInfo = dbService.selectSpeziDetail(sa, txtSchluessel, spcd);
            JRDataSource dataSourcePDF = new JRBeanCollectionDataSource(selectDetailInfo);

            // Generate JasperPrint for PDF generation
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSourcePDF);

            // Create temporary PDF file
            File pdfFile = File.createTempFile("output", ".pdf");

            // Export JasperPrint to PDF file
            JasperExportManager.exportReportToPdfFile(jasperPrint, pdfFile.getAbsolutePath());

            // Open the generated PDF file
            Desktop.getDesktop().open(pdfFile);

            // Show success notification
            Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.TOP_LEFT,
                    "PDF successfully generated and opened!");

        } catch (JRException | IOException e) {
            e.printStackTrace();
            // Show error message in dialog box
            JOptionPane.showMessageDialog(frame, "Error generating and opening PDF: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Extracts key-value pairs from a formatted string.
     *
     * @param input String containing key-value pairs in format "(key1=value1, key2=value2, ...)"
     * @return HashMap containing extracted key-value pairs
     */
    public static HashMap<String, String> extractValues(String input) {
        HashMap<String, String> map = new HashMap<>();
        // Remove unnecessary characters from the string
        input = input.substring(input.indexOf("(") + 1, input.lastIndexOf(")"));
        // Split the string by commas
        String[] parts = input.split(", ");
        for (String part : parts) {
            // Split each part by the equals sign
            String[] keyValue = part.split("=");
            if (keyValue.length == 2) {
                // Add key-value pair to the map
                map.put(keyValue[0], keyValue[1]);
            }
        }
        return map;
    }

    /**
     * Loads a font file and registers it in the local GraphicsEnvironment.
     *
     * @param fontFileName Name of the font file to be loaded
     * @param fontSize     Size of the font to be registered
     */
    private void loadFont(String fontFileName, float fontSize) {
        try {
            // Load font from file
            File fontFile = new File(fontFileName);
            Font dynamicFont = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(fontSize);

            // Register font in the local GraphicsEnvironment
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(dynamicFont);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            // Handle or log error in case of exception
        }
    }
}
