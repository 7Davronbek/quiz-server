package org.example.test;

import lombok.SneakyThrows;

import java.util.List;
import java.util.UUID;

public class TestService {
    private static final TestService testService = new TestService();
    private final TestRepository testRepository = TestRepository.getInstance();

    @SneakyThrows
    public List<Test> getAllTestsByOwnerId(UUID ownerId) {
        return testRepository.getAllTestsByOwnerId(ownerId);
    }

    @SneakyThrows
    public List<Test> getAllTests() {
        return testRepository.getAllTests();
    }

    @SneakyThrows
    public Test getSingleTest(UUID testId) {
        return testRepository.getSingleTest(testId);
    }

    @SneakyThrows
    public void updateTest(UUID testId, UUID ownerId, Test test) {
        testRepository.updateTest(testId, ownerId, test);
    }

    @SneakyThrows
    public void deleteTest(UUID testId, UUID ownerId) {
        testRepository.deleteTest(testId, ownerId);
    }

    @SneakyThrows
    public void createTest(UUID ownerId, Test test) {
        testRepository.createTest(ownerId, test);
    }

    public static TestService getInstance() {
        return testService;
    }
}
