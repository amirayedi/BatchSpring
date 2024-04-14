package com.demo.batch.processor;

import com.demo.entity.Student;
import org.springframework.batch.item.ItemProcessor;

public class StudentFilterProcessor implements ItemProcessor<Student,Student> {

    @Override
    public Student process(Student student) {
        if (student.getAge() >= 30) {
            return student;
        } else {
            return null;
        }
    }
}

