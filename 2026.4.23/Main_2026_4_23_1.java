/*
题目提交链接：https://atcoder.jp/contests/abc386/tasks/abc386_e?lang=en
题目大意：
    有一个长度为 N 的非负整数数列 A，以及一个整数 K。
已知从 N 个数中选 K 个数的组合数不超过 10 的 6 次方。
你可以从 A 中选出 K 个不同的元素（只看位置不同），计算它们的异或结果。
找到所有可能的选法之中，异或结果的最大值，并且输出这个最大值。
*/
import java.util.*;

public class Main_2026_4_23_1{
    static Scanner sc = new Scanner(System.in);
    static long ans = 0;  // 存储最终的最大异或结果
    static long s = 0;    // 存储所有数的异或总和（用于优化）

    /**
     * 深度优先搜索枚举所有组合
     * @param cnt   当前已经选了多少个数
     * @param start 当前从数组的哪个位置开始选（避免重复组合）
     * @param res   当前已选数的异或结果
     * @param a     原数组
     * @param k     需要选出的个数
     * @param opt   优化标志：1表示选k个，0表示选(n-k)个并用总异或转换
     */
    static void dfs(int cnt, int start, long res, long[] a, int k, int opt) {
        // 剪枝：即使把剩余所有数都选上，也不够k个，直接返回
        if(cnt + a.length - start + 1 < k) return;
        
        // 已经选够k个数，更新答案
        if(cnt == k) {
            if(opt == 1) {
                // 直接取当前异或结果
                ans = Math.max(ans, res);
            } else {
                // 利用公式：补集异或 = 总异或 ^ 当前选的异或
                // 因为选了n-k个，相当于没选的是k个，但我们需要的是k个的异或
                // 这里res是(n-k)个的异或，那么补集(k个)的异或 = 总异或 ^ res
                ans = Math.max(ans, s ^ res);
            }
            return;
        }
        
        // 越界检查
        if(start >= a.length) return;
        
        // 枚举从当前位置开始的所有选择
        for(int i = start; i < a.length; ++i) {
            // 注意：这里第二个参数传递的是 i+1，而不是 start+1
            // 这样保证了选择的下标严格递增，不会重复
            dfs(cnt + 1, i + 1, res ^ a[i], a, k, opt);
        }
    }
    
    static void solve() {
        int n = sc.nextInt(), k = sc.nextInt();
        long[] a = new long[n];
        
        // 读入数组，并计算所有数的异或总和
        for(int i = 0; i < n; ++i) {
            a[i] = sc.nextLong();
            s ^= a[i];
        }
        
        // 关键优化：选k个等价于选n-k个，然后取补集
        // 因为 C(n, k) = C(n, n-k)，我们选择较小的那个来枚举，减少组合数
        if(k <= n - k) {
            // 直接枚举选择k个数
            dfs(0, 0, 0L, a, k, 1);
        } else {
            // 枚举选择 n-k 个数，然后通过总异或推导出 k 个数的异或
            // 公式：选k个的异或 = 总异或 ^ 选(n-k)个的异或
            dfs(0, 0, 0L, a, n - k, 0);
        }
        
        System.out.println(ans);
    }
    
    public static void main(String[] args) {
        int opt = 0, t = 1;
        if(opt == 1) t = sc.nextInt();
        while(t-- != 0)
            solve();
    }
}
