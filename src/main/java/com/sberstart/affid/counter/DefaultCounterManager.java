package com.sberstart.affid.counter;

import com.sberstart.affid.counter.entities.Counter;
import org.apache.commons.lang.RandomStringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultCounterManager implements CounterManager {
    private static final int NAME_LENGTH = 10;

    private Map<String, Counter> counters = new HashMap<>();

    @Override
    public boolean contains(String key) {
        return counters.containsKey(key);
    }

    @Override
    public String add() {
        String name = getUniqueName();
        this.counters.put(name, new Counter());
        return name;
    }

    @Override
    public void increase(String key) {
        this.counters.get(key).increase();
    }

    @Override
    public Counter remove(String key) {
        return this.counters.remove(key);
    }

    @Override
    public Counter get(String key) {
        return this.counters.get(key);
    }

    @Override
    public List<String> getCounterNames() {
        return new ArrayList<>(this.counters.keySet());
    }

    @Override
    public long getSum() {
        return this.counters.values().stream().mapToLong(Counter::getValue).sum();
    }

    private String getUniqueName() {
        String name = RandomStringUtils.randomAlphanumeric(NAME_LENGTH);
        while (this.counters.containsKey(name)) {
            name = RandomStringUtils.randomAlphanumeric(NAME_LENGTH);
        }
        return name;
    }
}
