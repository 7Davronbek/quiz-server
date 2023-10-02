package org.example;

import org.example.ui.BaseUI;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static Scanner scannerInt = new Scanner(System.in);
    public static Scanner scannerStr = new Scanner(System.in);

    public static void main(String[] args) throws SQLException {
        new BaseUI().quizStart();
    }

    public static void wrongCommand() {
        System.out.println("Wrong command. Try again");
    }
}