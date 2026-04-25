package example;

import java.util.*;

public class Main_2026_4_25_1{
    static Scanner sc = new Scanner(System.in);
    static final int MOD = 998244353;
    static void solve() {
    	int n = sc.nextInt();
    	int[] a = new int[n];
    	for(int i = 0; i < n; ++i) a[i] = sc.nextInt();
    	int idx = 0;
    	Map<Integer, Integer> map = new HashMap<>();
    	for(int i = 0; i < n; ++i) {
    		for(int j = 0; j < i; ++j) {
    			int d = a[i] - a[j];
    			if(!map.containsKey(d)) map.put(d, idx++);
    		}
    	}
    	long[][][] dp = new long[n + 1][n + 1][idx];
    	for(int i = 1; i <= n; ++i) {
    		for(int j = 0; j < idx; ++j) {
    			dp[i][1][j] = 1;
    		}
    	}
    	
    	long[] ans = new long[n + 1];
    	ans[1] = n;
    	for(int i = 1; i <= n; ++i) {
    		for(int j = 2; j <= i; ++j) {
    			for(int k = 1; k < i; ++k) {
    				int d = a[i - 1] - a[k - 1];
    				int pos = map.get(d);
    				dp[i][j][pos] = (dp[i][j][pos] + dp[k][j - 1][pos]) % MOD;
    			}
    		}
    	}
    	for(int i = 1; i <= n; ++i) {
    		for(int j = 2; j <= i; ++j) {
    			for(int k = 0; k < idx; ++k) {
    				ans[j] = (ans[j] + dp[i][j][k]) % MOD;
    			}
    		}
    	}
    	for(int i = 1; i <= n; ++i) {
    		System.out.print(ans[i] + " ");
    	}
    	System.out.println();
    }
    
    public static void main(String[] args) {
    	int opt = 0, t = 1;
    	//opt = 1;
    	if(opt == 1) t = sc.nextInt();
    	while(t-- != 0)
    		solve();
    }
}
