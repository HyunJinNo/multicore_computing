package com.hyunjin.study.problem1;

/*
 This program should print the following values:
 (1) execution time of each thread
 (2) program execution time
 (3) the number of 'prime numbers'
*/

public final class pc_dynamic {
    private static int NUM_THREADS = 1; // default number of threads
    private static int NUM_END = 200000; // default input
    private static int NUM_START = 1;

    public static void main(String[] args) {
        if (args.length == 2) {
            NUM_THREADS = Integer.parseInt(args[0]); // Possible number of threads: 1, 2, 4, 6, 8, 10, 12, 14, 16, 32
            NUM_END = Integer.parseInt(args[1]);
        }
        DynamicThread[] dynamicThreads = new DynamicThread[NUM_THREADS];
        int[] results = new int[NUM_THREADS];

        for (int i = 1; i <= NUM_THREADS; i++) {
            dynamicThreads[i - 1] = new DynamicThread(results, i - 1);
        }

        long startTime = System.currentTimeMillis();

        for (DynamicThread dynamicThread : dynamicThreads) {
            dynamicThread.start();
        }

        try {
            for (DynamicThread dynamicThread : dynamicThreads) {
                dynamicThread.join();
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

    public synchronized static int getWork() {
        if (NUM_START >= NUM_END) {
            return -1;
        } else {
            NUM_START += 10;
            return (NUM_START - 10);
        }
    }
}

final class DynamicThread extends Thread {
    private final int[] results;
    private final int index;

    public DynamicThread(int[] results, int index) {
        this.results = results;
        this.index = index;
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();

        while (true) {
            int startNum = pc_dynamic.getWork();
            if (startNum == -1) {
                break;
            }
            for (int i = startNum; i < startNum + 10; i++) {
                if (isPrime(i)) {
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
