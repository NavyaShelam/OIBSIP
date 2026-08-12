import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class ATM {
    private Bank bank;
    private Scanner scanner;
    private Account currentAccount;

    // Constants for 110-character box drawing receipt layout
    private static final int TOTAL_WIDTH = 110;
    private static final int INNER_WIDTH = 108; // 110 minus left and right borders
    private static final int COL1_W = 6;  // S.No
    private static final int COL2_W = 22; // Transaction
    private static final int COL3_W = 26; // Amount
    private static final int COL4_W = 20; // Status
    private static final int COL5_W = 28; // Date & Time

    public ATM(Bank bank) {
        this.bank = bank;
        this.scanner = new Scanner(System.in);
    }

    private void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException ignored) {
        }
    }

    public void start() {
        showStartupScreen();
        if (!insertCardSimulation()) {
            return;
        }

        if (!authenticateUserSession()) {
            return;
        }

        showMainMenuLoop();
        terminateSession();
    }

    private void showStartupScreen() {
        System.out.println("============================================================");
        System.out.println("                    JAVA ATM");
        System.out.println("               SECURE BANKING SYSTEM");
        System.out.println("============================================================");
        System.out.println();
        System.out.println("Initializing ATM...");
        sleep(300);
        System.out.println("Checking system...");
        sleep(300);
        System.out.println("Connecting to banking network...");
        sleep(400);
        System.out.println();
        System.out.println("SYSTEM READY");
        System.out.println();
        System.out.println("============================================================");
        System.out.println();
    }

    private boolean insertCardSimulation() {
        System.out.println("Please press ENTER to insert your card...");
        scanner.nextLine();

        System.out.println("------------------------------------------------------------");
        System.out.println("CARD DETECTED");
        System.out.println("------------------------------------------------------------");
        System.out.println();
        System.out.println("Reading card...");
        sleep(300);
        System.out.println("Verifying card...");
        sleep(300);
        System.out.println();
        System.out.println("Card accepted.");
        System.out.println();
        System.out.println("------------------------------------------------------------");
        System.out.println();
        return true;
    }

    private boolean authenticateUserSession() {
        int attempts = 0;
        final int MAX_ATTEMPTS = 3;

        while (attempts < MAX_ATTEMPTS) {
            System.out.println("============================================================");
            System.out.println("                     USER LOGIN");
            System.out.println("============================================================");
            System.out.println();
            System.out.print("Enter User ID:\n> ");
            String userId = scanner.nextLine().trim();

            System.out.print("\nEnter PIN:\n> ");
            String pin;
            if (System.console() != null) {
                char[] pinChars = System.console().readPassword();
                pin = new String(pinChars).trim();
            } else {
                pin = scanner.nextLine().trim();
            }

            System.out.println("\nAuthenticating...");
            sleep(400);

            currentAccount = bank.authenticateUser(userId, pin);

            if (currentAccount != null) {
                System.out.println();
                System.out.println("============================================================");
                System.out.println("             AUTHENTICATION SUCCESSFUL");
                System.out.println("============================================================");
                System.out.println();
                System.out.println("Account verified.");
                System.out.println("Secure session established.");
                System.out.println();
                System.out.println("Welcome to Java ATM.");
                System.out.println();
                System.out.println("============================================================");
                sleep(400);
                return true;
            } else {
                attempts++;
                System.out.println("\nInvalid User ID or PIN.");
                if (attempts < MAX_ATTEMPTS) {
                    System.out.println("Attempts remaining: " + (MAX_ATTEMPTS - attempts));
                    System.out.println();
                }
            }
        }

        System.out.println();
        System.out.println("============================================================");
        System.out.println("                    ACCESS DENIED");
        System.out.println("============================================================");
        System.out.println();
        System.out.println("Maximum authentication attempts exceeded.");
        System.out.println();
        System.out.println("Your ATM session has been terminated.");
        System.out.println();
        System.out.println("Please contact your bank if you require assistance.");
        System.out.println();
        System.out.println("============================================================");
        return false;
    }

    private void showMainMenuLoop() {
        boolean running = true;

        while (running) {
            System.out.println();
            System.out.println("============================================================");
            System.out.println("                    JAVA ATM");
            System.out.println("               SECURE BANKING SYSTEM");
            System.out.println("============================================================");
            System.out.println();
            System.out.println("                    MAIN MENU");
            System.out.println();
            System.out.println("------------------------------------------------------------");
            System.out.println();
            System.out.println("   [1]  Cash Withdrawal");
            System.out.println("   [2]  Cash Deposit");
            System.out.println("   [3]  Fund Transfer");
            System.out.println("   [4]  Transaction History");
            System.out.println("   [5]  Balance Inquiry");
            System.out.println("   [6]  Exit / Eject Card");
            System.out.println();
            System.out.println("------------------------------------------------------------");
            System.out.println();
            System.out.print("Please select an option:\n> ");

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    handleCashWithdrawal();
                    running = askAnotherTransaction();
                    break;
                case "2":
                    handleCashDeposit();
                    running = askAnotherTransaction();
                    break;
                case "3":
                    handleFundTransfer();
                    running = askAnotherTransaction();
                    break;
                case "4":
                    handleTransactionHistory();
                    running = askAnotherTransaction();
                    break;
                case "5":
                    handleBalanceInquiry();
                    running = askAnotherTransaction();
                    break;
                case "6":
                    running = false;
                    break;
                default:
                    System.out.println("\nInvalid selection. Please choose an available option.");
                    sleep(300);
            }
        }
    }

    private void handleBalanceInquiry() {
        System.out.println();
        System.out.println("============================================================");
        System.out.println("                    BALANCE INQUIRY");
        System.out.println("============================================================");
        System.out.println();
        System.out.println("Processing request...");
        sleep(300);
        System.out.println("Verifying account...");
        sleep(300);
        System.out.println();
        System.out.println("------------------------------------------------------------");
        System.out.println();
        System.out.printf("Account Number    : %s%n", currentAccount.getMaskedAccountId());
        System.out.printf("Available Balance : ₹%,.2f%n", currentAccount.getBalance());
        System.out.println();
        System.out.println("------------------------------------------------------------");

        // Record balance inquiry transaction for receipt tracking
        currentAccount.addTransaction(new Transaction("BALANCE ENQUIRY", currentAccount.getBalance(), "Balance Inquiry", currentAccount.getBalance(), "SUCCESS"));
    }

    private void handleCashWithdrawal() {
        System.out.println();
        System.out.println("============================================================");
        System.out.println("                  CASH WITHDRAWAL");
        System.out.println("============================================================");
        System.out.println();
        System.out.println("Please enter the amount you would like to withdraw.");
        System.out.println();
        System.out.println("Enter 0 to cancel.");
        System.out.println();
        System.out.print("Amount:\n₹ > ");

        double amount = parseAmountInput();
        if (amount < 0) {
            return;
        }

        if (amount == 0) {
            System.out.println("\nTransaction cancelled.");
            return;
        }

        System.out.println();
        System.out.println("------------------------------------------------------------");
        System.out.println("                 PROCESSING REQUEST");
        System.out.println("------------------------------------------------------------");
        System.out.println();
        System.out.println("Checking available funds...");
        sleep(300);
        System.out.println("Verifying account...");
        sleep(300);
        System.out.println("Authorizing transaction...");
        sleep(300);
        System.out.println("Processing cash...");
        sleep(300);
        System.out.println();
        System.out.println("Please wait...");
        sleep(300);
        System.out.println();
        System.out.println("------------------------------------------------------------");

        if (currentAccount.getBalance() < amount) {
            System.out.println();
            System.out.println("============================================================");
            System.out.println("               TRANSACTION DECLINED");
            System.out.println("============================================================");
            System.out.println();
            System.out.println("Insufficient Funds.");
            System.out.println();
            System.out.println("The requested amount cannot be withdrawn.");
            System.out.println();
            System.out.println("No money has been deducted.");
            System.out.println();
            System.out.println("------------------------------------------------------------");
            return;
        }

        if (currentAccount.withdraw(amount)) {
            System.out.println();
            System.out.println("============================================================");
            System.out.println("              TRANSACTION APPROVED");
            System.out.println("============================================================");
            System.out.println();
            System.out.println("Please collect your cash.");
            System.out.println();
            System.out.println("------------------------------------------------------------");
            System.out.println();
            System.out.printf("Amount Withdrawn : ₹%,.2f%n", amount);
            System.out.printf("Remaining Balance: ₹%,.2f%n", currentAccount.getBalance());
            System.out.println();
            System.out.println("------------------------------------------------------------");
            System.out.println();
            System.out.println("Transaction completed successfully.");
        }
    }

    private void handleCashDeposit() {
        System.out.println();
        System.out.println("============================================================");
        System.out.println("                   CASH DEPOSIT");
        System.out.println("============================================================");
        System.out.println();
        System.out.println("Please enter the amount you would like to deposit.");
        System.out.println();
        System.out.println("Enter 0 to cancel.");
        System.out.println();
        System.out.print("Amount:\n₹ > ");

        double amount = parseAmountInput();
        if (amount < 0) {
            return;
        }

        if (amount == 0) {
            System.out.println("\nTransaction cancelled.");
            return;
        }

        System.out.println();
        System.out.println("------------------------------------------------------------");
        System.out.println("                 PROCESSING DEPOSIT");
        System.out.println("------------------------------------------------------------");
        System.out.println();
        System.out.println("Verifying amount...");
        sleep(300);
        System.out.println("Updating account...");
        sleep(300);
        System.out.println("Updating balance...");
        sleep(300);
        System.out.println();
        System.out.println("------------------------------------------------------------");

        if (currentAccount.deposit(amount)) {
            System.out.println();
            System.out.println("============================================================");
            System.out.println("                DEPOSIT SUCCESSFUL");
            System.out.println("============================================================");
            System.out.println();
            System.out.printf("Amount Deposited : ₹%,.2f%n", amount);
            System.out.printf("Updated Balance  : ₹%,.2f%n", currentAccount.getBalance());
            System.out.println();
            System.out.println("------------------------------------------------------------");
            System.out.println();
            System.out.println("Transaction completed successfully.");
        }
    }

    private void handleFundTransfer() {
        System.out.println();
        System.out.println("------------------------------------------------------------");
        System.out.println("                    FUND TRANSFER");
        System.out.println("------------------------------------------------------------");
        System.out.println();
        System.out.print("Enter recipient User ID or Account Number:\n> ");
        String recipientId = scanner.nextLine().trim();

        if (recipientId.isEmpty()) {
            System.out.println("\nPlease enter a valid User ID or Account Number.");
            return;
        }

        if (recipientId.equals("0")) {
            System.out.println("\nTransfer cancelled.");
            return;
        }

        Account recipient = bank.findAccountByUserId(recipientId);
        if (recipient == null) {
            recipient = bank.findAccountByNumber(recipientId);
        }

        if (recipient == null) {
            System.out.println();
            System.out.println("============================================================");
            System.out.println("                 TRANSFER DECLINED");
            System.out.println("============================================================");
            System.out.println();
            System.out.println("Recipient account not found.");
            System.out.println();
            System.out.println("Please verify the account number and try again.");
            System.out.println();
            System.out.println("No money has been transferred.");
            System.out.println();
            System.out.println("============================================================");
            return;
        }

        if (recipient.getAccountId().equalsIgnoreCase(currentAccount.getAccountId())) {
            System.out.println();
            System.out.println("============================================================");
            System.out.println("                  INVALID TRANSFER");
            System.out.println("============================================================");
            System.out.println();
            System.out.println("You cannot transfer money to your own account.");
            System.out.println();
            System.out.println("No money has been transferred.");
            System.out.println();
            System.out.println("============================================================");
            return;
        }

        System.out.println();
        System.out.println("✓ Recipient Account Found");
        System.out.printf("  Recipient      : %s%n", recipient.getUserId());
        System.out.printf("  Account Number : %s%n", recipient.getMaskedAccountId());
        System.out.println();
        System.out.print("Enter amount to transfer: ₹\n> ");

        double amount = parseTransferAmountInput();
        if (amount <= 0) {
            return;
        }

        if (currentAccount.getBalance() < amount) {
            System.out.println();
            System.out.println("============================================================");
            System.out.println("                 TRANSFER DECLINED");
            System.out.println("============================================================");
            System.out.println();
            System.out.println("Insufficient balance.");
            System.out.println();
            System.out.println("No money has been transferred.");
            System.out.println();
            System.out.println("============================================================");
            return;
        }

        // Transfer Confirmation Screen
        System.out.println();
        System.out.println("------------------------------------------------------------");
        System.out.println("                 TRANSFER CONFIRMATION");
        System.out.println("------------------------------------------------------------");
        System.out.println();
        System.out.printf("From Account : %s%n", currentAccount.getMaskedAccountId());
        System.out.printf("To Account   : %s%n", recipient.getMaskedAccountId());
        System.out.printf("Amount       : ₹%,.2f%n", amount);
        System.out.println();
        System.out.print("Confirm transfer? (Y/N):\n> ");

        String confirmChoice = scanner.nextLine().trim();
        if (!confirmChoice.equalsIgnoreCase("Y") && !confirmChoice.equalsIgnoreCase("YES") && !confirmChoice.equals("1")) {
            System.out.println();
            System.out.println("============================================================");
            System.out.println("                 TRANSFER CANCELLED");
            System.out.println("============================================================");
            System.out.println();
            System.out.println("No money has been transferred.");
            System.out.println();
            System.out.println("============================================================");
            return;
        }

        System.out.println();
        System.out.println("------------------------------------------------------------");
        System.out.println("                 PROCESSING TRANSFER");
        System.out.println("------------------------------------------------------------");
        System.out.println();
        System.out.println("Verifying recipient...");
        sleep(300);
        System.out.println("Checking available funds...");
        sleep(300);
        System.out.println("Authorizing transaction...");
        sleep(300);
        System.out.println("Transferring funds...");
        sleep(300);
        System.out.println();
        System.out.println("Please wait...");
        sleep(300);
        System.out.println();
        System.out.println("------------------------------------------------------------");

        if (currentAccount.transfer(recipient, amount)) {
            System.out.println();
            System.out.println("TRANSFER SUCCESSFUL");
            System.out.println("------------------------------------------------------------");
        }
    }

    private void handleTransactionHistory() {
        System.out.println();
        currentAccount.printTransactionHistory();
    }

    private double parseAmountInput() {
        try {
            String input = scanner.nextLine().trim();
            double amount = Double.parseDouble(input);
            if (amount < 0) {
                System.out.println("\nInvalid input. Amount cannot be negative.");
                return -1;
            }
            return amount;
        } catch (NumberFormatException e) {
            System.out.println("\nInvalid input. Please enter a valid numerical amount.");
            return -1;
        }
    }

    private double parseTransferAmountInput() {
        try {
            String input = scanner.nextLine().trim();
            double amount = Double.parseDouble(input);
            if (amount <= 0) {
                System.out.println("\nPlease enter a valid transfer amount.");
                return -1;
            }
            return amount;
        } catch (NumberFormatException e) {
            System.out.println("\nPlease enter a valid transfer amount.");
            return -1;
        }
    }

    private boolean askAnotherTransaction() {
        System.out.println();
        System.out.println("------------------------------------------------------------");
        System.out.println();
        System.out.println("Transaction completed.");
        System.out.println();
        System.out.println("Would you like to perform another transaction?");
        System.out.println();
        System.out.println("[1] Yes");
        System.out.println("[2] No");
        System.out.println();
        System.out.print("Select:\n> ");

        String choice = scanner.nextLine().trim();
        return choice.equals("1") || choice.equalsIgnoreCase("Y");
    }

    private void terminateSession() {
        System.out.println();
        System.out.println("============================================================");
        System.out.println("                    PRINT RECEIPT?");
        System.out.println("============================================================");
        System.out.println();
        System.out.println("[1] Yes");
        System.out.println("[2] No");
        System.out.println();
        System.out.print("Select:\n> ");

        String receiptChoice = scanner.nextLine().trim();
        if (receiptChoice.equals("1") || receiptChoice.equalsIgnoreCase("Y")) {
            printReceipt();
        }

        System.out.println();
        System.out.println("============================================================");
        System.out.println("                 ENDING ATM SESSION");
        System.out.println("============================================================");
        System.out.println();
        System.out.println("Please wait...");
        sleep(400);
        System.out.println();
        System.out.println("Preparing card ejection...");
        sleep(400);
        System.out.println();
        System.out.println("------------------------------------------------------------");
        System.out.println();
        System.out.println("PLEASE TAKE YOUR CARD.");
        System.out.println();
        System.out.println("CARD EJECTED SUCCESSFULLY.");
        System.out.println();
        System.out.println("------------------------------------------------------------");
        System.out.println();
        System.out.println("Thank you for using Java ATM.");
        System.out.println();
        System.out.println("Please make sure you have collected your card and cash.");
        System.out.println();
        System.out.println("============================================================");
        System.out.println("                 SESSION TERMINATED");
        System.out.println("============================================================");
        System.out.println();
        System.out.println("Goodbye!");
    }

    // =========================================================================
    // FULL-WIDTH EXCEL-STYLE BOX DRAWING RECEIPT IMPLEMENTATION
    // =========================================================================

    public void printReceipt() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        String currentDate = now.format(dateFormatter);
        String currentTime = now.format(timeFormatter);
        String maskedAcc = currentAccount != null ? currentAccount.getMaskedAccountId() : "****0000";

        System.out.println();
        // Top border
        System.out.println("┌" + repeatChar('─', INNER_WIDTH) + "┐");

        // Centered Header
        printBoxLine(centerText("JAVA ATM", INNER_WIDTH));
        printBoxLine(centerText("SECURE BANKING SYSTEM", INNER_WIDTH));
        printBoxLine(centerText("ATM TRANSACTION RECEIPT", INNER_WIDTH));

        // Separator below header
        printFullSeparator();

        // Upper Section: Account Number, Date, Time
        String metaText = String.format(" Account Number : %-15s Date : %-15s Time : %-15s", maskedAcc, currentDate, currentTime);
        printBoxLine(padRight(metaText, INNER_WIDTH));

        // Table Header Separator
        printTableHeaderSeparator();

        // Table Column Names
        printTableHeader();

        // Table Header Bottom Separator
        printTableRowSeparator();

        // Transaction Data Rows
        ArrayList<Transaction> txList = currentAccount != null ? currentAccount.getTransactions() : new ArrayList<>();
        Transaction lastTransferTx = null;

        if (txList.isEmpty()) {
            // Default Balance Inquiry line if no other transactions performed
            printTransactionRow(1, "BALANCE ENQUIRY", String.format("₹%,.2f", currentAccount != null ? currentAccount.getBalance() : 0.0), "SUCCESS", currentDate + " " + currentTime);
            printTableBottomSeparator();
        } else {
            int sno = 1;
            for (Transaction t : txList) {
                String amountStr = String.format("₹%,.2f", t.getAmount());
                printTransactionRow(sno++, t.getType(), amountStr, t.getStatus(), t.getFormattedDateTime());
                if (t.getFromAccountMasked() != null && t.getToAccountMasked() != null) {
                    lastTransferTx = t;
                }
            }
            printTableBottomSeparator();
        }

        // If transfer transaction present, print From Account / To Account rows
        if (lastTransferTx != null) {
            printBoxLine(padRight(" From Account : " + lastTransferTx.getFromAccountMasked(), INNER_WIDTH));
            printBoxLine(padRight(" To Account   : " + lastTransferTx.getToAccountMasked(), INNER_WIDTH));
            printFullSeparator();
        }

        // Footer Messages
        printBoxLine(centerText("TRANSACTION COMPLETED SUCCESSFULLY", INNER_WIDTH));
        printBoxLine(centerText("THANK YOU FOR USING JAVA ATM", INNER_WIDTH));

        // Bottom Border
        System.out.println("└" + repeatChar('─', INNER_WIDTH) + "┘");
        System.out.println();
    }

    private void printBoxLine(String content) {
        System.out.println("│" + padRight(content, INNER_WIDTH) + "│");
    }

    private void printFullSeparator() {
        System.out.println("├" + repeatChar('─', INNER_WIDTH) + "┤");
    }

    private void printTableHeaderSeparator() {
        System.out.println("├" + repeatChar('─', COL1_W)
                + "┬" + repeatChar('─', COL2_W)
                + "┬" + repeatChar('─', COL3_W)
                + "┬" + repeatChar('─', COL4_W)
                + "┬" + repeatChar('─', COL5_W) + "┤");
    }

    private void printTableHeader() {
        String c1 = centerText("S.No", COL1_W);
        String c2 = centerText("Transaction", COL2_W);
        String c3 = centerText("Amount", COL3_W);
        String c4 = centerText("Status", COL4_W);
        String c5 = centerText("Date & Time", COL5_W);
        System.out.println("│" + c1 + "│" + c2 + "│" + c3 + "│" + c4 + "│" + c5 + "│");
    }

    private void printTableRowSeparator() {
        System.out.println("├" + repeatChar('─', COL1_W)
                + "┼" + repeatChar('─', COL2_W)
                + "┼" + repeatChar('─', COL3_W)
                + "┼" + repeatChar('─', COL4_W)
                + "┼" + repeatChar('─', COL5_W) + "┤");
    }

    private void printTableBottomSeparator() {
        System.out.println("├" + repeatChar('─', COL1_W)
                + "┴" + repeatChar('─', COL2_W)
                + "┴" + repeatChar('─', COL3_W)
                + "┴" + repeatChar('─', COL4_W)
                + "┴" + repeatChar('─', COL5_W) + "┤");
    }

    private void printTransactionRow(int sno, String type, String amount, String status, String dateTimeStr) {
        String c1 = centerText(String.valueOf(sno), COL1_W);
        String c2 = padRight(" " + type, COL2_W);
        String c3 = padLeft(amount + " ", COL3_W);
        String c4 = centerText(status, COL4_W);
        String c5 = centerText(dateTimeStr, COL5_W);
        System.out.println("│" + c1 + "│" + c2 + "│" + c3 + "│" + c4 + "│" + c5 + "│");
    }

    // Helper string methods
    private String centerText(String text, int width) {
        if (text == null) text = "";
        if (text.length() >= width) {
            return text.substring(0, width);
        }
        int leftPadding = (width - text.length()) / 2;
        int rightPadding = width - text.length() - leftPadding;
        return repeatChar(' ', leftPadding) + text + repeatChar(' ', rightPadding);
    }

    private String padRight(String text, int width) {
        if (text == null) text = "";
        if (text.length() >= width) {
            return text.substring(0, width);
        }
        return text + repeatChar(' ', width - text.length());
    }

    private String padLeft(String text, int width) {
        if (text == null) text = "";
        if (text.length() >= width) {
            return text.substring(0, width);
        }
        return repeatChar(' ', width - text.length()) + text;
    }

    private String repeatChar(char ch, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(ch);
        }
        return sb.toString();
    }
}
