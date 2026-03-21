import java.util.Scanner;
public class RouteToGateway {
        // storing critical information
        static int n; // # of routers
        static int[][] matrix; // directed graph
        static int[] gateways; // Gateway router #
        static int sa; // sa router #
    public static void main(String[] args) {
        parser();
        int[][] transpose = transposeGraph();

    }
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
}
