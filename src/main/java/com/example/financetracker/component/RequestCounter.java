package com.example.financetracker.component;
import lombok.Getter;
import org.springframework.stereotype.Component;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Component
public class RequestCounter {
    private static final AtomicInteger counter = new AtomicInteger(0);

    public synchronized int incrementAndGet() {
        return counter.incrementAndGet();
    }

    public static synchronized int getRequestCount() {
        return counter.get();
    }
}

