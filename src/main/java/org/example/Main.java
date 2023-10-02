package org.example;

import lombok.SneakyThrows;
import org.example.ui.BaseUI;

import jakarta.mail.MessagingException;
import org.example.student.StudentService;
import org.example.ui.BaseUI;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static Scanner scannerInt = new Scanner(System.in);
    public static Scanner scannerStr = new Scanner(System.in);

    public static StudentService studentService=StudentService.getInstance();

    public static void main(String[] args) throws SQLException, MessagingException, IOException {
        new BaseUI().quizstart();
    }

    public static void wrongCommand() {
        System.out.println("Wrong command. Try again");
    }

}