package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class ParallelMonteCarloPi {

    private static final long TOTAL_ITERATIONS = 1_000_000_000L;

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Помилка: вкажіть кількість потоків як аргумент командного рядка.");
            System.err.println("Приклад: java ParallelMonteCarloPi 8");
            return;
        }

        int numThreads = 0;
        try {
            numThreads = Integer.parseInt(args[0]);
            if (numThreads <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            System.err.println("Помилка: кількість потоків має бути додатнім цілим числом.");
            return;
        }

        System.out.println("Запуск обчислень з " + numThreads + " потоками...");

        AtomicLong totalInCircle = new AtomicLong(0);

        List<Thread> threads = new ArrayList<>();

        long iterationsPerThread = TOTAL_ITERATIONS / numThreads;
        long remainingIterations = TOTAL_ITERATIONS % numThreads;

        long startTime = System.nanoTime();

        for (int i = 0; i < numThreads; i++) {
            long itersForThisThread = iterationsPerThread;
            if (i == numThreads - 1) {
                itersForThisThread += remainingIterations;
            }

            Runnable task = new PiCalculatorTask(itersForThisThread, totalInCircle);
            Thread thread = new Thread(task);
            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.err.println("Потік був перерваний: " + e.getMessage());
                Thread.currentThread().interrupt();
            }
        }

        long endTime = System.nanoTime();

        long timeElapsedNanos = endTime - startTime;
        double timeElapsedMillis = timeElapsedNanos / 1_000_000.0;

        double pi = 4.0 * totalInCircle.get() / TOTAL_ITERATIONS;

        System.out.println("!===! Розділювальна смуга =) !===!");
        System.out.printf("Значення PI: %.5f\n", pi);
        System.out.println("потоків: " + numThreads);
        System.out.printf("проведено ітерацій: %,d\n", TOTAL_ITERATIONS);
        System.out.printf("час: %.2f мс\n", timeElapsedMillis);
        System.out.println("!===! Розділювальна смуга =) !===!");
    }
}
