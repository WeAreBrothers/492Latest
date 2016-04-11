package com.example.robien.beachbuddy;

/**
 * Created by Robien on 3/18/2016.
 */
public class Student {

    private String name, email, assocClassName, assocClassNum, instructor;

    public Student(String name, String email, String assocClassName, String assocClassNum, String instructor) {
        this.setName(name);
        this.setEmail(email);
        this.setClassName(assocClassName);
        this.setClassNum(assocClassNum);
        this.setInstructor(instructor);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getClassName() {
        return assocClassName;
    }

    public void setClassName(String assocClassName) {
        this.assocClassName = assocClassName;
    }

    public String getClassNum() {
        return assocClassNum;
    }

    public void setClassNum(String assocClassNum) {
        this.assocClassNum = assocClassNum;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }
}
