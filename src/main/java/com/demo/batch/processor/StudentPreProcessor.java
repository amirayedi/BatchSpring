package com.demo.batch.processor;

import com.demo.entity.Student;
import org.springframework.batch.item.ItemProcessor;

public class StudentPreProcessor implements ItemProcessor<Student,Student> {

    @Override
    public Student process(Student student) {
        if (student.getFirstname().startsWith("A")) {
            return student;
        } else {
            return null;
        }
    }
}
