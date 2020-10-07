package com.example.demo.cosmos.primary;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.example.demo.cosmos.Student;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "students", path = "primaryStudents")
public interface PrimaryStudentRepository extends CosmosRepository<Student,String> {
    
}
