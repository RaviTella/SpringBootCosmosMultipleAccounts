package com.example.demo.cosmos;

import com.example.demo.cosmos.primary.PrimaryStudentRepository;
import com.example.demo.cosmos.secondary.SecondaryStudentRepository;
import com.example.demo.cosmos.tertiary.TertiaryStudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class Test {
    private final PrimaryStudentRepository primarystudentRepository;
    private final SecondaryStudentRepository secondaryStudentRepository;
    private final TertiaryStudentRepository tertiaryStudentRepository;

    @Autowired
    public Test(PrimaryStudentRepository primarystudentRepository, SecondaryStudentRepository secondaryStudentRepository, TertiaryStudentRepository tertiaryStudentRepository) {
        this.primarystudentRepository = primarystudentRepository;
        this.secondaryStudentRepository = secondaryStudentRepository;
        this.tertiaryStudentRepository = tertiaryStudentRepository;
    }

    @PostConstruct
    void load(){
        loadPrimaryStudentCollection();
        loadSecondaryStudentCollection();
        loadTertiaryStudentCollection();
        printData();
    }

    private void loadPrimaryStudentCollection() {
        Student s1 = new Student();
        s1.setName("Student1");
        primarystudentRepository.save(s1);

    }

    private void loadSecondaryStudentCollection() {
        Student s1 = new Student();
        s1.setName("Student2");
        secondaryStudentRepository.save(s1);
    }

    private void loadTertiaryStudentCollection() {
        Student s1 = new Student();
        s1.setName("Student3");
        tertiaryStudentRepository.save(s1);
    }

    private void printData(){
        primarystudentRepository.findAll().forEach(System.out::println);
        secondaryStudentRepository.findAll().forEach(System.out::println);
        tertiaryStudentRepository.findAll().forEach(System.out::println);
    }

}
