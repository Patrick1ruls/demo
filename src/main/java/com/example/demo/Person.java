package com.example.demo;

import java.util.Objects;

public class Person {
    private int creditScore;
    private String state;

    Person() {

    }

    public Person(int creditScore, String state) {
        this.creditScore = creditScore;
        this.state = state;
    }

    public int getCreditScore() {
        return Objects.requireNonNullElse(this.creditScore, 0);
    }

    public Person setCreditScore(int creditScore) {
        this.creditScore = creditScore;
        return this;
    }

    public String getState() {
        return Objects.requireNonNullElse(this.state, "");
    }

    public Person setState(String state) {
        this.state = state;
        return this;
    }

}
