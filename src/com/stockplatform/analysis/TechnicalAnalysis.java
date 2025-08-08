package com.stockplatform.analysis;

import java.util.*;

public class TechnicalAnalysis {

    public static double calculateSMA(List<Double> prices, int period) {
        if (prices.size() < period) return 0;
        double sum = 0;
        for (int i = prices.size() - period; i < prices.size(); i++) {
            sum += prices.get(i);
        }
        return sum / period;
    }

    public static double calculateRSI(List<Double> prices, int period) {
        if (prices.size() < period + 1) return 0;

        double gain = 0, loss = 0;
        for (int i = prices.size() - period; i < prices.size(); i++) {
            double diff = prices.get(i) - prices.get(i - 1);
            if (diff > 0) gain += diff;
            else loss -= diff;
        }

        if (loss == 0) return 100;
        double rs = gain / loss;
        return 100 - (100 / (1 + rs));
    }

    public static double calculateEMA(List<Double> prices, int period) {
        if (prices.size() < period) return 0;

        double multiplier = 2.0 / (period + 1);
        double ema = calculateSMA(prices.subList(0, period), period);

        for (int i = period; i < prices.size(); i++) {
            ema = ((prices.get(i) - ema) * multiplier) + ema;
        }
        return ema;
    }

    public static double calculateMACD(List<Double> prices) {
        double ema12 = calculateEMA(prices, 12);
        double ema26 = calculateEMA(prices, 26);
        return ema12 - ema26;
    }
}
