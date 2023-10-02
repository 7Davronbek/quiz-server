package org.example.ui;


import static org.example.Main.scannerInt;
import static org.example.Main.wrongCommand;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.SneakyThrows;
import org.example.database.Database;
import org.example.student.Student;
import org.example.student.StudentService;
import org.mindrot.jbcrypt.BCrypt;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Executors;

import static org.example.Main.*;

public class BaseUI {
    private static StudentService studentService = StudentService.getInstance();

    public void quizstart() throws SQLException, MessagingException, IOException {

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
                case 1 -> {
                    signUp();
                }
                case 2 -> {
                    logIn();
                }
                case 0 -> isExit = true;
                default -> wrongCommand();
            }
        }
    }


    private void logIn() throws SQLException {
        System.out.print("Emaillinggizni kiriting: ");
        String email = scannerStr.nextLine();


        System.out.print("Password kiriting: ");
        String password = scannerStr.nextLine();


        Student byEmail = studentService.findByEmail(email);
        if(byEmail==null){
            System.out.println("bunaqa email yoq");
            return;
        }
        boolean passwordMatches = BCrypt.checkpw(password, byEmail.getPassword());

        if (passwordMatches) {
            System.out.println("Muvafaqiyatli kirdinggiz");
            boolean istrue = true;
            while (istrue) {
                System.out.print("""
                        1. Create test
                        2. Start test
                        3. My tests
                        4. Solved tests
                                            
                        0. Back
                        >> \s""");
                int tanlov = scannerInt.nextInt();
                switch (tanlov) {
                    case 1:
                        CreateTest(Database.getConnection());
                    case 2:
                        StartTest();
                    case 3:
                        MyTests();
                    case 4:
                        SolvedTests();
                    case 0:
                        istrue = false;
                    default:
                        System.out.println("notogri son kiritinggiz");

                }
            }
        } else {
            System.out.println("password yoki email xato kiritinggiz");
        }
    }


    @SneakyThrows
    private void SolvedTests() {

    }

    private void MyTests() {
    }

    private void StartTest() {
    }

    @SneakyThrows
    public void CreateTest(Connection connection) {
        System.out.print("Choose test type(REGULAR/EXAM/BOTH);");
        String testType = scannerStr.nextLine();

        System.out.print("Choose test category(MATH/BIOLOGY/HISTORY);");
        String testCategory = scannerStr.nextLine();

        System.out.print("Enter test description: ");
        String desc = scannerStr.nextLine();

        String insertTestQuary = "INSERT INTO test(id,title,exam_type,category_type,owner_id) VALUES (?::uuid,?,?::exam_type_enum,?::category_type_enum,CAST(? AS UUID))";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertTestQuary)) {
            UUID uuid = UUID.randomUUID();
            String stringUUID = uuid.toString();
            preparedStatement.setString(1, stringUUID);
            preparedStatement.setString(2, desc);
            preparedStatement.setString(3, testType);
            preparedStatement.setString(4, testCategory);
            preparedStatement.setString(5, "19790fa8-e57c-432d-884a-e59a297d62f5");

            preparedStatement.execute();
           /* System.out.println("Test has been created successfully.");

            System.out.print("Enter option name: ");
            String optionName=scannerStr.nextLine();

            System.out.print("Is correct answer(T/F); ");
            String isCorrect = scannerStr.nextLine();
            boolean isTrue = isCorrect.equals("T");

            String insertOptionQuery = "INSERT INTO option(id,option_name,is_correct)VALUES(?::uuid,?,?)";

            try(PreparedStatement preparedStatement1=connection.prepareStatement(insertOptionQuery)){
                preparedStatement1.setString(1,stringUUID);
                preparedStatement1.setString(2,optionName);
                preparedStatement1.setString(3,isCorrect);

                preparedStatement1.execute();
                System.out.println("Variantlar qabul qilindi.");
            }*/
        }

    }


    private void signUp() throws MessagingException, IOException {
        System.out.print("Ismingizni kiriting ⇒ ");
        String name = scannerStr.nextLine();

        System.out.print("Emailingizni kiriting ⇒ ");
        String email = scannerStr.nextLine();


        Student byEmail = studentService.findByEmail(email);
        if (byEmail!=null) {
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
