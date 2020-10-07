package com.example.demo.cosmos.tertiary;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.example.demo.cosmos.Student;

public interface TertiaryStudentRepository extends CosmosRepository<Student,String> {

}
