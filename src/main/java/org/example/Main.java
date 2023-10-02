package org.example;

import lombok.SneakyThrows;
import org.example.ui.BaseUI;

import java.util.Scanner;

public class Main {
    public static Scanner scannerInt = new Scanner(System.in);
    public static Scanner scannerStr = new Scanner(System.in);

    @SneakyThrows
    public static void main(String[] args) {
        new BaseUI().quizStart();
    }

    public static void wrongCommand() {
        System.out.println("Wrong command. Try again");
    }
}