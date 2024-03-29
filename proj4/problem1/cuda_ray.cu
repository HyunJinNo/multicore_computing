#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <time.h>
#include <math.h>
#include <chrono>
#include <iostream>
#include "cuda_runtime.h"
#include "device_launch_parameters.h"

using namespace std;
using namespace std::chrono;

#define SPHERES 20
#define rnd( x ) (x * rand() / RAND_MAX)
#define INF 2e10f
#define DIM 2048

struct Sphere {
	float r, b, g;
	float radius;
	float x, y, z;
	float hit(float ox, float oy, float* n) {
		float dx = ox - x;
		float dy = oy - y;
		if (dx * dx + dy * dy < radius * radius) {
			float dz = sqrtf(radius * radius - dx * dx - dy * dy);
			*n = dz / sqrtf(radius * radius);
			return dz + z;
		}
		return -INF;
	}
};

__global__ void kernel(Sphere* s, unsigned char* ptr) {
	int x = threadIdx.x + blockIdx.x * blockDim.x;
	int y = threadIdx.y + blockIdx.y * blockDim.y;
	int offset = x + y * DIM;
	float ox = (x - DIM / 2);
	float oy = (y - DIM / 2);

	//printf("x:%d, y:%d, ox:%f, oy:%f\n", x, y, ox, oy);

	int i;
	float r = 0, g = 0, b = 0;
	float maxz = -INF;

	for (i = 0; i < SPHERES; i++) {
		float n;
		//float t = s[i].hit(ox, oy, &n);
		float t;
		float dx = ox - s[i].x;
		float dy = oy - s[i].y;
		float radius = s[i].radius;
		if (dx * dx + dy * dy < radius * radius) {
			float dz = sqrtf(radius * radius - dx * dx - dy * dy);
			n = dz / sqrtf(radius * radius);
			t = dz + s[i].z;
		}
		else {
			t = -INF;
		}
		if (t > maxz) {
			float fscale = n;
			r = s[i].r * fscale;
			g = s[i].g * fscale;
			b = s[i].b * fscale;
			maxz = t;
		}
	}

	ptr[offset * 4 + 0] = (int)(r * 255);
	ptr[offset * 4 + 1] = (int)(g * 255);
	ptr[offset * 4 + 2] = (int)(b * 255);
	ptr[offset * 4 + 3] = 255;
}

void ppm_write(unsigned char* bitmap, int xdim, int ydim, FILE* fp) {
	int i, x, y;
	fprintf(fp, "P3\n");
	fprintf(fp, "%d %d\n", xdim, ydim);
	fprintf(fp, "255\n");

	for (y = 0; y < ydim; y++) {
		for (x = 0; x < xdim; x++) {
			i = x + y * xdim;
			fprintf(fp, "%d %d %d ", bitmap[4 * i], bitmap[4 * i + 1], bitmap[4 * i + 2]);
		}
		fprintf(fp, "\n");
	}
}

int main() {
	int i;
	Sphere* temp_s;
	Sphere* d_temp_s;
	unsigned char* bitmap;
	unsigned char* d_bitmap;
	int size1 = sizeof(Sphere) * SPHERES;
	int size2 = sizeof(unsigned char) * DIM * DIM * 4;
	FILE* fp = fopen("result.ppm", "w");
	srand(time(NULL));

	// Allocate space for device copies of temp_s and bitmap
	cudaMalloc((void**)&d_temp_s, size1);
	cudaMalloc((void**)&d_bitmap, size2);

	// Allocate space for host copies of temp_s and bitmap
	temp_s = (Sphere*)malloc(size1);
	bitmap = (unsigned char*)malloc(size2);

	// Setup initial values
	for (i = 0; i < SPHERES; i++) {
		temp_s[i].r = rnd(1.0f);
		temp_s[i].g = rnd(1.0f);
		temp_s[i].b = rnd(1.0f);
		temp_s[i].x = rnd(2000.0f) - 1000;
		temp_s[i].y = rnd(2000.0f) - 1000;
		temp_s[i].z = rnd(2000.0f) - 1000;
		temp_s[i].radius = rnd(200.0f) + 40;
	}

	auto start_time = high_resolution_clock::now();

	// Copy values to device
	cudaMemcpy(d_temp_s, temp_s, size1, cudaMemcpyHostToDevice);

	// Setup the execution configuration
	dim3 dimBlock(32, 32, 1);
	dim3 dimGrid(64, 64, 1);

	kernel<<<dimGrid, dimBlock>>>(d_temp_s, d_bitmap);

	// Copy result back to host
	cudaMemcpy(bitmap, d_bitmap, size2, cudaMemcpyDeviceToHost);

	auto end_time = high_resolution_clock::now();
	auto duration = duration_cast<microseconds>(end_time - start_time);

	ppm_write(bitmap, DIM, DIM, fp);

	fclose(fp);
	free(bitmap);
	free(temp_s);
	cudaFree(d_bitmap);
	cudaFree(d_temp_s);

	cout << "CUDA ray tracing: " << duration.count() / 1000000.0 << " sec" << endl;
	cout << "[result.ppm] was generated." << endl;
	return 0;
}