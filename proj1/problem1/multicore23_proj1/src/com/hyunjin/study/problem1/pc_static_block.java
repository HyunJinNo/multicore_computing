package com.hyunjin.study.problem1;

/*
 This program should print the following values:
 (1) execution time of each thread
 (2) program execution time
 (3) the number of 'prime numbers'
*/

public final class pc_static_block {
    private static int NUM_THREADS = 1;  // default number of threads: 1
    private static int NUM_END = 200000; // default input: 200000

    public static void main(String[] args) {
        if (args.length == 2) {
            NUM_THREADS = Integer.parseInt(args[0]); // Possible number of threads: 1, 2, 4, 6, 8, 10, 12, 14, 16, 32
            NUM_END = Integer.parseInt(args[1]);
        }

        BlockThread[] blockThreads = new BlockThread[NUM_THREADS];
        int[] results = new int[NUM_THREADS];

        int workUnit = NUM_END / NUM_THREADS;
        for (int i = 1; i <= NUM_THREADS; i++) {
            int startNum = workUnit * (i - 1) + 1;
            int endNum;
            if (i == NUM_THREADS) {
                endNum = NUM_END;
            } else {
                endNum = workUnit * i;
            }
            blockThreads[i - 1] = new BlockThread(startNum, endNum, results, i - 1);
        }

        long startTime = System.currentTimeMillis();

        for (BlockThread blockThread : blockThreads) {
            blockThread.start();
        }

        try {
            for (BlockThread blockThread : blockThreads) {
                blockThread.join();
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

final class BlockThread extends Thread {
    private final int startNum;
    private final int endNum;
    private final int[] results;
    private final int index;

    public BlockThread(int startNum, int endNum, int[] results, int index) {
        this.startNum = startNum;
        this.endNum = endNum;
        this.results = results;
        this.index = index;
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();

        for (int i = startNum; i <= endNum; i++) {
            if (isPrime(i)) {
                results[index] += 1;
            }
        }

        long endTime = System.currentTimeMillis();
        long timeDiff = endTime - startTime;

        System.out.println(this.getName() + " ==> " + startNum + " ~ " + endNum + ", Execution Time: " + timeDiff + "ms");
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

