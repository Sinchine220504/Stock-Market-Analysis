package com.stockplatform.fetcher;

import com.stockplatform.db.DBConnection;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class StockDataFetcher {

    private static final String CSV_FILE_PATH = "mock_prices.csv"; // Place this file in the root of your project

    public static void startFetching() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

        executor.scheduleAtFixedRate(() -> {
            try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
                String line;
                boolean skipHeader = true;

                while ((line = br.readLine()) != null) {
                    if (skipHeader) {
                        skipHeader = false;
                        continue;
                    }

                    String[] data = line.split(",");
                    if (data.length != 7) continue;

                    String symbol = data[0].trim();
                    String timestampStr = data[1].trim();
                    double open = Double.parseDouble(data[2].trim());
                    double close = Double.parseDouble(data[3].trim());
                    double high = Double.parseDouble(data[4].trim());
                    double low = Double.parseDouble(data[5].trim());
                    long volume = Long.parseLong(data[6].trim());

                    int stockId = getStockId(symbol);
                    if (stockId == -1) continue;

                    insertPrice(stockId, timestampStr, open, close, high, low, volume);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 30, TimeUnit.SECONDS);
    }

    private static int getStockId(String symbol) {
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT id FROM stocks WHERE symbol = ?");
            ps.setString(1, symbol);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    private static void insertPrice(int stockId, String timestampStr, double open, double close, double high, double low, long volume) {
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO stock_prices (stock_id, timestamp, open_price, close_price, high, low, volume) VALUES (?, ?, ?, ?, ?, ?, ?)");
            ps.setInt(1, stockId);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Timestamp ts = new Timestamp(sdf.parse(timestampStr).getTime());
            ps.setTimestamp(2, ts);
            ps.setDouble(3, open);
            ps.setDouble(4, close);
            ps.setDouble(5, high);
            ps.setDouble(6, low);
            ps.setLong(7, volume);

            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        startFetching();
    }

    public static List<Double> getClosingPrices(int stockId, int limit) {
        List<Double> prices = new ArrayList<>();
        String query = "SELECT close_price FROM stock_prices WHERE stock_id = ? ORDER BY timestamp DESC LIMIT ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, stockId);
            ps.setInt(2, limit);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                prices.add(rs.getDouble("close_price"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Optional: reverse to get oldest-to-newest order
        Collections.reverse(prices);
        return prices;
    }

}
