package org.example.solved_test;

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
public class SolvedTestRepository {
    private static final SolvedTestRepository solvedTestRepository = new SolvedTestRepository();

    //    GET ALL
    @SneakyThrows
    public List<SolvedTest> getAllSolvedTestsByOwnerId(UUID ownerId) {

        String solvedTestQuery = """
                select u.id, t.id, o.id
                from solved_test as st
                         inner join test t on t.id = st.test_id
                         inner join option o on st.option_id = o.id
                         inner join "user" u on u.id = st.user_id where u.id = ?::uuid;
                """;

        try (Connection connection = Database.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(solvedTestQuery);

            preparedStatement.setString(1, String.valueOf(ownerId));

            ResultSet resultSet = preparedStatement.executeQuery();

            List<SolvedTest> solvedTests = new ArrayList<>();

            while (resultSet.next()) {

                String userId = resultSet.getString("user_id");
                String testId = resultSet.getString("test_id");
                String optionId = resultSet.getString("option_id");

                SolvedTest solvedTest = new SolvedTest(UUID.fromString(userId), UUID.fromString(testId), UUID.fromString(optionId));
                solvedTests.add(solvedTest);

            }
            return solvedTests;
        }
    }

    //    GET SINGLE
//    @SneakyThrows
//    public SolvedTest getSingleSolvedTest() {
//
//    }

    public static SolvedTestRepository getInstance() {
        return solvedTestRepository;
    }
}
