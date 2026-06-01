import java.util.*;

public class MicroserviceDFSGraph {

    static Map<String, List<String>> graph = new HashMap<>();
    static Set<String> visited = new HashSet<>();
    static List<String> entryOrder = new ArrayList<>();
    static List<String> finishOrder = new ArrayList<>();

    public static void addEdge(String from, String to) {
        graph.putIfAbsent(from, new ArrayList<>());
        graph.putIfAbsent(to, new ArrayList<>());
        graph.get(from).add(to);
    }

    public static void dfs(String service) {
        visited.add(service);
        entryOrder.add(service);

        List<String> neighbours = graph.get(service);
        Collections.sort(neighbours);

        for (String next : neighbours) {
            if (!visited.contains(next)) {
                System.out.println(service + "  --->  " + next);
                dfs(next);
            }
        }

        finishOrder.add(service);
    }

    public static void printGraph() {
        System.out.println("Microservice Call Graph:");
        System.out.println();

        List<String> services = new ArrayList<>(graph.keySet());
        Collections.sort(services);

        for (String service : services) {
            List<String> neighbours = graph.get(service);
            Collections.sort(neighbours);

            if (neighbours.isEmpty()) {
                System.out.println(service + "  --->  no outgoing calls");
            } else {
                System.out.print(service + "  --->  ");
                for (int i = 0; i < neighbours.size(); i++) {
                    System.out.print(neighbours.get(i));
                    if (i < neighbours.size() - 1) {
                        System.out.print(", ");
                    }
                }
                System.out.println();
            }
        }
    }

    public static void main(String[] args) {

        addEdge("api", "auth");
        addEdge("api", "catalog");
        addEdge("api", "cart");
        addEdge("cart", "inventory");
        addEdge("cart", "payment");
        addEdge("payment", "notif");
        addEdge("payment", "analytics");
        addEdge("payment", "ship");
        addEdge("ship", "inventory");
        addEdge("ship", "notif");
        addEdge("catalog", "inventory");
        addEdge("auth", "analytics");

        printGraph();

        System.out.println();
        System.out.println("DFS Traversal Path from api:");
        System.out.println();

        dfs("api");

        System.out.println();
        System.out.println("DFS Entry Order:");
        System.out.println(String.join(" -> ", entryOrder));

        System.out.println();
        System.out.println("DFS Finish Order / Post-Order:");
        System.out.println(String.join(" -> ", finishOrder));

        System.out.println();
        System.out.println("Downstream Services of api:");

        List<String> downstream = new ArrayList<>(entryOrder);
        downstream.remove("api");

        System.out.println(String.join(" -> ", downstream));

        System.out.println();
        System.out.println("Time Complexity:");
        System.out.println("O(V + E)");
        System.out.println("O(9 + 12) = O(21)");
    }
}