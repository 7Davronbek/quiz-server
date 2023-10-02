package org.example.ui;


import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.SneakyThrows;
import org.example.student.Student;
import org.example.student.StudentService;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Executors;

import static org.example.Main.*;

public class BaseUI {
    private static final StudentService studentService = StudentService.getInstance();

    public void quizStart() {
        boolean isExit = false;
        while (!isExit) {
            System.out.print("""
                    1. Sign up
                    2. Log in
                                        
                    0. Exit
                    >> \s""");
            int command = scannerInt.nextInt();
            switch (command) {
                case 1 -> signUp();
                case 2 -> logIn();
                case 0 -> isExit = true;
                default -> wrongCommand();
            }
        }
    }

    private void logIn() {
        new StudentUI().start();
    }

    @SneakyThrows
    private void signUp() {
        System.out.print("Ismingizni kiriting ⇒ ");
        String name = scannerStr.nextLine();

        System.out.print("Emailingizni kiriting ⇒ ");
        String email = scannerStr.nextLine();

        boolean byEmail = studentService.findByEmail(email);
        if (byEmail) {
            System.out.println("Bu email allaqachon ro'yxatdan o'tgan❗");
        } else {
            Random random = new Random();
            int randomNumber = random.nextInt(100000, 999999);
            sendToEmail(Message.RecipientType.TO, new InternetAddress(email), randomNumber);
            System.out.println("Emailingizga tasdiqlash kodi yuborildi❗");
            System.out.print("Tasdiqlash kodini kiriting ⇒ ");
            int verificationCode = scannerInt.nextInt();

            if (verificationCode == randomNumber) {
                System.out.print("\nEng kamida 4 ta belgidan iborat yangi parol o'rnating ⇒ ");
                String password = scannerStr.nextLine();
                if (password.length() < 4) {
                    System.out.print("Eng kamida 4 ta belgidan iborat yangi parol o'rnating ⇒ ");
                } else {
                    String trimPassword = password.trim();
                    studentService.addStudent(new Student(UUID.randomUUID(), name, email, trimPassword));
                    System.out.println("Muvaffaqiyatli ro'yxatdan o'tdingiz❗\n");
                }
            } else {
                System.out.println("Tasdiqlash kodini notog'ri kiritdingiz❗ Qaytadan urinib ko'ring ↻." + "\n");
            }
        }
    }

    public void sendToEmail(Message.RecipientType recipientType, InternetAddress internetAddress, int random) throws IOException, MessagingException, MessagingException {
        Properties properties = new Properties();
        properties.load(new FileReader("src/main/resources/application.properties"));
        String mail = properties.getProperty("mail.gmail");
        String password = properties.getProperty("myMail.password");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mail, password);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(mail));
        message.addRecipient(recipientType, internetAddress);
        message.setText(String.valueOf(random));

        Executors.newCachedThreadPool().submit(() -> {
            try {
                Transport.send(message);
            } catch (MessagingException e) {
                throw new RuntimeException();
            }
        });

    }

}
