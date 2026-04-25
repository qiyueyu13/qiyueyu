/*
题目提交链接：https://atcoder.jp/contests/abc344/tasks/abc344_f
题目大意：
    有一个 N 行 N 列的网格，从 (1,1) 出发，要到达 (N,N)，初始金钱为 0。
在每个格子 (i,j)，每步可以：
停在原地，金钱增加 P[i][j]
支付 R[i][j] 金钱，向右移动到 (i, j+1)
支付 D[i][j] 金钱，向下移动到 (i+1, j)
不能使金钱为负，不能移出网格。
问：在最优策略下，最少需要多少步到达 (N,N)？
约束：
2 ≤ N ≤ 80
1 ≤ 所有输入 ≤ 10^9

简明思路：
    核心想法：
    从起点到终点，可以分成若干段，每段从某个格子出发，先在该格子停留赚钱（如果钱不够），然后一口气不赚钱地移动到下一格。
    做法：
    预处理 cost[a][b]：从 a 到 b 只移动不赚钱的最小花费。
    DP 状态 (步数, 剩余钱)。
    转移：枚举上一步的格子 (k,l) → 当前格子 (i,j)，计算需要先在 (k,l) 停几次赚钱才能付清路费，更新步数和剩余钱。
    结果：取 (N,N) 的最少步数。
*/
package example;
import java.util.*;

public class Main_2026_4_21_1 {
    static Scanner sc = new Scanner(System.in);

    // 用于 DP 中保存某个格子上的最优状态
    static class Node {
        int r;       // 到达该格子时剩余的钱（取模/余数相关的表示）
        long val;    // 到达该格子所需的最少步数
        Node(int r, long val) {
            this.r = r;
            this.val = val;
        }
    }

    static long INF = 1_000_000_000_000_00L; // 一个大数表示不可达

    static void solve() {
        int n = sc.nextInt();

        // 输入 P[i][j]：在 (i,j) 停留一次能赚的钱
        int[][] p = new int[n][n];
        // 输入 R[i][j]：从 (i,j) 向右移动一次需要的花费
        int[][] r = new int[n][n - 1];
        // 输入 D[i][j]：从 (i,j) 向下移动一次需要的花费
        int[][] d = new int[n - 1][n];

        for (int i = 0; i < n; ++i)
            for (int j = 0; j < n; ++j)
                p[i][j] = sc.nextInt();

        for (int i = 0; i < n; ++i)
            for (int j = 0; j < n - 1; ++j)
                r[i][j] = sc.nextInt();

        for (int i = 0; i < n - 1; ++i)
            for (int j = 0; j < n; ++j)
                d[i][j] = sc.nextInt();

        // cost[i][j][k][l] 表示从 (i,j) 到 (k,l) 的最小花费金钱（不赚钱，只考虑移动花费）
        long[][][][] cost = new long[n][n][n][n];
        for (int i = 0; i < n; ++i)
            for (int j = 0; j < n; ++j)
                for (int k = i; k < n; ++k)
                    for (int l = j; l < n; ++l)
                        if (i == k && j == l) cost[i][j][k][l] = 0;
                        else cost[i][j][k][l] = INF;

        // 动态规划计算 cost
        // 从 (i,j) 出发，逐步向右/向下扩展到 (k,l)，记录最小总移动花费
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                for (int k = i; k < n; ++k) {
                    for (int l = j; l < n; ++l) {
                        if (k > i) // 可以从上一行向下走到当前行
                            cost[i][j][k][l] = Math.min(cost[i][j][k][l],
                                    cost[i][j][k - 1][l] + d[k - 1][l]);
                        if (l > j) // 可以从左边列向右走到当前列
                            cost[i][j][k][l] = Math.min(cost[i][j][k][l],
                                    cost[i][j][k][l - 1] + r[k][l - 1]);
                    }
                }
            }
        }

        // dp[i][j] 表示到达 (i,j) 的最优状态
        Node[][] dp = new Node[n][n];
        for (int i = 0; i < n; ++i)
            for (int j = 0; j < n; ++j)
                dp[i][j] = new Node(0, INF);

        dp[0][0].val = 0;   // 起点步数为0，剩余钱为0

        // 主 DP：从每个可能的起点 (k,l) 更新终点 (i,j)
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                for (int k = 0; k <= i; ++k) {
                    for (int l = 0; l <= j; ++l) {
                        if (i == k && j == l) continue;

                        // Cost：从 (k,l) 纯移动到 (i,j) 需要的花费（不赚钱）
                        long Cost = cost[k][l][i][j];
                        // need：在 (k,l) 还需要赚多少钱才能支付移动花费
                        // dp[k][l].r 表示到达 (k,l) 时剩余的钱
                        long need = Cost - dp[k][l].r;

                        long cnt = 0;
                        if (need > 0) {
                            // 在 (k,l) 停留赚 P[k][l] 一次，需要多少次才能凑够 need
                            cnt = (need + p[k][l] - 1) / p[k][l];
                        }

                        // tot = 总步数 = 停留次数 + 移动步数 + 到达 (k,l) 的步数
                        long tot = cnt + (i - k) + (j - l) + dp[k][l].val;

                        // 剩余的钱 = 赚的总钱 + 原有剩余 - 花费
                        int R = (int)(cnt * p[k][l] + dp[k][l].r - Cost);

                        // 如果步数更少，更新
                        if (dp[i][j].val > tot) {
                            dp[i][j].r = R;
                            dp[i][j].val = tot;
                        }
                        // 如果步数相同，选择剩余钱更多的状态（方便后续继续移动）
                        else if (dp[i][j].val == tot && dp[i][j].r < R) {
                            dp[i][j].r = R;
                        }
                    }
                }
            }
        }

        // 输出到达 (N-1, N-1) 的最少步数
        System.out.println(dp[n - 1][n - 1].val);
    }

    public static void main(String[] args) {
        int t = 1;
        while (t-- != 0) {
            solve();
        }
    }
}
