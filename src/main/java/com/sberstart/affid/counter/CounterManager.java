package com.sberstart.affid.counter;

import com.sberstart.affid.counter.entities.Counter;

import java.util.List;

public interface CounterManager {

    boolean contains(String key);

    String add();

    void increase(String key);

    Counter remove(String key);

    Counter get(String key);

    List<String> getCounterNames();

    long getSum();
}
