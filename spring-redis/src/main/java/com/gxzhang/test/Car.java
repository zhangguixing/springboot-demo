package com.gxzhang.test;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * @author zhangguixing Email:zhangguixing@co-mall.com
 * @since 2021-05-24 下午 14:52
 */
public class Car implements Serializable {
    private String name;

    private Car car;

    @JsonIgnore
    User user;

    public Car(String name, Car car) {
        this.name = name;
        this.car = car;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}