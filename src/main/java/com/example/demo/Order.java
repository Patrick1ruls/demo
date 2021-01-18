package com.example.demo;

import java.util.Objects;

public class Order {
    private Product product;
    private Person person;

    Order() {
        this.product = new Product();
        this.person = new Person();
    }

    Order(Product product, Person person) {
        this.product = product;
        this.person = person;
    }

    public Product getProduct() {
        return Objects.requireNonNullElse(this.product, new Product());
    }

    public Order setProduct(Product product) {
        this.product = product;
        return this;
    }

    public Person getPerson() {
        return Objects.requireNonNullElse(this.person, new Person());
    }

    public Order setPerson(Person person) {
        this.person = person;
        return this;
    }
}
