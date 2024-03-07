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


### **2. Line Multiplication**

The row multiplication method is a modified version of the standard multiplication technique, in which elements from the ith row of matrix A are multiplied by matching elements from the jth row of matrix B, with the results being compiled in the corresponding position of matrix C.

**PseudoCode:**

### **3. Block Multiplication**

Block multiplication is an algorithm that segments matrices A and B into smaller blocks, leveraging row multiplication to compute the values for matrix C efficiently.

**PseudoCode:**



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

### **4. Floating Point Operations Comparison**