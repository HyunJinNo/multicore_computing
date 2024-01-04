#include <omp.h>
#include <stdio.h>
#include <stdbool.h>
#include <stdlib.h>

#define END_NUM 200000

bool isPrime(int x) {
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

void main(int argc, char *argv[]) {
	if (argc == 3) {
		/*
		* argv[1]: scheduling type number
		* 1: static with default chunk size
		* 2: dynamic with default chunk size
		* 3: static with chunk size 10
		* 4: dynamic with chunk size 10
		* 
		* argv[2]: number of threads
		* => 1, 2, 4, 6, 8, 10, 12, 14, 16
		*/

		int scheduling_type_number = atoi(argv[1]);
		int num_threads = atoi(argv[2]);
		int i;
		int count = 0;
		double start_time, end_time;
		omp_set_num_threads(num_threads);
		start_time = omp_get_wtime();

		switch (scheduling_type_number) {
			case 1:
				// 1: static with default chunk size
				#pragma omp parallel for reduction (+:count) schedule(static)
				for (i = 1; i <= END_NUM; i++) {
					if (isPrime(i)) {
						count++;
					}
				}
				end_time = omp_get_wtime();

				printf("The number of threads: %d\n", num_threads);
				printf("The number of 'prime numbers' from 1 to 200000: %d\n", count);
				printf("The execution time: %lfs\n", end_time - start_time);
				break;
			case 2:
				// 2: dynamic with default chunk 
				#pragma omp parallel for reduction (+:count) schedule(dynamic)
				for (i = 1; i <= END_NUM; i++) {
					if (isPrime(i)) {
						count++;
					}
				}
				end_time = omp_get_wtime();

				printf("The number of threads: %d\n", num_threads);
				printf("The number of 'prime numbers' from 1 to 200000: %d\n", count);
				printf("The execution time: %lfs\n", end_time - start_time);
				break;
			case 3:
				// 3: static with chunk size 10
				#pragma omp parallel for reduction (+:count) schedule(static, 10)
				for (i = 1; i <= END_NUM; i++) {
					if (isPrime(i)) {
						count++;
					}
				}
				end_time = omp_get_wtime();

				printf("The number of threads: %d\n", num_threads);
				printf("The number of 'prime numbers' from 1 to 200000: %d\n", count);
				printf("The execution time: %lfs\n", end_time - start_time);
				break;
			case 4:
				// 4: dynamic with chunk size 10
				#pragma omp parallel for reduction (+:count) schedule(dynamic, 10)
				for (i = 1; i <= END_NUM; i++) {
					if (isPrime(i)) {
						count++;
					}
				}
				end_time = omp_get_wtime();

				printf("The number of threads: %d\n", num_threads);
				printf("The number of 'prime numbers' from 1 to 200000: %d\n", count);
				printf("The execution time: %lfs\n", end_time - start_time);
				break;
			default:
				printf("Scheduling type number should be 1, 2, 3, or 4.\n");
		}
	}
	else {
		printf("This program needs only two parameters\n");
	}
}