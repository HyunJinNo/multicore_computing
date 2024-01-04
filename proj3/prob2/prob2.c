#include <omp.h>
#include <stdio.h>
#include <stdlib.h>

#define NUM_STEPS 10000000

void main(int argc, char *argv[]) {
	if (argc == 4) {
		/*
		* argv[1]: scheduling type number
		* 1: static
		* 2: dynamic
		* 3: guided
		* 
		* argv[2]: chunk size
		* => 1, 5, 10, 100
		* 
		* argv[3]: number of threads
		* => 1, 2, 4, 6, 8, 10, 12, 14, 16
		*/
		
		int scheduling_type_number = atoi(argv[1]);
		int chunk_size = atoi(argv[2]);
		int num_threads = atoi(argv[3]);
		long i;
		double step = 1.0 / (double) NUM_STEPS;
		double x, pi, sum = 0.0;
		double start_time, end_time;
		omp_set_num_threads(num_threads);
		start_time = omp_get_wtime();

		printf("Chunk size: %d\n", chunk_size);
		printf("The number of threads: %d\n", num_threads);

		switch (scheduling_type_number) {
			case 1:
				// static
				#pragma omp parallel for reduction (+:sum) schedule(static, chunk_size)
				for (i = 0; i < NUM_STEPS; i++) {
					x = (i + 0.5) * step;
					sum = sum + 4.0 / (1.0 + x * x);
				}

				pi = step * sum;

				end_time = omp_get_wtime();
				printf("pi = %.24lf\n", pi);
				printf("The execution Time : %lfms\n", end_time - start_time);
				break;
			case 2:
				// dynamic
				#pragma omp parallel for reduction (+:sum) schedule(dynamic, chunk_size)
				for (i = 0; i < NUM_STEPS; i++) {
					x = (i + 0.5) * step;
					sum = sum + 4.0 / (1.0 + x * x);
				}

				pi = step * sum;

				end_time = omp_get_wtime();
				printf("pi = %.24lf\n", pi);
				printf("The execution Time : %lfms\n", end_time - start_time);
				break;
			case 3:
				// guided
				#pragma omp parallel for reduction (+:sum) schedule(guided, chunk_size)
				for (i = 0; i < NUM_STEPS; i++) {
					x = (i + 0.5) * step;
					sum = sum + 4.0 / (1.0 + x * x);
				}

				pi = step * sum;

				end_time = omp_get_wtime();
				printf("pi = %.24lf\n", pi);
				printf("The execution Time : %lfms\n", end_time - start_time);
				break;
			default:
				printf("Scheduling type number should be 1, 2, or 3.\n");
		}
	}
	else {
		printf("This program needs only three parameters.\n");
	}
}