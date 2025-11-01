package com.workintech.dependencyinjection.model;

public class MidDeveloper extends Developer {
    public MidDeveloper(int id, String name, double salary) {
        super(id, name, salary, Experience.MID);
    }
}
