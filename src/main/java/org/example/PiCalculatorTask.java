package org.example;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

public class PiCalculatorTask implements Runnable {

    private long iterations;
    private AtomicLong totalInCircleCount;

    public PiCalculatorTask(long iterations, AtomicLong totalInCircleCount) {
        this.iterations = iterations;
        this.totalInCircleCount = totalInCircleCount;
    }

    @Override
    public void run() {
        long localInCircle = 0;

        ThreadLocalRandom rand = ThreadLocalRandom.current();

        for (long i = 0; i < iterations; i++) {
            double x = rand.nextDouble(); // Випадкове число від 0 до 1 ( з крапочкою )
            double y = rand.nextDouble();

            if (x * x + y * y <= 1) {
                localInCircle++;
            }
        }

        // Ета синхронізації
        totalInCircleCount.addAndGet(localInCircle);
    }
}