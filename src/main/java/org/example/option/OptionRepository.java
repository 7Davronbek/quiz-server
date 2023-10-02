package org.example.option;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.example.database.Database;
import org.example.test.Test;
import org.example.test.TestCategory;
import org.example.test.TestType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OptionRepository {
    private static final OptionRepository optionRepository = new OptionRepository();

    @SneakyThrows
    public void addOption(UUID testId, Option option) {

        String addOptionQuery = """
                INSERT INTO option (exam_id, option_name, is_correct) VALUES (?::uuid, ?, ?::boolean);
                """;

        try (Connection connection = Database.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(addOptionQuery);

            preparedStatement.setString(1, String.valueOf(testId));
            preparedStatement.setString(2, option.getOptionName());
            preparedStatement.setString(3, String.valueOf(option.isCorrect()));

            preparedStatement.execute();
        }

    }

    @SneakyThrows
    public void deleteOption(UUID optionId, UUID testId) {

        String deleteOptionQuery = """
                DELETE FROM option WHERE id = ?::uuid and exam_id = ?::uuid;
                """;

        try (Connection connection = Database.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(deleteOptionQuery);
            preparedStatement.setString(1, String.valueOf(optionId));
            preparedStatement.setString(2, String.valueOf(testId));
            preparedStatement.executeUpdate();
        }

    }

    @SneakyThrows
    public Option updateOption(UUID optionId, UUID testId, Option option) {

        String updateOptionQuery = """
                UPDATE option SET (option_name, is_correct) VALUES (?, ?) WHERE id = ?::uuid AND exam_id = ?::uuid;
                """;

        try (Connection connection = Database.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(updateOptionQuery);

            preparedStatement.setString(1, option.getOptionName());
            preparedStatement.setString(2, String.valueOf(option.isCorrect()));
            preparedStatement.setString(3, String.valueOf(optionId));
            preparedStatement.setString(4, String.valueOf(testId));

            preparedStatement.executeUpdate();
        }
        return null;
    }

    @SneakyThrows
    public List<Option> getAllOptionsByTestIdAndCategory(TestCategory testCategory, TestType testType) {
        String selectAllOptionQuery = """
                select title, exam_type, category_type, option_name, is_correct
                from test
                         join option o on test.id = o.exam_id
                where category_type = ?::category_type and exam_type = ?::exam_type;
                """;

//        String selectAllOptionQuery = " SELECT * FROM option o WHERE o.exam_id = ?;";

        try (Connection connection = Database.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(selectAllOptionQuery);

            preparedStatement.setString(1, String.valueOf(testCategory));
            preparedStatement.setString(2, String.valueOf(testType));

            ResultSet resultSet = preparedStatement.executeQuery();
            List<Option> options = new ArrayList<>();

            while (resultSet.next()) {

                String optionName = resultSet.getString("option_name");
                boolean isCorrect = resultSet.getBoolean("is_correct");
                String id = resultSet.getString("id");
                String examId = resultSet.getString("exam_id");

                Option option = new Option(UUID.fromString(id), UUID.fromString(examId), optionName, isCorrect);
                options.add(option);
            }
            return options;
        }
    }

    public static OptionRepository getInstance() {
        return optionRepository;
    }
}
