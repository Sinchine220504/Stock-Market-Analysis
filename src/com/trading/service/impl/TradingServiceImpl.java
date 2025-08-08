package com.trading.service.impl;

import com.trading.model.*;
import com.trading.service.TradingService;
import com.trading.exception.InsufficientHoldingException;

import java.util.*;

public class TradingServiceImpl implements TradingService {

    private List<PortfolioEntry> holdings = new ArrayList<>();
    private List<Trade> tradeHistory = new ArrayList<>();

    @Override
    public void buyStock(String symbol, double currentPrice, int quantity) {
        boolean exists = false;

        for (PortfolioEntry entry : holdings) {
            if (entry.getSymbol().equalsIgnoreCase(symbol)) {
                int totalQty = entry.getTotalQuantity() + quantity;
                double newAvgPrice = ((entry.getAverageBuyPrice() * entry.getTotalQuantity()) + (currentPrice * quantity)) / totalQty;
                entry.setTotalQuantity(totalQty);
                entry.setAverageBuyPrice(newAvgPrice);
                exists = true;
                break;
            }
        }

        if (!exists) {
            holdings.add(new PortfolioEntry(symbol, quantity, currentPrice));
        }

        tradeHistory.add(new Trade(symbol, quantity, currentPrice, "BUY", java.time.LocalDateTime.now()));
        System.out.println("Bought " + quantity + " shares of " + symbol + " at Rs" + currentPrice);
    }

    @Override
    public void sellStock(String symbol, double price, int quantity) throws InsufficientHoldingException {
        PortfolioEntry found = null;
        for (PortfolioEntry entry : holdings) {
            if (entry.getSymbol().equalsIgnoreCase(symbol)) {
                found = entry;
                break;
            }
        }

        if (found == null || found.getTotalQuantity() < quantity) {
            throw new InsufficientHoldingException("Insufficient shares to sell.");
        }

        found.setTotalQuantity(found.getTotalQuantity() - quantity);
        if (found.getTotalQuantity() == 0) {
            holdings.remove(found);
        }

        tradeHistory.add(new Trade(symbol, quantity, price, "SELL", java.time.LocalDateTime.now()));
        System.out.println("Sold " + quantity + " shares of " + symbol + " at Rs" + price);
    }

    @Override
    public void displayPortfolio() {
        System.out.println("\n Current Portfolio:");
        for (PortfolioEntry entry : holdings) {
            System.out.println("Stock: " + entry.getSymbol()
                    + ", Quantity: " + entry.getTotalQuantity()
                    + ", Avg. Buy Price: Rs" + entry.getAverageBuyPrice());
        }
    }

    @Override
    public void displayProfitLoss(Map<String, Double> currentPrices) {
        System.out.println("\n Profit/Loss Report:");
        for (PortfolioEntry entry : holdings) {
            String symbol = entry.getSymbol();
            double currentPrice = currentPrices.getOrDefault(symbol, 0.0);
            double profitLoss = (currentPrice - entry.getAverageBuyPrice()) * entry.getTotalQuantity();

            System.out.println("Stock: " + symbol
                    + " | Qty: " + entry.getTotalQuantity()
                    + " | Buy @ Rs" + entry.getAverageBuyPrice()
                    + " | Current Rs" + currentPrice
                    + " | P/L: Rs" + profitLoss);
        }
    }
}