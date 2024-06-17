package app.microservice;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
@Component
public class Microservice {
    private String valueUrl;
    private String valueUsername;
    private String valuePasswort;

    public Microservice(){
        getMysqlInfo();
    }
    private void getMysqlInfo() {
        Properties props = new Properties();

        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            props.load(input);

            valueUrl = props.getProperty("spring.datasource.url");
            valueUsername = props.getProperty("spring.datasource.username");
            valuePasswort = props.getProperty("spring.datasource.password");



        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getArtikelNameFromErp(String artikelnr) {

        String artName = "";

        try {
            Connection connection = DriverManager.getConnection(valueUrl, valueUsername, valuePasswort);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT er.name FROM erp.artikel er WHERE er.artikelnr='"+artikelnr+"'");

            if (resultSet.next()) {
                artName = resultSet.getString("name");
            }else{
                artName="*** Artikel "+artikelnr+" nicht im Artikelstamm! ***";
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return artName;
    }
    public String getKundenNameFromErp(String kundennr) {

        String kunName = "";

        try {
            Connection connection = DriverManager.getConnection(valueUrl, valueUsername, valuePasswort);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT er.name, er.laendercode_iso FROM erp.kunden er WHERE er.kundennr='"+kundennr+"'");

            if (resultSet.next()) {
                kunName = resultSet.getString("name")+"|"+resultSet.getString("laendercode_iso");
            }else{
                kunName="*** Kunden "+kundennr+" nicht im Kundenstamm! ***";
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return kunName;
    }

    public List<String> getLandFromErp() {
        List<String> landNames = new ArrayList<>();

        // Adding the first field to the beginning of the list
        landNames.add("00| -- Auswahl --");

        try {
            Connection connection = DriverManager.getConnection(valueUrl, valueUsername, valuePasswort);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT kunden.landesbez, kunden.laendercode_iso FROM erp.kunden GROUP BY kunden.landesbez, kunden.laendercode_iso HAVING (((kunden.laendercode_iso) Is Not Null)) ORDER BY kunden.landesbez;");

            while (resultSet.next()) {
                String landName = resultSet.getString("laendercode_iso")+"|"+resultSet.getString("landesbez");
                landNames.add(landName);
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return landNames;
    }


    public void getUpdateByLandName(Integer mand, String sa, String schluessel, String spcd, String schluesselLand) { //zweiterschlussel
        Connection connection = null;
        PreparedStatement selectStatement = null;
        PreparedStatement insertStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(valueUrl, valueUsername, valuePasswort);
            // Select query
            String selectQuery = "SELECT znr, textart, titel FROM Spezi.tblspez_detail WHERE schluessel=? AND mand=? AND spcd=?";
            selectStatement = connection.prepareStatement(selectQuery);
            selectStatement.setString(1, schluesselLand);
            selectStatement.setInt(2, mand);
            selectStatement.setString(3, spcd);

            resultSet = selectStatement.executeQuery();

            // Insert query
            String insertQuery = "INSERT INTO Spezi.tblspez_detail (mand, sa, schluessel, spcd, znr, textart, titel) VALUES (?, ?, ?, ?, ?, ?, ?)";
            insertStatement = connection.prepareStatement(insertQuery);

            while (resultSet.next()) {

                // Set values for the insert statement
                insertStatement.setInt(1, mand);
                insertStatement.setString(2, sa);
                insertStatement.setString(3, schluessel);
                insertStatement.setString(4, spcd);
                insertStatement.setString(5, resultSet.getString("znr"));
                insertStatement.setString(6, resultSet.getString("textart"));
                insertStatement.setString(7, resultSet.getString("titel"));

                // Execute the insert statement
                insertStatement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("SQL error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Close resources in finally block to ensure they are always closed
            try {
                if (resultSet != null) resultSet.close();
                if (selectStatement != null) selectStatement.close();
                if (insertStatement != null) insertStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public boolean checkRABExistence(String kundennr, String artikelnr) {
        boolean exists = false;

        try {
            Connection connection = DriverManager.getConnection(valueUrl, valueUsername, valuePasswort);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) AS count FROM erp.kab WHERE kundennr='"+kundennr+"' AND artikelnr='"+artikelnr+"'");

            if (resultSet.next()) {
                int count = resultSet.getInt("count");
                exists = (count > 0);
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return exists;
    }

    public void getUpdateByLandNameSpezial(Integer mand, String sa, String schluessel, String spcd, String schluesselLand, String saSpez) { //zweiterschlussel
        Connection connection = null;
        PreparedStatement selectStatement = null;
        PreparedStatement insertStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(valueUrl, valueUsername, valuePasswort);
            // Select query
            String selectQuery = "SELECT znr, textart, titel FROM Spezi.tblspez_detail WHERE schluessel=? AND mand=? AND spcd=? AND sa ='"+saSpez+"'";
            selectStatement = connection.prepareStatement(selectQuery);
            selectStatement.setString(1, schluesselLand);
            selectStatement.setInt(2, mand);
            selectStatement.setString(3, spcd);

            resultSet = selectStatement.executeQuery();

            // Insert query
            String insertQuery = "INSERT INTO Spezi.tblspez_detail (mand, sa, schluessel, spcd, znr, textart, titel) VALUES (?, ?, ?, ?, ?, ?, ?)";
            insertStatement = connection.prepareStatement(insertQuery);

            while (resultSet.next()) {

                // Set values for the insert statement
                insertStatement.setInt(1, mand);
                insertStatement.setString(2, sa);
                insertStatement.setString(3, schluessel);
                insertStatement.setString(4, spcd);
                insertStatement.setString(5, resultSet.getString("znr"));
                insertStatement.setString(6, resultSet.getString("textart"));
                insertStatement.setString(7, resultSet.getString("titel"));

                // Execute the insert statement
                insertStatement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("SQL error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Close resources in finally block to ensure they are always closed
            try {
                if (resultSet != null) resultSet.close();
                if (selectStatement != null) selectStatement.close();
                if (insertStatement != null) insertStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }


}
