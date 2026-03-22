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
        int[][] transpose = transposeGraph();

        DijkstraRes fromSA = dijkstra(matrix, sa); // compute shortest distances from SA
        DijkstraRes toSA = dijkstra(transpose, sa); // compute shortest distances to SA (transpose)

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
    static int[][] transposeGraph() {
        int[][] transpose = new int[n + 1][n + 1];

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                transpose[j][i] = matrix[i][j];
            }
        }

        return transpose;
    }

    static class DijkstraRes {
        int[] dist;
        int[] succ;

        DijkstraRes(int[] dist, int[] succ) {
            this.dist = dist;
            this.succ = succ;
        }
    }

    static DijkstraRes dijkstra(int[][] dg, int start) {
        int[] dist = new int[n + 1]; // distance array
        int[] succ = new int[n + 1]; // successor array
        boolean[] visited = new boolean[n + 1];

        // set every distance to inf (Integer.MAX_VALUE)
        Arrays.fill(dist, Integer.MAX_VALUE);
        Arrays.fill(succ, -1); // set every successor to -1

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
                        succ[v] = u;
                    }
                }
            }
        }

        return new DijkstraRes(dist, succ);
    }
}
