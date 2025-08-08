package com.trading;

import com.trading.exception.InsufficientHoldingException;
import com.trading.service.TradingService;
import com.trading.service.impl.TradingServiceImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class TradingSimulatorApp {
    public static void main(String[] args) {
        TradingService tradingService = new TradingServiceImpl();
        Scanner scanner = new Scanner(System.in);

        // Simulated current market prices
        Map<String, Double> currentPrices = new HashMap<>();
        currentPrices.put("TCS", 3850.00);
        currentPrices.put("INFY", 1540.50);
        currentPrices.put("RELIANCE", 2810.75);

        boolean running = true;
        while (running) {
            System.out.println("\n--- Stock Trading Simulator ---");
            System.out.println("1. Buy Stock");
            System.out.println("2. Sell Stock");
            System.out.println("3. View Portfolio");
            System.out.println("4. View Profit/Loss");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            int choice = Integer.parseInt(scanner.nextLine());
            try {
                switch (choice) {
                    case 1:
                        System.out.print("Enter stock symbol: ");
                        String buySymbol = scanner.nextLine().toUpperCase();

                        Double currentPrice = currentPrices.get(buySymbol);
                        if (currentPrice == null) {
                            System.out.println("Stock not found in market.");
                            break;
                        }

                        System.out.println("Current Market Price of " + buySymbol + ": Rs" + currentPrice);
                        System.out.print("Enter quantity to buy: ");
                        int buyQty = Integer.parseInt(scanner.nextLine());

                        double totalPrice = currentPrice * buyQty;
                        System.out.println("Total price: Rs" + totalPrice);
                        System.out.print("Confirm purchase? (Y/N): ");
                        String confirm = scanner.nextLine();

                        if (confirm.equalsIgnoreCase("Y")) {
                            tradingService.buyStock(buySymbol, currentPrice, buyQty);
                            System.out.println(" Purchase successful!");
                        } else {
                            System.out.println("Purchase cancelled.");
                        }
                        break;

                    case 2:
                    	 System.out.print("Enter stock symbol to sell: ");
                         String sellSymbol = scanner.nextLine().toUpperCase();

                         Double currentSellPrice = currentPrices.get(sellSymbol);
                         if (currentSellPrice == null) {
                             System.out.println("Stock not found in market.");
                             break;
                         }

                         System.out.println("Current Market Price of " + sellSymbol + ": Rs" + currentSellPrice);
                         System.out.print("Enter quantity to sell: ");
                         int sellQty = Integer.parseInt(scanner.nextLine());

                         double totalSellAmount = currentSellPrice * sellQty;
                         System.out.println("Total amount receivable: Rs" + totalSellAmount);
                         System.out.print("Confirm sale? (Y/N): ");
                         String sellConfirm = scanner.nextLine();

                         if (sellConfirm.equalsIgnoreCase("Y")) {
                             tradingService.sellStock(sellSymbol, currentSellPrice, sellQty);
                             System.out.println("Sale completed!");
                         } else {
                             System.out.println("Sale cancelled.");
                         }
                         break;

                     case 3:
                         tradingService.displayPortfolio();
                         break;

                     case 4:
                         tradingService.displayProfitLoss(currentPrices);
                         break;

                     case 5:
                         running = false;
                         System.out.println("Exiting Simulator. Thank you!");
                         break;

                     default:
                         System.out.println("Invalid choice. Please enter between 1-5.");
                 }
             } catch (InsufficientHoldingException e) {
                 System.out.println("Error: " + e.getMessage());
             } catch (Exception e) {
                 System.out.println("Unexpected error: " + e.getMessage());
             }
         }

        scanner.close();
    }
}