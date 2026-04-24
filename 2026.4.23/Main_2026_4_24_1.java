/*
题目提交链接：https://atcoder.jp/contests/abc385/tasks/abc385_e
题目大意：
        给你一棵树，你可以删掉一些顶点和边。
    剩下的图形必须是一个雪花树。雪花树的形状是：
    有一个中心点，中心点连着 x 个中间点，每个中间点再连着 y 个叶子。
    x 和 y 都是至少为 1 的正整数。问最少需要删掉多少个顶点。题目保证一定可以做到。
*/
import java.util.*;

public class Main_2026_4_24_1 {
    static Scanner sc = new Scanner(System.in);
    static List<Integer>[] adj;   // 邻接表，存储树结构
    static int ans = Integer.MAX_VALUE;  // 最少删除节点数，初始为一个很大的值

    static void solve() {
        int n = sc.nextInt();  // 读入顶点个数
        adj = new List[n];
        for (int i = 0; i < n; ++i) adj[i] = new ArrayList<>();

        // 读入 n-1 条边，构建无向树
        for (int i = 0; i < n - 1; ++i) {
            int u = sc.nextInt(), v = sc.nextInt();
            u--; v--;  // 改为 0-based 编号
            adj[u].add(v);
            adj[v].add(u);
        }

        // 枚举每一个顶点作为雪花树的「中心」
        for (int u = 0; u < n; ++u) {
            // 收集所有邻居的度数（deg(neighbor)）
            // 这些邻居将扮演雪花树的「中层节点」
            List<Integer> list = new ArrayList<>();
            for (int v : adj[u]) {
                // 注意这里存的是邻居 v 的度数
                list.add(adj[v].size());
            }

            // 度数从小到大排序
            Collections.sort(list);
            int cnt = list.size();  // cnt = u 的度数（即邻居个数）

            // 检查：把雪花树“嵌入”到原树中，删掉最少节点
            for (int i = 0; i < list.size(); ++i) {
                // 假设我们只保留 (cnt - i) 个中层节点，
                // 且每个中层节点保留 list.get(i) 个叶子（因为邻居度数有限，取较小的值来保守计算）
                // n - (cnt - i) * list.get(i) - 1 的含义：
                //   (cnt - i) 个中层节点，
                //   每个中层节点贡献 list.get(i) 个叶子，
                //   再加 1 个中心节点，
                //   就是剩余雪花树的总节点数。
                // 原树有 n 个节点，减去剩余节点数 = 要删除的节点数。
                int remove = n - (cnt - i) * list.get(i) - 1;
                if (remove < ans) ans = remove;
            }
        }

        System.out.println(ans);
    }

    public static void main(String[] args) {
        int opt = 0, t = 1;
        // opt = 1 时可支持多组测试数据（题目默认单组）
        if (opt == 1) t = sc.nextInt();
        while (t-- != 0)
            solve();
    }
}
