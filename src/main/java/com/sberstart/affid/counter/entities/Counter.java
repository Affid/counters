package com.sberstart.affid.counter.entities;

public class Counter {

    private Integer value = 0;

    public void increase() {
        this.value++;
    }

    public Integer getValue() {
        return this.value;
    }
}
