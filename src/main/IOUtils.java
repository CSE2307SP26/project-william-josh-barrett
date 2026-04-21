package main;

import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class IOUtils {
    
    private Scanner keyboardInput;
    private BankManager bank;

    public IOUtils(BankManager bank) {
        this.keyboardInput = new Scanner(System.in);
        this.bank = bank;
    }

    public int scanInt() {
        while (true) {
            try {
                int integer = keyboardInput.nextInt();
                keyboardInput.nextLine();
                return integer;
            } catch (InputMismatchException e) {
                System.out.print("Invalid integer, please try again: ");
            } catch (NoSuchElementException e) {
                System.out.print("No input detected, please try again: ");
            }
            keyboardInput.nextLine();
        }
    }

    public double scanDouble() {
        while (true) {
            try {
                double doubleValue = keyboardInput.nextDouble();
                keyboardInput.nextLine();
                return doubleValue;
            } catch (InputMismatchException e) {
                System.out.print("Invalid amount, please try again: ");
            } catch (NoSuchElementException e) {
                System.out.print("No input detected, please try again: ");
            }
            keyboardInput.nextLine();
        }
    }

    public String scanLine() {
        return keyboardInput.nextLine();
    }

    public int getUserSelection(int max) {
        int selection = -1;
        while (selection < 1 || selection >= max) {
            System.out.print("Please make a selection: ");
            selection = scanInt();
        }
        return selection;
    }

    public int selectAccount(String message) {
        bank.printAccounts();
        while (true) {
            System.out.print(message);
            int accountIndex = scanInt() - 1;
            if (accountIndex < 0 || accountIndex >= bank.getSize()) {
                continue;
            }
            return accountIndex;
        }
    }

}
