import java.util.Scanner;
import java.util.Arrays;

public class RouteToGateway {

        // all static variables
        static int n; // # of routers
        static int[][] matrix; // directed graph
        static int[] gateways; // Gateway router #
        static int sa; // sa router #

    public static void main(String[] args) {
        parser();
        int[][] policyGraph = policyGraph();
        int[][] transpose = transposeGraph(policyGraph);


        DijkstraRes fromSA = dijkstra(policyGraph, sa); // compute shortest distances from SA
        DijkstraRes toSA = dijkstra(transpose, sa); // compute shortest distances to SA (transpose)

        for (int i = 1; i <= n; i++) {
            if (!isGateway(i)) {
                forwardingTable(i, fromSA, toSA);
            }
        }
    }

    // parser for directed graph
    static void parser() {
        Scanner sc = new Scanner(System.in);

        n = sc.nextInt();

        matrix = new int[n + 1][n + 1];
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                matrix[i][j] = sc.nextInt();
            }
        }

        // get gateway routers
        sc.nextLine();
        String gatewayLine = sc.nextLine();
        String[] parts = gatewayLine.split("\\s+");
        gateways = new int[parts.length];

        for (int i = 0; i < parts.length; i++) {
            gateways[i] = Integer.parseInt(parts[i]);
        }

        sa = Integer.parseInt(sc.nextLine());
        sc.close();

    }

    // purpose: efficiency (solves needed results in at most 2 calls to Dijkstra's alg)
    static int[][] transposeGraph(int[][] matrix) {
        int[][] transpose = new int[n + 1][n + 1];

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                transpose[j][i] = matrix[i][j];
            }
        }

        return transpose;
    }

    // take out illegal paths and make a new matrix
    static int[][] policyGraph() {
        int[][] policy = new int[n + 1][n + 1];

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                policy[i][j] = matrix[i][j];
            }
        }

        for (int g : gateways) {
            for (int j = 1; j <= n; j++) {
                policy[g][j] = -1;
            }
            policy[g][g] = 0;
        }
        return policy;
    }

    static class DijkstraRes {
        int[] dist;
        int[] parent;

        DijkstraRes(int[] dist, int[] parent) {
            this.dist = dist;
            this.parent = parent;
        }
    }

    static DijkstraRes dijkstra(int[][] dg, int start) {
        int[] dist = new int[n + 1]; // distance array
        int[] parent = new int[n + 1]; // parent array
        boolean[] visited = new boolean[n + 1];

        // set every distance to inf (Integer.MAX_VALUE)
        Arrays.fill(dist, Integer.MAX_VALUE);
        Arrays.fill(parent, -1); // set every parent to -1

        dist[start] = 0; // starting router
        for (int count = 1; count <= n; count++) { // run dijkstra to n times
            int u = -1;
            int minDist = Integer.MAX_VALUE;

            for (int i = 1; i <= n; i++) { // find unvisted routers
                if (!visited[i] && dist[i] < minDist) {
                    minDist = dist[i];
                    u = i;
                }
            }

            if (u == -1) {
                break;
            }

            visited[u] = true;

            for (int v = 1; v <= n; v++) { // check all adjacent routers for shorter paths
                if (dg[u][v] != -1 && !visited[v]) {
                    if (dist[u] != Integer.MAX_VALUE && dist[u] + dg[u][v] < dist[v]) {
                        dist[v] = dist[u] + dg[u][v];
                        parent[v] = u;
                    }
                }
            }
        }

        return new DijkstraRes(dist, parent);
    }

    // function to check if router is a gateway router
    static boolean isGateway(int router) {
        for (int g : gateways) {
            if (g == router) {
                return true;
            }
        }
        return false;
    }

    // move datagram towards SA
    static int nextRouterToSA(int source, int[] parentToSA) {
        if (source == sa) {
            return sa;
        }

        return parentToSA[source];
    }

    // move datagram towards gateway
    static int nextRouterFromSA(int gateway, int[] parentFromSA) {
        int curr = gateway;
        int nextHop = -1;

        while (parentFromSA[curr] != -1) {
            nextHop = curr;
            curr = parentFromSA[curr];
        }

        if (curr != sa) {
            return -1;
        }

        return nextHop;
    }

    // Forwarding Table implementation
    static void forwardingTable(int source, DijkstraRes fromSA, DijkstraRes toSA) {
        System.out.println("Forwarding Table for " + source);
        System.out.println("To Cost Next Hop");

        for (int gateway : gateways) {
            int costToSA = toSA.dist[source];
            int costFromSA = fromSA.dist[gateway];

            if (costToSA == Integer.MAX_VALUE || costFromSA == Integer.MAX_VALUE) {
                System.out.println(gateway + "   -1   -1");
            } else {
                int total = costToSA + costFromSA;
                int next;

                if (source == sa) {
                    next = nextRouterFromSA(gateway, fromSA.parent);
                } else {
                    next = nextRouterToSA(source, toSA.parent);
                }

                System.out.println(gateway + "   " + total + "   " + next);
            }
        }
    }

}
