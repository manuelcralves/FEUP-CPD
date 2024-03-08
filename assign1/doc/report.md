# **Performance evaluation of a single core**

The proposed project aims to show the effect on the processor performance of the memory hierarchy when accessing large amounts of data.

## **Index**
- **[Problem description and algorithms explanation](#problem-description-and-algorithms-explanation)**
- **[Performance metrics](#performance-metrics)**


## **Problem description and algorithms explanation**

For this study we will approach the problem, **product of two matrices** in different ways (*explained bellow*).

### **1. Basic Multiplication**

The fundamental multiplication algorithm in linear algebra generates a matrix C from two matrices A and B. This process involves multiplying the elements of the ith row in matrix A with the corresponding elements of the jth column in matrix B and summing these products.

**PseudoCode:**

#### C++

| **BASIC   MULTIPLICATION** 	| **Time(s)** 	| **L1 DCM**  	| **L2 DCM**  	|
|----------------------------	|-------------	|-------------	|-------------	|
| 600x600                    	| 0.193       	| 244747545   	| 39978950    	|
| 1000x1000                  	| 1.196       	| 1215313152  	| 248319156   	|
| 1400x1400                  	| 3.429       	| 3523398807  	| 1253135650  	|
| 1800x1800                  	| 17.836      	| 9062706413  	| 6541764536  	|
| 2200x2200                  	| 38.358      	| 17629584035 	| 20673202138 	|
| 2600x2600                  	| 68.457      	| 30874977148 	| 49650891442 	|
| 3000x3000                  	| 116.453     	| 50291188228 	| 96497809106 	|

#### JAVA

| **BASIC   MULTIPLICATION** 	| **Time(s)** 	|
|----------------------------	|-------------	|
| 600x600                    	| 0.277       	|
| 1000x1000                  	| 3.553       	|
| 1400x1400                  	| 14.564      	|
| 1800x1800                  	| 38.209      	|
| 2200x2200                  	| 69.848      	|
| 2600x2600                  	| 147.208     	|
| 3000x3000                  	| 207.720      	|

### **2. Line Multiplication**

The row multiplication method is a modified version of the standard multiplication technique, in which elements from the ith row of matrix A are multiplied by matching elements from the jth row of matrix B, with the results being compiled in the corresponding position of matrix C.

**PseudoCode:**

#### C++

| **LINE   MULTIPLICATION** 	| **Time(s)** 	| **L1 DCM** 	| **L2 DCM** 	|
|---------------------------	|-------------	|------------	|------------	|
| 600x600                   	| 0.106       	| 27111173   	| 58059476   	|
| 1000x1000                 	| 0.495       	| 125642250  	| 260780188  	|
| 1400x1400                 	| 1.598       	| 345960470  	| 702550929  	|
| 1800x1800                 	| 3.394       	| 745461447  	| 1426447348 	|
| 2200x2200                 	| 6.170        	| 2075548635 	| 2527297324 	|
| 2600x2600                 	| 10.313      	| 4413146923 	| 4179519948 	|
| 3000x3000                 	| 15.843      	| 6780661035 	| 6290745735 	|

#### JAVA

| **LINE   MULTIPLICATION** 	| **Time(s)** 	|
|---------------------------	|-------------	|
| 600x600                   	| 0.115       	|
| 1000x1000                 	| 0.510        	|
| 1400x1400                 	| 1.645       	|
| 1800x1800                 	| 3.649       	|
| 2200x2200                 	| 6.522       	|
| 2600x2600                 	| 11.625      	|
| 3000x3000                 	| 17.300        	|

### **3. Block Multiplication**

Block multiplication is an algorithm that segments matrices A and B into smaller blocks, leveraging row multiplication to compute the values for matrix C efficiently.

**PseudoCode:**

#### Block Size: 128

| **Size** 	| **Time(s)** 	| **L1 DCM**  	| **L2 DCM**  	|
|----------	|-------------	|-------------	|-------------	|
| 4096     	| 32.420       	| 9726634684  	| 32336039327 	|
| 6144     	| 113.143     	| 32816297366 	| 1.11489E+11 	|
| 8192     	| 268.534     	| 77838387177 	| 2.56855E+11 	|
| 10240    	| 577.762     	| 1.51963E+11 	| 5.01128E+11 	|

#### Block Size: 256

| **Size** 	| **Time(s)** 	| **L1 DCM**  	| **L2 DCM**  	|
|----------	|-------------	|-------------	|-------------	|
| 4096     	| 27.134      	| 9077964464  	| 23191634192 	|
| 6144     	| 96.966      	| 30637532883 	| 77563361945 	|
| 8192     	| 390.608     	| 73036003266 	| 1.58385E+11 	|
| 10240    	| 497.023     	| 1.4183E+11  	| 3.44638E+11 	|

#### Block Size: 512

| **Size** 	| **Time(s)** 	| **L1 DCM**  	| **L2 DCM**  	|
|----------	|-------------	|-------------	|-------------	|
| 4096     	| 34.222      	| 8766986850  	| 19004137434 	|
| 6144     	| 89.656      	| 29605181682 	| 66059445685 	|
| 8192     	| 329.561     	| 70347816557 	| 1.34401E+11 	|
| 10240    	| 433.028     	| 1.36924E+11 	| 3.02231E+11 	|

## **Performance metrics**

To assess the processor's performance with the specified task, we evaluated the outcomes using two distinct programming languages alongside key performance metrics.

The primary language we focused on is C++, utilizing the Performance API (PAPI) to track the number of data cache misses in both Level 1 and Level 2 caches.

For our alternative programming language, we chose Java, aiming to compare its execution time with that of C++.

To ensure the reliability of our study, we decided to collect a significant number of samples: 5 for "quick" executions and 3 for "longer" ones. Additionally, we incorporated matrices of varying sizes as outlined in the proposal.




## **Results and analysis**

### **1. Basic Multiplication**

### **2. Line Multiplication**

### **C++ vs Java:**

### **3. Block Multiplication**