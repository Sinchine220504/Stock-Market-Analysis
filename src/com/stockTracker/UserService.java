package com.stockTracker;

import java.util.Scanner;

class UserService {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UserManager manager = new UserManager();

        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Register User");
            System.out.println("2. Login User");
            System.out.println("3. Logout");
            System.out.print("Choose an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter username: ");
                    String username = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String password = scanner.nextLine();
                    manager.registerUser(username, password);
                    System.out.println("✅ User registered successfully.");
                    break;
                case 2:
                    System.out.print("Enter username: ");
                    String loginUser = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String loginPass = scanner.nextLine();
                    boolean success = manager.loginUser(loginUser, loginPass);
                    if (success) {
                        System.out.println("✅ Login successful! You are ready to go...");
                    } else {
                        System.out.println("❌ Invalid credentials.");
                    }
                    break;
                case 3:
                    System.out.print("Are you sure you want to logout? (yes/no): ");
                    String confirm = scanner.nextLine().trim().toLowerCase();
                    if (confirm.equals("yes")) {
                        System.out.println("✅ Successfully logged out.");
                        return; // Exit the program
                    } else {
                        System.out.println("❌ Logout canceled.");
                    }
                    break;
                default:
                    System.out.println("⚠️ Invalid choice.");
            }
        }
    }
}
