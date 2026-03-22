# 🧑🏻‍💻 RouteToGateway

RouteToGateway is a Java program that reads a directed weighted graph from standard input and generates forwarding tables for all non-gateway routers. The program computes the least-cost valid routes to each gateway while enforcing a Security Agent (`SA`) routing policy.

## 🧑‍🔧 Architecture Overview

- Java program reads:
  - number of routers
  - directed weighted adjacency matrix
  - gateway router list
  - Security Agent (`SA`) router index
- A policy graph is built from the original graph
- A transpose of the policy graph is constructed
- Dijkstra’s algorithm is run twice:
  - once on the policy graph
  - once on the transpose graph
- The results are used to generate forwarding tables for every non-gateway router

## ⭐️ Features

- 📥 Standard input parsing for the full graph input
- 🧭 Directed weighted graph routing
- 🛡️ Security Agent (`SA`) policy enforcement
- 🚪 Gateway-aware forwarding table generation
- 🔁 Uses the transpose graph for efficient shortest path computation
- 📊 Prints forwarding tables for every non-gateway router
- ❌ Handles unreachable routes with `-1 -1`
- ⚡ Uses at most two runs of standard Dijkstra’s algorithm

## 📝 Requirements

- Java 17+  
  or any Java version that supports `javac` and `java`

## 📁 Files

- `RouteToGateway.java` — main program file

## ⚙️ How It Works

The program enforces the following routing policy:

- Every route to a gateway must pass through the Security Agent (`SA`)
- A gateway cannot be used as an intermediate router before reaching `SA`
- After leaving `SA`, the first gateway reached must be the destination gateway

To support this, the program builds a modified policy graph where gateway routers cannot be used as intermediate forwarding nodes.

## 📌 Full Example

Compile the program with:

```bash
javac RouteToGateway.java
```

Run the program with:

```bash
java RouteToGateway.java
```

Heres an example input of a Autonomous System (directed graph form):

```
6
0 1 10 -1 -1 2
10 0 1 -1 -1 -1
1 10 0 -1 -1 -1
-1 -1 2 0 1 10
-1 -1 -1 10 0 1
-1 -1 -1 1 10 0
2 5
6
```

- 1st line: Number of routers
- Lines 2-7: Weights to each router
- 8th line: Gateway routers
- 9th line: SA router (security agent router)

The output will include a forwarding table for each router that isn't a gateway router.
- Each row contains
    - Destination gateway
    - Total cost
    - Next hop

Here is an example output using the example input:

```
Forwarding Table for 1
To Cost Next Hop
2   7   6
5   4   6

Forwarding Table for 3
To Cost Next Hop
2   8   1
5   5   1

Forwarding Table for 4
To Cost Next Hop
2   10   3
5   7   3

Forwarding Table for 6
To Cost Next Hop
2   5   4
5   2   4
```

