package com.trading.model;

import java.time.LocalDateTime;

//Model for trade history record
public class Trade {
 private String symbol;
 private int quantity;
 private double priceAtTrade;
 private String action; // BUY or SELL
 private LocalDateTime tradeTime;

 public Trade(String symbol, int quantity, double price, String action, LocalDateTime time) {
     this.symbol = symbol;
     this.quantity = quantity;
     this.priceAtTrade = price;
     this.action = action;
     this.tradeTime = time;
 }

 public String getSymbol() { return symbol; }
 public int getQuantity() { return quantity; }
 public double getPriceAtTrade() { return priceAtTrade; }
 public String getAction() { return action; }
 public LocalDateTime getTradeTime() { return tradeTime; }
}
