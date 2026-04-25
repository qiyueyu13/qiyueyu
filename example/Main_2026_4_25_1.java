/*
题目提交链接：https://atcoder.jp/contests/abc362/tasks/abc362_e
题目大意：
    给定长度为 N 的整数序列 A1, A2, ..., AN。
对于每个 k = 1 到 N，计算长度为 k 的子序列中，是等差数列的数量。
子序列不要求连续，只需保持原顺序。
即使数值相同，只要选出的下标集合不同，就算不同子序列。
结果对 998244353 取模。
N ≤ 80，Ai ≤ 10^9。
*/


package example;

import java.util.*;

public class Main_2026_4_25_1 {
    static Scanner sc = new Scanner(System.in);
    static final int MOD = 998244353;  // 模数
    
    static void solve() {
        int n = sc.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; ++i) a[i] = sc.nextInt();
        
        // ========== 第一步：离散化公差 ==========
        // 因为公差范围很大（-1e9 ~ 1e9），但最多只有 n*(n-1)/2 种不同的公差
        // 所以用 map 将每个不同的公差映射到一个整数索引 (0, 1, 2, ...)
        int idx = 0;  // 不同公差的数量
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < i; ++j) {
                int d = a[i] - a[j];  // 计算公差
                if (!map.containsKey(d)) {
                    map.put(d, idx++);  // 为新公差分配索引
                }
            }
        }
        
        // ========== 第二步：定义 DP 数组 ==========
        // dp[i][j][p] 表示：以原序列中第 i 个元素（1-based）结尾，长度为 j 的等差数列中，
        // 公差对应的索引为 p 的子序列数量
        // 注意：第三维大小是 idx（不同公差的种类数）
        long[][][] dp = new long[n + 1][n + 1][Math.max(1, idx)];  // n+1 是为了 1-based 索引方便
        
        // ========== 第三步：初始化（长度为 1 的子序列） ==========
        // 任何单个元素本身就是一个长度为 1 的等差数列
        // 这里长度 j=1，对于任意公差索引 p，dp[i][1][p] = 1
        // 理解为：长度为 1 的序列可以看作公差任意（占位），所以所有公差索引都有贡献
        for (int i = 1; i <= n; ++i) {
            for (int j = 0; j < idx; ++j) {
                dp[i][1][j] = 1;
            }
        }
        
        // ========== 第四步：答案数组 ==========
        // ans[k] 表示长度为 k 的等差数列子序列的总数
        long[] ans = new long[n + 1];
        ans[1] = n;  // 长度为 1 的子序列有 n 个（每个元素本身）
        
        // ========== 第五步：DP 递推（计算 dp[i][j][p]） ==========
        // 状态转移：对于以 i 结尾、长度为 j 的等差数列，考虑它的倒数第二个元素是 k（k < i）
        // 公差 d = a[i] - a[k]，记 p = map.get(d)
        // 那么所有以 k 结尾、长度为 j-1、公差为 p 的等差数列，后面加上 a[i] 就得到以 i 结尾、长度 j、公差 p 的等差数列
        // 所以转移方程：dp[i][j][p] += dp[k][j-1][p]
        for (int i = 1; i <= n; ++i) {           // i：当前结尾位置（1-based）
            for (int j = 2; j <= i; ++j) {       // j：子序列长度（至少为 2）
                for (int k = 1; k < i; ++k) {    // k：倒数第二个元素的位置（1-based）
                    int d = a[i - 1] - a[k - 1]; // 计算公差（注意数组是 0-based）
                    int pos = map.get(d);        // 获取公差对应的索引
                    dp[i][j][pos] = (dp[i][j][pos] + dp[k][j - 1][pos]) % MOD;
                }
            }
        }
        
        // ========== 第六步：汇总答案 ==========
        // 对所有结尾位置 i 和所有公差索引 p，将 dp[i][j][p] 累加到 ans[j] 中
        for (int i = 1; i <= n; ++i) {
            for (int j = 2; j <= i; ++j) {
                for (int k = 0; k < idx; ++k) {
                    ans[j] = (ans[j] + dp[i][j][k]) % MOD;
                }
            }
        }
        
        // ========== 第七步：输出结果 ==========
        for (int i = 1; i <= n; ++i) {
            System.out.print(ans[i] + " ");
        }
        System.out.println();
    }
    
    public static void main(String[] args) {
        // opt 变量用于控制是否多组测试用例，这里固定为 0 表示单组测试
        int opt = 0, t = 1;
        if (opt == 1) t = sc.nextInt();  // 如果 opt=1，则读取测试用例组数
        while (t-- != 0)
            solve();
    }
}
