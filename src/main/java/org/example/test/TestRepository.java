package org.example.test;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.example.database.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestRepository {
    private static final TestRepository testRepository = new TestRepository();

    @SneakyThrows
    public List<Test> getAllTestsByOwnerId(UUID ownerId) {
        String getAllTestsByOwnerIdQuery = """
                select *
                from test
                         inner join "user" u on test.owner_id = u.id
                where u.id = ?::uuid;
                """;
        try (Connection connection = Database.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(getAllTestsByOwnerIdQuery);

            preparedStatement.setString(1, String.valueOf(ownerId));

            ResultSet resultSet = preparedStatement.executeQuery();

            List<Test> tests = new ArrayList<>();

            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String title = resultSet.getString("title");
                String examType = resultSet.getString("testType");
                String examCategory = resultSet.getString("testCategory");
                String ownerUUID = resultSet.getString("owner_id");

                Test test = new Test(UUID.fromString(id), title, TestType.valueOf(examType), TestCategory.valueOf(examCategory), UUID.fromString(ownerUUID));
                tests.add(test);
            }
            return tests;
        }
    }

    @SneakyThrows
    public List<Test> getAllTests() {
        String getAllTestsQuery = """
                select * from test;
                """;
        try (Connection connection = Database.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(getAllTestsQuery);

            ResultSet resultSet = preparedStatement.executeQuery();

            List<Test> tests = new ArrayList<>();

            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String title = resultSet.getString("title");
                String examType = resultSet.getString("testType");
                String examCategory = resultSet.getString("testCategory");
                String ownerUUID = resultSet.getString("owner_id");

                Test test = new Test(UUID.fromString(id), title, TestType.valueOf(examType), TestCategory.valueOf(examCategory), UUID.fromString(ownerUUID));
                tests.add(test);
            }
            return tests;
        }
    }

    @SneakyThrows
    public Test getSingleTest(UUID testId) {
        String getQuery = """
                select * from test where id = ?
                """;

        try (Connection connection = Database.getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement(getQuery);

            preparedStatement.setString(1, String.valueOf(testId));
            ResultSet resultSet = preparedStatement.executeQuery();

            String id = resultSet.getString("id");
            String title = resultSet.getString("title");
            String examType = resultSet.getString("testType");
            String examCategory = resultSet.getString("testCategory");
            String ownerUUID = resultSet.getString("owner_id");
            return new Test(UUID.fromString(id), title, TestType.valueOf(examType), TestCategory.valueOf(examCategory), UUID.fromString(ownerUUID));

        }
    }

    @SneakyThrows
    public void updateTest(UUID testId, UUID ownerId, Test test) {
        String updateQuery = """
                UPDATE test SET 
                (title, exam_type, category_type) VALUES (?, ?::exam_type, ?::category_type) 
                WHERE id = ?::uuid and owner_id = ?::uuid;
                """;
        try (Connection connection = Database.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);

            preparedStatement.setString(1, test.getTitle());
            preparedStatement.setString(2, String.valueOf(test.getTestType()));
            preparedStatement.setString(3, String.valueOf(test.getTestCategory()));
            preparedStatement.setString(4, String.valueOf(testId));
            preparedStatement.setString(5, String.valueOf(ownerId));

            preparedStatement.executeUpdate();
        }
    }

    @SneakyThrows
    public void deleteTest(UUID testId, UUID ownerId) {
        String deleteTestQuery = """
                DELETE
                FROM test
                WHERE owner_id = ?::uuid
                  and id = ?::uuid;
                """;
        try (Connection connection = Database.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(deleteTestQuery);

            preparedStatement.setString(1, String.valueOf(ownerId));
            preparedStatement.setString(2, String.valueOf(testId));

            preparedStatement.executeUpdate();
        }
    }

    @SneakyThrows
    public void createTest(UUID ownerId, Test test) {
        String createTestQuery = """
                INSERT INTO test (title, exam_type, category_type, owner_id) VALUES (?, ?::exam_type, ?::category_type, ?::uuid);
                """;
        try (Connection connection = Database.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(createTestQuery);

            preparedStatement.setString(1, test.getTitle());
            preparedStatement.setString(2, String.valueOf(test.getTestType()));
            preparedStatement.setString(3, String.valueOf(test.getTestCategory()));
            preparedStatement.setString(4, String.valueOf(ownerId));

            preparedStatement.execute();
        }
    }

    public static TestRepository getInstance() {
        return testRepository;
    }

}
