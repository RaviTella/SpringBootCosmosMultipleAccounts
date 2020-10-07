package com.example.demo.cosmos.secondary;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.example.demo.cosmos.Student;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "students", path = "secondaryStudents")
public interface SecondaryStudentRepository extends CosmosRepository<Student,String> {
}
