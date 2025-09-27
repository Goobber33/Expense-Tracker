import java.util.Scanner;
import java.util.ArrayList;
import java.io.*;

public class ExpenseTracker {
    private static ArrayList<Expense> expenses = new ArrayList<>();
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int choice;
        loadExpenses();

        do {
            System.out.println("\n--- Expense Tracker ---");
            System.out.println("1. Add Expense");
            System.out.println("2. View All Expenses");
            System.out.println("3. Show Totals");
            System.out.println("4. Save & Exit");
            System.out.println("5. Choice: ");

            while (!sc.hasNextInt()) {
                System.out.print("Enter a number 1-4 ");
                sc.next();
            }
            choice = sc.nextInt();
            sc.nextLine();

            switch(choice) {
                case 1: addExpense(sc); break;
                case 2: viewExpenses(); break;
                case 3: showTotals(); break;
                case 4: saveExpenses(); System.out.println("Goodbye!"); break;
                default: System.out.println("Invalid choice.");
            }
        } while (choice != 4);

        sc.close();
    }

    private static void addExpense(Scanner sc) {
        System.out.print("Enter amount: ");
        while (!sc.hasNextDouble()) {
            System.out.print("Enter a valid amount: ");
            sc.next();
        }
        double amount = sc.nextDouble(); sc.nextLine();

        System.out.print("Enter category (e.g., Food, Rent, Gas): ");
        String category = sc.nextLine().trim();

        System.out.print("Enter date (YYY-MM-DD): ");
        String date = sc.nextLine().trim();

        expenses.add(new Expense(amount, category, date));
        System.out.println("Expense added!");
    }

    private static void viewExpenses() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses recorded");
            return;
        }
        for (Expense e : expenses) System.out.println(e);
    }

    private static void showTotals() {
        double total = 0;
        for (Expense e : expenses) total += e.getAmount();
        System.out.println("Total expenses: $" + String.format("%.2f", total));
    }

    private static void saveExpenses() {
        try (PrintWriter pw = new PrintWriter(new FileWriter("expenses.csv"))) {
            for (Expense e : expenses) {
                pw.println(e.getDate() + "," + e.getAmount());
            }
        } catch (IOException ex) {
            System.out.println("Error saving file" + ex.getMessage());
        }
    }

    private static void loadExpenses() {
    File f = new File("expenses.csv");
    if (!f.exists()) return;

    try (BufferedReader br = new BufferedReader(new FileReader(f))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length != 3) continue;
            String date = parts[0];
            String category = parts[1];
            double amount = Double.parseDouble(parts[2]);
            expenses.add(new Expense(amount, category, date));
        }
    } catch (IOException ex) {
        System.out.println("Error reading file: " + ex.getMessage());
    }
  }
}
