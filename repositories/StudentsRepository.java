package com.company.repositories;

import com.company.task.Student;

import java.util.List;

public interface StudentsRepository extends CrudRepository<Student> {
    List<Student> findAllByAge(int age);
}
