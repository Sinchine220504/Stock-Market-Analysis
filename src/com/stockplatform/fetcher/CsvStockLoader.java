package com.stockplatform.fetcher;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class CsvStockLoader {

    public static void main(String[] args) {
        String csvFile = "C:\\Users\\SINCHINE DAS\\eclipse-workspace\\StockPlatform1\\mock_prices.csv"; // make sure this path is correct
        String line;
        String splitBy = ",";

        List<String[]> records = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            // Skip the header
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] values = line.split(splitBy);
                if (values.length >= 7) {
                    records.add(values);
                }
            }

            if (records.size() < 26) {
                System.out.println("Not enough data (need at least 26 records)");
                return;
            }

        } catch (Exception e) {
            System.out.println("File read error: " + e.getMessage());
            return;
        }

        // Insert into database
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/stock_market_platform", "root", "akanksha");

            String sql = "INSERT INTO stock_prices (date, open_price, close_price, low, high, volume) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            for (String[] values : records) {
                String date = values[1];                        // 01-01-2023 00:00
                double open = Double.parseDouble(values[2]);   // 145.73
                double close = Double.parseDouble(values[3]);  // 144.86
                double low = Double.parseDouble(values[4]);    // 144.14
                double high = Double.parseDouble(values[5]);   // 146.24
                long volume = Long.parseLong(values[6]);       // 3331029

                stmt.setString(1, date);
                stmt.setDouble(2, open);
                stmt.setDouble(3, close);
                stmt.setDouble(4, low);
                stmt.setDouble(5, high);
                stmt.setLong(6, volume);

                stmt.addBatch();
            }

            stmt.executeBatch();
            conn.close();
            System.out.println("Stock data inserted successfully!");

        } catch (Exception e) {
            System.out.println("DB error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
