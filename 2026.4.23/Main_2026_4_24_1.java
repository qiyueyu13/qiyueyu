/*
题目提交链接：https://atcoder.jp/contests/abc385/tasks/abc385_e
题目大意：
        给你一棵树，你可以删掉一些顶点和边。
    剩下的图形必须是一个雪花树。雪花树的形状是：
    有一个中心点，中心点连着 x 个中间点，每个中间点再连着 y 个叶子。
    x 和 y 都是至少为 1 的正整数。问最少需要删掉多少个顶点。题目保证一定可以做到。
*/
package example;

import java.util.*;

public class Main_2026_4_24_1{
    static Scanner sc = new Scanner(System.in);
    static List<Integer>[] adj;
    static int ans = Integer.MAX_VALUE;
    static void solve() {
    	int n = sc.nextInt();
    	adj = new List[n];
    	for(int i = 0; i < n; ++i) adj[i] = new ArrayList<>();
    	for(int i = 0; i < n - 1; ++i) {
    		int u = sc.nextInt(), v = sc.nextInt();
    		u--; v--;
    		adj[u].add(v);
    		adj[v].add(u);
    	}
    	for(int u = 0; u < n; ++u) {
    		List<Integer> list = new ArrayList<>();
    		for(int v : adj[u]) {
    			list.add(adj[v].size());
    		}
    		Collections.sort(list);
    		int cnt = list.size();
        	for(int i = 0; i < list.size(); ++i) {
        		ans = Math.min(n - (cnt - i) * list.get(i) - 1, ans);
        	}
    	}
    	System.out.println(ans);
    }
    
    public static void main(String[] args) {
    	int opt = 0, t = 1;
    	//opt = 1;
    	if(opt == 1) t = sc.nextInt();
    	while(t-- != 0)
    		solve();
    }
}
