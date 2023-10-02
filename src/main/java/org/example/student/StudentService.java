package org.example.student;

import java.util.List;
import java.util.UUID;

public class StudentService {
    private static final StudentRepository studentRepository = StudentRepository.getInstance();
    public static final StudentService studentService = new StudentService();

    public void addStudent(Student entity) {
        studentRepository.add(entity);
    }

    public void deleteStudent(UUID id) {
        studentRepository.delete(id);
    }

    public Student findByStudentId(UUID id) {
        return studentRepository.findById(id);
    }

    public List<Student> getAllStudent() {
        return studentRepository.getAll();
    }

    public static StudentService getInstance() {
        return studentService;
    }

    public Student findByEmail(String email) {
        return studentRepository.findByEmail(email);
    }

//    public Student findStudentByNameAndSurname(String name, String surname) {
//        List<Student> allStudent = getAllStudent();
//        for (Student student : allStudent) {
//            if (student.getName().equals(name) && student.getSurname().equals(surname)) {
//                return student;
//            }
//        }
//        return null;
//    }

}
