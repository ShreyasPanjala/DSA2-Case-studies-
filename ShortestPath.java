import java.util.*;

class Edge {
    int src, dest, wt;

    Edge(int s, int d, int w) {
        src = s;
        dest = d;
        wt = w;
    }
}

public class ShortestPath {
    static final int INF = 999999;

    static void dijkstra(int[][] graph, int source) {
        int n = graph.length;
        int[] dist = new int[n];
        boolean[] visited = new boolean[n];

        Arrays.fill(dist, INF);
        dist[source] = 0;

        for (int i = 0; i < n - 1; i++) {
            int u = -1, min = INF;

            for (int v = 0; v < n; v++) {
                if (!visited[v] && dist[v] < min) {
                    min = dist[v];
                    u = v;
                }
            }

            if (u == -1)
                break;

            visited[u] = true;

            for (int v = 0; v < n; v++) {
                if (graph[u][v] != 0 && !visited[v]
                        && dist[u] + graph[u][v] < dist[v]) {
                    dist[v] = dist[u] + graph[u][v];
                }
            }
        }

        System.out.println("Dijkstra distances: " + Arrays.toString(dist));
    }

    static void bellmanFord(List<Edge> edges, int v, int source) {
        int[] dist = new int[v];

        Arrays.fill(dist, INF);
        dist[source] = 0;

        for (int i = 1; i <= v - 1; i++) {
            for (Edge e : edges) {
                if (dist[e.src] != INF
                        && dist[e.src] + e.wt < dist[e.dest]) {
                    dist[e.dest] = dist[e.src] + e.wt;
                }
            }
        }

        for (Edge e : edges) {
            if (dist[e.src] != INF
                    && dist[e.src] + e.wt < dist[e.dest]) {
                System.out.println("Negative cycle detected");
                return;
            }
        }

        System.out.println("Bellman-Ford distances: " + Arrays.toString(dist));
    }

    static void floydWarshall(int[][] dist) {
        int n = dist.length;

        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (dist[i][k] != INF && dist[k][j] != INF
                            && dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                    }
                }
            }
        }

        for (int i = 0; i < n; i++) {
            if (dist[i][i] < 0) {
                System.out.println("Negative cycle detected");
                return;
            }
        }

        System.out.println("Floyd-Warshall distance matrix calculated");
    }

    public static void main(String[] args) {
        int[][] graph = {
                {0, 4, 0, 0},
                {0, 0, 5, 0},
                {0, 0, 0, 3},
                {0, 0, 0, 0}
        };

        List<Edge> edges = new ArrayList<>();

        edges.add(new Edge(0, 1, 4));
        edges.add(new Edge(1, 2, -2));
        edges.add(new Edge(2, 3, 3));
        edges.add(new Edge(0, 3, 10));

        int[][] matrix = {
                {0, 4, INF, 10},
                {INF, 0, -2, INF},
                {INF, INF, 0, 3},
                {INF, INF, INF, 0}
        };

        dijkstra(graph, 0);
        bellmanFord(edges, 4, 0);
        floydWarshall(matrix);

        System.out.println("Program Executed Successfully");
    }
}