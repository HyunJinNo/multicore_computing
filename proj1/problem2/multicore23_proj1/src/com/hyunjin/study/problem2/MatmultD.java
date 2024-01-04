package com.hyunjin.study.problem2;

import java.util.*;
import java.lang.*;

/*
 This program should print the following values:
 (1) execution time of each thread
 (2) execution time when using all threads
 (3) sum of all elements in the resulting matrix
*/

public final class MatmultD {
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int thread_no = 1;
        if (args.length == 1) {
            thread_no = Integer.parseInt(args[0]); // Possible number of threads: 1, 2, 4, 6, 8, 10, 12, 14, 16, 32
        }

        int[][] a = readMatrix();
        int[][] b = readMatrix();

        if (a.length == 0) {
            System.out.println("Invalid Input.");
            return;
        }
        if (a[0].length != b.length) {
            System.out.println("Invalid Dimension.");
            return;
        }

        int[][] c = new int[a.length][b[0].length];
        MatrixThread[] matrixThreads = new MatrixThread[thread_no];

        int endRow = a.length;
        int cycle = thread_no;
        for (int i = 1; i <= thread_no; i++) {
            int startRow = i - 1;
            matrixThreads[i - 1] = new MatrixThread(a, b, c, startRow, endRow, cycle);
        }

        long startTime = System.currentTimeMillis();

        for (MatrixThread matrixThread : matrixThreads) {
            matrixThread.start();
        }

        try {
            for (MatrixThread matrixThread : matrixThreads) {
                matrixThread.join();
            }
        } catch (Exception ignored) {}

        long endTime = System.currentTimeMillis();
        long timeDiff = endTime - startTime;

        printMatrix(c);
        System.out.printf("\n[thread_no]:%2d , [Time]:%4d ms\n", thread_no, timeDiff);
    }

    public static int[][] readMatrix() {
        int rows = sc.nextInt();
        int cols = sc.nextInt();
        int[][] result = new int[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] = sc.nextInt();
            }
        }
        return result;
    }

    public static void printMatrix(int[][] mat) {
        System.out.println("\nMatrix[" + mat.length + "][" + mat[0].length + "]");
        int cols = mat[0].length;
        int sum = 0;
        for (int[] ints : mat) {
            for (int j = 0; j < cols; j++) {
                //System.out.printf("%4d " , ints[j]);
                sum += ints[j];
            }
            //System.out.println();
        }
        //System.out.println();
        System.out.println("Matrix Sum = " + sum + "\n");
    }
}

class MatrixThread extends Thread {
    private final int[][] a; // a[m][n]
    private final int[][] b; // b[n][p]
    private final int[][] c; // c[m][p]
    private final int startRow;
    private final int endRow;
    private final int cycle;

    public MatrixThread(int[][] a, int[][] b, int[][] c, int startRow, int endRow, int cycle) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.startRow = startRow;
        this.endRow = endRow;
        this.cycle = cycle;
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();

        for (int i = startRow; i < endRow; i += cycle) {
            for (int j = 0; j < c[0].length; j++) {
                for (int k = 0; k < a[0].length; k++) {
                    c[i][j] += a[i][k] * b[k][j];
                }
            }
        }

        long endTime = System.currentTimeMillis();
        long timeDiff = endTime - startTime;
        System.out.println(this.getName() + " ==> Execution Time: " + timeDiff + "ms");
    }
}