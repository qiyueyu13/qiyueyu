/*
题目链接
https://atcoder.jp/contests/abc386/tasks/abc386_e?lang=en
*/
import java.util.*;

public class Main{
    static Scanner sc = new Scanner(System.in);
    static long ans = 0;
    static long s = 0;
    static void dfs(int cnt, int start, long res, long[] a, int k, int opt) {
    	if(cnt + a.length - start + 1 < k) return;
    	if(cnt == k) {
    		if(opt == 1) {
    			ans = Math.max(ans, res);
    		}else {
    			ans = Math.max(ans, s ^ res);
    		}
    		return;
    	}
    	if(start >= a.length) return;
    	int n = a.length;
    	for(int i = start; i < n; ++i) {
    		dfs(cnt + 1, start + 1, res ^ a[i], a, k, opt);
    	}
    }
    
    static void solve() {
       int n = sc.nextInt(), k = sc.nextInt();
       long[] a = new long[n];
       for(int i = 0; i < n; ++i) {
    	   a[i] = sc.nextLong();
    	   s ^= a[i];
       }
       if(k <= n - k) dfs(0, 0, 0L, a, k, 1);
       else {
    	   dfs(0, 0, 0L, a, n - k, 0);
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
