package org.example.ui;


import lombok.SneakyThrows;
import org.example.database.Database;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

import static org.example.Main.*;

public class BaseUI {
    public void quizStart() throws SQLException {
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


        String password1 = "123";
        String hashedPassword = BCrypt.hashpw(password1, BCrypt.gensalt());


        String providedPassword = password;
        boolean passwordMatches = BCrypt.checkpw(providedPassword, hashedPassword);

        if (passwordMatches) {
            System.out.println("Muvafaqiyatli kirdinggiz");
            boolean istrue=true;
            while (istrue){
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
                        istrue=false;
                    default:
                        System.out.println("notogri son kiritinggiz");

                }
            }
        } else {
            System.out.println("oxshamadi");
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
            preparedStatement.setString(5, "e2ad8e28-6d67-4d9c-848c-1552b10cc6e3");

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


    private void signUp() {

    }
}
