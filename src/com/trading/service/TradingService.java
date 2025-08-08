package com.trading.service;

import com.trading.exception.InsufficientHoldingException;
import java.util.Map;

// Interface defining trading actions
public interface TradingService {
    void buyStock(String symbol, double currentPrice, int quantity);
    void sellStock(String symbol, double price, int quantity) throws InsufficientHoldingException;
    void displayPortfolio();
    void displayProfitLoss(Map<String, Double> currentPrices);
}
