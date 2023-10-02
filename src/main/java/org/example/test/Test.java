package org.example.test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Test {
    private UUID id;
    private String title;
    private TestType testType;
    private TestCategory testCategory;
    private UUID ownerId;
}
