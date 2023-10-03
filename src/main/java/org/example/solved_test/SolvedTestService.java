package org.example.solved_test;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SolvedTestService {
    private static final SolvedTestService solvedTestService = new SolvedTestService();
    private static final SolvedTestRepository solvedTestRepository = SolvedTestRepository.getInstance();

    @SneakyThrows
    public List<SolvedTest> getAllSolvedTestsByOwnerId(UUID ownerId) {
        return solvedTestRepository.getAllSolvedTestsByOwnerId(ownerId);
    }

    public static SolvedTestService getInstance() {
        return solvedTestService;
    }
}
