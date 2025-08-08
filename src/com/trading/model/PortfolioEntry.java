package com.trading.model;

// Model for a stock entry in user's portfolio
public class PortfolioEntry {
    private String symbol;
    private int totalQuantity;
    private double averageBuyPrice;

    public PortfolioEntry(String symbol, int quantity, double avgPrice) {
        this.symbol = symbol;
        this.totalQuantity = quantity;
        this.averageBuyPrice = avgPrice;
    }

    public String getSymbol() { return symbol; }
    public int getTotalQuantity() { return totalQuantity; }
    public double getAverageBuyPrice() { return averageBuyPrice; }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public void setAverageBuyPrice(double averageBuyPrice) {
        this.averageBuyPrice = averageBuyPrice;
    }
}