#include <stdio.h>
#include <iostream>
#include <chrono>
#include <thrust/host_vector.h>
#include <thrust/device_vector.h>
#include <thrust/sequence.h>
#include <thrust/transform.h>
#include <thrust/transform_reduce.h>

using namespace std;
using namespace std::chrono;

struct functor {
    __host__ __device__
        double operator() (const double& x) const {
        return (4.0 / (1.0 + ((x + 0.5) / 1000000000) * ((x + 0.5) / 1000000000)));
    }
};

int main() {
    long num_steps = 1000000000;
    double pi = 0.0;
    double step = 1.0 / (double)num_steps;
    functor my_functor;
    thrust::plus<double> binary_op;
    double init = 0.0;

    auto start_time = high_resolution_clock::now();

    thrust::device_vector<double> X(num_steps);
    thrust::sequence(X.begin(), X.end());

    pi = thrust::transform_reduce(X.begin(), X.end(), my_functor, init, binary_op) * step;

    auto end_time = high_resolution_clock::now();
    auto duration = duration_cast<microseconds>(end_time - start_time);

    cout << "Execition Time: " << duration.count() / 1000000.0 << " sec" << endl;
    printf("pi=%.10lf\n", pi);
    return 0;
}