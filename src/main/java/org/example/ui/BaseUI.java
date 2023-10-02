package org.example.ui;

import static org.example.Main.scannerInt;
import static org.example.Main.wrongCommand;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import lombok.SneakyThrows;
import org.example.database.Database;
import org.example.student.*;
import org.example.test.*;
import org.mindrot.jbcrypt.BCrypt;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.concurrent.Executors;

import static org.example.Main.*;

public class BaseUI {
    private static final TestService testService = TestService.getInstance();
    private static final StudentService studentService = StudentService.getInstance();

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
        if (byEmail == null) {
            System.out.println("Bunaqa email topilmadi!");
            return;
        }
        boolean passwordMatches = BCrypt.checkpw(password, byEmail.getPassword());

        if (passwordMatches) {
            System.out.println("Muvafaqiyatli kirdingiz! \n");
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
                    case 1 -> CreateTest(Database.getConnection());
                    case 2 -> StartTest();
                    case 3 -> MyTests(byEmail.getId());
                    case 4 -> solvedTests();
                    case 0 -> istrue = false;
                    default -> wrongCommand();
                }
            }
        } else {
            System.out.println("Password yoki email xato kiritinggiz");
        }
    }


    @SneakyThrows
    private void solvedTests() {

    }

    @SneakyThrows
    private void MyTests(UUID studentId) {
        Student byStudentId = studentService.findByStudentId(studentId);

        List<Test> tests = testService.getAllTests();


        Connection connection = Database.getConnection();
        String query = """
                select * from test where owner_id = ?
                """;
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setObject(1, byStudentId.getId());
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Test> tests = new ArrayList<>();

        while (resultSet.next()) {
            String uuid = resultSet.getString("id");
            UUID testId = UUID.fromString(uuid);
            String title = resultSet.getString("title");
            String examType = resultSet.getString("exam_type");
            String categoryType = resultSet.getString("category_type");
            String ownerId1 = resultSet.getString("owner_id");
            UUID ownerId = UUID.fromString(ownerId1);
            Exam_Type examType1 = Exam_Type.valueOf(examType);
            Category_Type categoryType1 = Category_Type.valueOf(categoryType);

            /*Test test = new Test(testId, title, examType1, categoryType1, ownerId);*/
            /*tests.add(test);*/
        }
      
        if (tests.size() > 0) {
            int count = 1;
            for (Test test : tests) {
                System.out.println(count + ". Test name -> " + test.getTitle());
            }
            System.out.println("0.Exit");
            System.out.print("Birorta testni tanlang -> ");
            int chooseTest = scannerInt.nextInt();
            if (chooseTest != 0) {
                Test test = tests.get(chooseTest - 1);
                boolean isExited = false;
                while (!isExited) {
                    System.out.print("""
                            #. Update
                            *. Delete
                            0. Exit
                            >>\s""");
                    String s = scannerStr.nextLine();
                    switch (s) {
                        case "#" -> updateTest(test.getId(), byStudentId.getId(), test);
                        case "*" -> deleteTest(test);
                        case "0" -> isExited = true;
                        default -> wrongCommand();
                    }
                }
            }
        } else {
            System.out.println("Sizda birorta ham test mavjud emas!" + "\n");
        }
    }

    private void deleteTest(Test test) {
        testService.deleteTest(test.getId(), test.getOwnerId());
        System.out.println("Test muvaffaqiyatli o'chirildi!" + "\n");
    }

    @SneakyThrows
    private void updateTest(UUID TestId, UUID studentId, Test test) {
        String updateQuery = """
                update test set title = ?, testtype = ?::test_type_enum, testcategory = ?::testcategory_enum;
                """;
        Connection connection = Database.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);

        System.out.print("Yangi test title ni kiriting(%s) -> ".formatted(test.getTitle()));
        String newTitle = scannerStr.nextLine();

        System.out.print("Yangi test type ni kiriting(%s) -> ".formatted(test.getTestType()));
        String newTestType = scannerStr.nextLine();

        System.out.print("Yangi test category type ni kiriting(%s) -> ".formatted(test.getTestCategory()));
        String newCategoryType = scannerStr.nextLine();

        preparedStatement.setString(1, newTitle.isBlank() ? test.getTitle() : newTitle);
        preparedStatement.setString(2, newTestType.isBlank() ? String.valueOf(test.getTestType()) : newTestType);
        preparedStatement.setString(3, newCategoryType.isBlank() ? String.valueOf(test.getTestCategory()) : newCategoryType);

        preparedStatement.executeUpdate();
        if (newTitle.isBlank() && newCategoryType.isBlank() && newTestType.isBlank()) {
            System.out.println("Hech qanday o'zgartirish kiritilmadi!");
        } else {
            System.out.println("Test o'zgartirildi!" + "\n");
        }
    }


    private void StartTest() {
        
    }

    @SneakyThrows
    public void CreateTest(Connection connection) {
        System.out.print("Choose test type(REGULAR/EXAM) -> ");
        String testType = scannerStr.nextLine();

        System.out.print("Choose test category(MATH/BIOLOGY/HISTORY) -> ");
        String testCategory = scannerStr.nextLine();

        System.out.print("Enter test description: ");
        String desc = scannerStr.nextLine();

        String insertTestQuary = "INSERT INTO test(title,exam_type,category_type,owner_id) VALUES (?::uuid,?,?::exam_type_enum,?::category_type_enum,CAST(? AS UUID))";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertTestQuary)) {

            preparedStatement.setString(1, desc);
            preparedStatement.setString(2, testType);
            preparedStatement.setString(3, testCategory);
            preparedStatement.setString(4, "19790fa8-e57c-432d-884a-e59a297d62f5");

            preparedStatement.execute();
            System.out.println("Test has been created successfully.");

//            System.out.print("Enter option name: ");
//            String optionName = scannerStr.nextLine();
//
//            System.out.print("Is correct answer(T/F); ");
//            String isCorrect = scannerStr.nextLine();
//            boolean isTrue = isCorrect.equals("T");
//
//            String insertOptionQuery = "INSERT INTO option(id,option_name,is_correct)VALUES(?::uuid,?,?)";
//
//            try (PreparedStatement preparedStatement1 = connection.prepareStatement(insertOptionQuery)) {
//                preparedStatement1.setString(1, stringUUID);
//                preparedStatement1.setString(2, optionName);
//                preparedStatement1.setString(3, isCorrect);
//
//                preparedStatement1.execute();
//                System.out.println("Variantlar qabul qilindi.");
//            }

            UUID uuid = UUID.randomUUID();
            String stringUUID = uuid.toString();
            preparedStatement.setString(1, stringUUID);
            preparedStatement.setString(2, desc);
            preparedStatement.setString(3, testType);
            preparedStatement.setString(4, testCategory);
            preparedStatement.setString(5, "6f99d07d-04aa-4f23-8df9-a3a47051142e");

            preparedStatement.execute();
            /*  System.out.println("Test has been created successfully.");*/
            boolean chek = true;
            while (chek) {
                System.out.print("Enter option name: ");
                String optionName = scannerStr.nextLine();

                System.out.print("Is correct answer(T/F); ");
                String isCorrect = scannerStr.nextLine();
                boolean isTrue = false;
                if (isCorrect.equals("T")) {
                    isTrue = true;
                }

                String insertOptionQuery = "INSERT INTO option(exam_id,option_name,is_correct)VALUES(?::uuid,?,?::boolean)";

                try (PreparedStatement preparedStatement1 = connection.prepareStatement(insertOptionQuery)) {

                    preparedStatement1.setString(1, stringUUID);
                    preparedStatement1.setString(2, optionName);
                    preparedStatement1.setString(3, String.valueOf(isTrue));

                    preparedStatement1.execute();
                    System.out.println("Variantlar qabul qilindi.");
                    System.out.println("Yana varinantlar bormi(XA/YOQ)");
                    String answer = scannerStr.nextLine();
                    if(!answer.equals("XA")){
                        chek=false;
                    }
                }
            }
        }
    }

    private void signUp() throws MessagingException, IOException {
        System.out.print("Ismingizni kiriting ⇒ ");
        String name = scannerStr.nextLine();

        System.out.print("Emailingizni kiriting ⇒ ");
        String email = scannerStr.nextLine();

        Student byEmail = studentService.findByEmail(email);
        if (byEmail != null) {
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
                    System.out.print("Eng kamida 4 ta belgidan iborat yangi parol o'rnating❗" + "\n");
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
