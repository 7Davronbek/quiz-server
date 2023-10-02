package org.example.student;

import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student {
    private UUID id;
    private String name;
    private String email;
    private String password;
}
