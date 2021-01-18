package com.example.demo;

import java.util.Objects;

public class Product {
    private String name;
    private double interestRate = 5.0;
    private boolean disqualified;

    Product() {

    }

    public Product(String name, double interestRate) {
        this.name = name;
        this.interestRate = interestRate;
    }

    public String getName() {
        return Objects.requireNonNullElse(this.name, "");
    }

    public Product setName(String name) {
        this.name = name;
        return this;
    }

    public double getInterestRate() {
        return Objects.requireNonNullElse(this.interestRate, 5.0);
    }

    public Product setInterestRate(double interestRate) {
        this.interestRate = interestRate;
        return this;
    }

    public void addToInterestRate(double value) {
        this.interestRate += value;
    }

    public boolean isDisqualified() {
        return Objects.requireNonNullElse(this.disqualified, false);
    }

    public Product setDisqualified(boolean disqualified) {
        this.disqualified = disqualified;
        return this;
    }

}
