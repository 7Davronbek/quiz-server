package org.example.option;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Option {
    private UUID id;
    private UUID testId;
    private String optionName;
    private boolean isCorrect;
}
