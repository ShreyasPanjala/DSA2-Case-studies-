import java.util.*;

public class KnapsackDP {
    static List<Character> knapsack01(int[] weights, int[] values, int W) {
        int n = weights.length;
        int[][] dp = new int[n + 1][W + 1];

        for (int i = 1; i <= n; i++) {
            for (int w = 1; w <= W; w++) {
                if (weights[i - 1] <= w) {
                    dp[i][w] = Math.max(
                            values[i - 1] + dp[i - 1][w - weights[i - 1]],
                            dp[i - 1][w]
                    );
                } else {
                    dp[i][w] = dp[i - 1][w];
                }
            }
        }

        List<Character> chosen = new ArrayList<>();

        int w = W;

        for (int i = n; i >= 1; i--) {
            if (dp[i][w] != dp[i - 1][w]) {
                chosen.add(
                        (char) ('A' + i - 1));

                w -= weights[i - 1];
            }
        }

        Collections.reverse(chosen);

        System.out.println(
                "Maximum Value = " + dp[n][W]);

        return chosen;
    }

    public static void main(String[] args) {
        int[] weights = {5, 8, 3, 10, 4, 6, 7, 2};

        int[] values = {40, 50, 20, 70, 30, 35, 45, 15};

        int W = 24;

        List<Character> result = knapsack01(weights, values, W);

        System.out.println(
                "Selected Items: " + result);
    }
}