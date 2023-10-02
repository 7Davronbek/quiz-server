package org.example.student;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.example.database.Database;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StudentRepository {
    private static final StudentRepository studentRepository = new StudentRepository();

    public static StudentRepository getInstance() {
        return studentRepository;
    }

    @SneakyThrows
    public void add(Student student) {
        String addStudentQuery = """
                insert into student (name, email, password) values (?, ?, ?);
                """;
        Connection connection = Database.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(addStudentQuery);

        String password = student.getPassword();
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        preparedStatement.setString(1, student.getName());
        preparedStatement.setString(2, student.getEmail());
        preparedStatement.setString(3, hashedPassword);
        preparedStatement.execute();
        connection.close();
        System.out.println("Student muvaffaqiyatli qo'shildi \n");
    }

    @SneakyThrows
    public void delete(UUID id) {
        String deleteStudentQuery = """
                delete from student as st where st.id = ?
                """;
        Connection connection = Database.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(deleteStudentQuery);
        preparedStatement.setObject(1, id);
        preparedStatement.execute();
        connection.close();
        System.out.println("Student muvaffaqiyatli o'chirildi! \n");
    }

    @SneakyThrows
    public Student findById(UUID id) {
        Connection connection = Database.getConnection();
        String findByIdQuery = """
                select * from student where id = ?
                """;
        PreparedStatement preparedStatement = connection.prepareStatement(findByIdQuery);
        preparedStatement.setObject(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            String string = resultSet.getString("id");
            UUID uuid = UUID.fromString(string);
            String name = resultSet.getString("name");
            String emial = resultSet.getString("email");
            String password = resultSet.getString("password");
            Student student = new Student(uuid, name, emial, password);
            System.out.println(student);
            return student;
        }
        return null;
    }

    @SneakyThrows
    public List<Student> getAll() {
        Connection connection = Database.getConnection();
        String query = """
                select * from "student";
                """;

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Student> students = new ArrayList<>();

        while (resultSet.next()) {
            String id = resultSet.getString("id");
            UUID uuid = UUID.fromString(id);
            String name = resultSet.getString("name");
            String email = resultSet.getString("email");
            String password = resultSet.getString("password");

            Student student = new Student(uuid, name, email, password);
            students.add(student);
        }

        return students;
    }

}
