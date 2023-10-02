package org.example.option;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.example.test.TestCategory;
import org.example.test.TestType;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OptionService {
    private static final OptionService optionService = new OptionService();
    private final OptionRepository optionRepository = OptionRepository.getInstance();

    @SneakyThrows
    public void addOption(UUID testId, Option option) {
        optionRepository.addOption(testId, option);
    }

    @SneakyThrows
    public void deleteOption(UUID optionId, UUID testId) {
        optionRepository.deleteOption(optionId, testId);
    }

    @SneakyThrows
    public Option updateOption(UUID optionId, UUID testId, Option option) {
        return optionRepository.updateOption(optionId, testId, option);
    }

    @SneakyThrows
    public List<Option> getAllOptionsByTestIdAndCategory(TestCategory testCategory, TestType testType) {
        return optionRepository.getAllOptionsByTestIdAndCategory(testCategory, testType);
    }


    public static OptionService getInstance() {
        return optionService;
    }
}
