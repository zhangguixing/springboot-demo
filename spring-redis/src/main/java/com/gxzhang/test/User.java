package com.gxzhang.test;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.io.Serializable;

public class User implements Serializable {

    private Car car;

    public User() {
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }
}