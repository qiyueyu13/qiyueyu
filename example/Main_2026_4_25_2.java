/*
题目提交链接：https://atcoder.jp/contests/abc344/tasks/abc344_f
题目大意：
        给定一个长度为 N 的 01 字符串 S，有 Q 个操作：
    类型1 (L R)：翻转区间 [L, R] 内的所有字符（0变1，1变0）
    类型2 (L R)：查询子串 S[L..R] 是否是"好字符串"（即相邻字符都不相同）

简明思路：
    关键观察：一个字符串是"好字符串"当且仅当所有相邻字符对都不相等。
    转化：定义数组 diff[i] 表示 S[i] 和 S[i+1] 是否不同：
    diff[i] = 1 如果 S[i] ≠ S[i+1]
    diff[i] = 0 如果 S[i] = S[i+1]
    那么 S[L..R] 是好字符串 ⟺ diff[L] + diff[L+1] + ... + diff[R-1] = (R-L)（即所有相邻对都不同）
    问题转化为：
    区间翻转操作会影响 diff 数组：翻转 [L, R] 只会改变 diff[L-1] 和 diff[R] 两个位置
    区间查询 [L, R] 变成查询 diff 数组在 [L, R-1] 的和
    使用线段树：维护 diff 数组，支持单点更新（翻转时修改最多2个点）和区间求和。
*/
package example;
import java.util.*;

public class Main_2026_4_25_2 {
    // 线段树：维护diff数组，支持单点更新和区间求和
    static class SegmentTree {
        private int[] tree;   // 线段树数组
        private int n;        // diff数组长度 = N-1

        public SegmentTree(String s) {
            this.n = s.length() - 1;  // diff数组长度为N-1
            this.tree = new int[4 * n];
            if (n > 0) {
                build(s, 1, 1, n);
            }
        }

        // 构建线段树：叶子i存储 S[i] 和 S[i+1] 是否不同
        private void build(String s, int node, int start, int end) {
            if (start == end) {
                // start: 1-based，对应S[start]和S[start+1]
                // 转换为0-based：start-1 和 start
                tree[node] = s.charAt(start - 1) == s.charAt(start) ? 0 : 1;
                return;
            }
            int mid = (start + end) / 2;
            int leftChild = node * 2;
            int rightChild = node * 2 + 1;
            build(s, leftChild, start, mid);
            build(s, rightChild, mid + 1, end);
            tree[node] = tree[leftChild] + tree[rightChild];
        }

        // 区间查询 [L, R] 的和
        public int query(int L, int R) {
            if (L > R) return 0;  // 空区间
            return query(1, 1, n, L, R);
        }

        private int query(int node, int start, int end, int L, int R) {
            if (R < start || L > end) return 0;
            if (L <= start && end <= R) return tree[node];
            int mid = (start + end) / 2;
            int leftChild = node * 2;
            int rightChild = node * 2 + 1;
            int leftSum = query(leftChild, start, mid, L, R);
            int rightSum = query(rightChild, mid + 1, end, L, R);
            return leftSum + rightSum;
        }

        // 单点更新：将index位置的值改为val（0或1）
        public void update(int index, int val) {
            if (index < 1 || index > n) return;  // 边界检查
            update(1, 1, n, index, val);
        }

        private void update(int node, int start, int end, int index, int val) {
            if (start == end) {
                tree[node] = val;
                return;
            }
            int mid = (start + end) / 2;
            int leftChild = node * 2;
            int rightChild = node * 2 + 1;
            if (index <= mid) {
                update(leftChild, start, mid, index, val);
            } else {
                update(rightChild, mid + 1, end, index, val);
            }
            tree[node] = tree[leftChild] + tree[rightChild];
        }
    }

    static Scanner sc = new Scanner(System.in);

    static void solve() {
        int n = sc.nextInt();  // 字符串长度
        int q = sc.nextInt();  // 查询次数
        sc.nextLine();         // 消耗换行
        String s = sc.nextLine();
        
        SegmentTree seg = new SegmentTree(s);  // 构建diff数组的线段树
        
        while (q-- > 0) {
            int opt = sc.nextInt();
            int l = sc.nextInt();
            int r = sc.nextInt();
            
            if (opt == 1) {
                // 区间翻转操作：只会影响diff[l-1]和diff[r]（如果存在）
                // 因为只改变边界，内部相邻关系不变
                
                // 影响位置 l-1：S[l-1]和S[l]的关系改变
                if (l - 1 >= 1) {
                    // 取反：当前值0变1，1变0
                    int cur = seg.query(l - 1, l - 1);
                    seg.update(l - 1, cur == 0 ? 1 : 0);
                }
                // 影响位置 r：S[r]和S[r+1]的关系改变
                if (r < n) {
                    int cur = seg.query(r, r);
                    seg.update(r, cur == 0 ? 1 : 0);
                }
            } else {
                // 查询操作：检查S[L..R]是否是好字符串
                // 好字符串条件：区间内所有相邻字符都不同
                // 即 diff[L] + diff[L+1] + ... + diff[R-1] = R - L
                int len = r - l;          // 相邻对的数量 = R-L
                int sum = seg.query(l, r - 1);  // 实际不同的相邻对数量
                System.out.println(sum == len ? "Yes" : "No");
            }
        }
    }

    public static void main(String[] args) {
        solve();
    }
}
