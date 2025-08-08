
import com.stockplatform.analysis.TechnicalAnalysis;
import com.stockplatform.fetcher.StockDataFetcher;
import com.stockTracker.UserManager;
import com.stockTracker.User;
import com.trading.exception.InsufficientHoldingException;
import com.trading.service.TradingService;
import com.trading.service.impl.TradingServiceImpl;

import java.util.*;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static UserManager userManager = new UserManager();
    private static User loggedInUser = null;
    private static TradingService tradingService = new TradingServiceImpl();
    private static Map<String, Double> mockMarketPrices = new HashMap<>();

    public static void main(String[] args) {
        // Initialize stock price fetching
        StockDataFetcher.startFetching();

        // Mock current prices (can be fetched dynamically from DB or API)
        mockMarketPrices.put("TCS", 3850.00);
        mockMarketPrices.put("INFY", 1540.50);
        mockMarketPrices.put("RELIANCE", 2810.75);

        while (true) {
            if (loggedInUser == null) {
                showLoginMenu();
            } else {
                showTradingMenu();
            }
        }
    }

    private static void showLoginMenu() {
        System.out.println("\n--- Welcome to Stock Platform ---");
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Exit");
        System.out.print("Choose: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                System.out.print("Enter username: ");
                String regUsername = scanner.nextLine();
                System.out.print("Enter password: ");
                String regPassword = scanner.nextLine();
                User user = userManager.registerUser(regUsername, regPassword);
                System.out.println("✅ Registered! Your User ID: " + user.getUserId());
                break;
            case 2:
                System.out.print("Enter username: ");
                String username = scanner.nextLine();
                System.out.print("Enter password: ");
                String password = scanner.nextLine();
                if (userManager.loginUser(username, password)) {
                    loggedInUser = userManager.findUserByUsername(username);
                    System.out.println("✅ Login successful. Welcome, " + loggedInUser.getUsername());
                } else {
                    System.out.println("❌ Invalid credentials.");
                }
                break;
            case 3:
                System.out.println("🚪 Exiting... Goodbye!");
                System.exit(0);
                break;
            default:
                System.out.println("⚠️ Invalid choice.");
        }
    }

    private static void showTradingMenu() {
        System.out.println("\n--- Main Menu ---");
        System.out.println("1. Technical Analysis");
        System.out.println("2. Buy Stock");
        System.out.println("3. Sell Stock");
        System.out.println("4. View Portfolio");
        System.out.println("5. View Profit/Loss");
        System.out.println("6. Logout");
        System.out.print("Enter choice: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        try {
            switch (choice) {
                case 1:
                    performAnalysis();
                    break;
                case 2:
                    buyStock();
                    break;
                case 3:
                    sellStock();
                    break;
                case 4:
                    tradingService.displayPortfolio();
                    break;
                case 5:
                    tradingService.displayProfitLoss(mockMarketPrices);
                    break;
                case 6:
                    loggedInUser = null;
                    System.out.println("✅ Logged out.");
                    break;
                default:
                    System.out.println("⚠️ Invalid choice.");
            }
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    private static void performAnalysis() {
        System.out.print("Enter stock ID for analysis (e.g., 1): ");
        int stockId = scanner.nextInt();
        List<Double> prices = StockDataFetcher.getClosingPrices(stockId, 30);

        if (prices.size() < 26) {
            System.out.println("Not enough data (need at least 26 records).");
            return;
        }

        double sma = TechnicalAnalysis.calculateSMA(prices, 10);
        double rsi = TechnicalAnalysis.calculateRSI(prices, 14);
        double macd = TechnicalAnalysis.calculateMACD(prices);

        System.out.printf("📈 SMA (10): %.2f\n", sma);
        System.out.printf("📉 RSI (14): %.2f\n", rsi);
        System.out.printf("📊 MACD: %.2f\n", macd);
    }

    private static void buyStock() {
        System.out.print("Enter stock symbol to buy: ");
        String symbol = scanner.nextLine().toUpperCase();

        Double price = mockMarketPrices.get(symbol);
        if (price == null) {
            System.out.println("Stock not found.");
            return;
        }

        System.out.println("Current price of " + symbol + " is Rs" + price);
        System.out.print("Enter quantity: ");
        int qty = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Confirm purchase? (Y/N): ");
        if (scanner.nextLine().equalsIgnoreCase("Y")) {
            tradingService.buyStock(symbol, price, qty);
        } else {
            System.out.println("Purchase cancelled.");
        }
    }

    private static void sellStock() throws InsufficientHoldingException {
        System.out.print("Enter stock symbol to sell: ");
        String symbol = scanner.nextLine().toUpperCase();

        Double price = mockMarketPrices.get(symbol);
        if (price == null) {
            System.out.println("Stock not found.");
            return;
        }

        System.out.println("Current price of " + symbol + " is Rs" + price);
        System.out.print("Enter quantity: ");
        int qty = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Confirm sale? (Y/N): ");
        if (scanner.nextLine().equalsIgnoreCase("Y")) {
            tradingService.sellStock(symbol, price, qty);
        } else {
            System.out.println("Sale cancelled.");
        }
    }
}
