package org.example.test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Test {
    private UUID testId;
    private String title;
    private Exam_Type examType;
    private Category_Type categoryType;
    private UUID ownerId;
}
