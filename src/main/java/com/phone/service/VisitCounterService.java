package com.phone.service;

import org.springframework.stereotype.Service;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class VisitCounterService {

    private final AtomicLong counter = new AtomicLong(0);

    public void increment() {
        counter.incrementAndGet();
    }

    public Long getCounter() {
        return counter.get();
    }

    public void reset() {
        counter.set(0);
    }
}