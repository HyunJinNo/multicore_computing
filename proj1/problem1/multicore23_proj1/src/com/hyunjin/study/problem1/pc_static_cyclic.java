package com.hyunjin.study.problem1;

/*
 This program should print the following values:
 (1) execution time of each thread
 (2) program execution time
 (3) the number of 'prime numbers'
*/

public final class pc_static_cyclic {
    private static int NUM_THREADS = 1;  // default number of threads
    private static int NUM_END = 200000; // default input

    public static void main(String[] args) {
        if (args.length == 2) {
            NUM_THREADS = Integer.parseInt(args[0]); // Possible number of threads: 1, 2, 4, 6, 8, 10, 12, 14, 16, 32
            NUM_END = Integer.parseInt(args[1]);
        }

        CyclicThread[] cyclicThreads = new CyclicThread[NUM_THREADS];
        int[] results = new int[NUM_THREADS];

        int workUnit = NUM_THREADS * 10;
        int endNum = NUM_END;
        for (int i = 1; i <= NUM_THREADS; i++) {
            int startNum = 10 * (i - 1) + 1;
            cyclicThreads[i - 1] = new CyclicThread(startNum, endNum, workUnit, results, i - 1);
        }

        long startTime = System.currentTimeMillis();

        for (CyclicThread cyclicThread : cyclicThreads) {
            cyclicThread.start();
        }

        try {
            for (CyclicThread cyclicThread : cyclicThreads) {
                cyclicThread.join();
            }
        } catch (Exception ignored) {}

        int answer = 0;
        for (int result : results) {
            answer += result;
        }

        long endTime = System.currentTimeMillis();
        long timeDiff = endTime - startTime;
        System.out.println("\nProgram Execution Time: " + timeDiff + "ms");
        System.out.println("1 ~ " + NUM_END + ", the number of 'prime numbers': " + answer);
    }
}

final class CyclicThread extends Thread {
    private final int startNum;
    private final int endNum;
    private final int workUnit;
    private final int[] results;
    private final int index;

    public CyclicThread(int startNum, int endNum, int workUnit, int[] results, int index) {
        this.startNum = startNum;
        this.endNum = endNum;
        this.workUnit = workUnit;
        this.results = results;
        this.index = index;
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();

        for (int i = startNum; i <= endNum; i += workUnit) {
            for (int j = 0; j < 10; j++) {
                if (isPrime(i + j)) {
                    results[index] += 1;
                }
            }
        }

        long endTime = System.currentTimeMillis();
        long timeDiff = endTime - startTime;
        System.out.println(this.getName() + " ==> Execution Time: " + timeDiff + "ms");
    }

    private boolean isPrime(int x) {
        if (x <= 1) {
            return false;
        }
        for (int i = 2; i < x; i++) {
            if (x % i == 0) {
                return false;
            }
        }
        return true;
    }
}
