package org.example.solved_test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SolvedTest {
    private UUID userId;
    private UUID testId;
    private UUID optionId;
}
