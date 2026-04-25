/*
题目提交链接：https://atcoder.jp/contests/abc346/tasks/abc346_f
题目大意：
    定义：
    f(X, k) 表示把字符串 X 重复 k 次得到的字符串。例如 X="abc", f(X,2)="abcabc"。
    g(X, k) 表示把 X 的每个字符各自重复 k 次得到的字符串。例如 X="abc", g(X,3)="aaabbbccc"。
    当 k=0 时，两者都为空字符串。
    输入：
    一个非常大的正整数 N（可达 10^12）
    字符串 S
    字符串 T
    任务：
    构造 f(S, N)（S 重复 N 次）
    构造 g(T, k)（T 中每个字符重复 k 次）
    找出最大的非负整数 k，使得 g(T, k) 是 f(S, N) 的子序列（不要求连续，但顺序要一致）
    约束：
    1 ≤ N ≤ 10^12
    1 ≤ |S|, |T| ≤ 10^5
    输出：
    最大的合法 k

简明思路：
  目标
  找到最大 k，使 g(T,k) 是 f(S,N) 的子序列。
  其中：
  f(S,N) = S 重复 N 次
  g(T,k) = T 的每个字符重复 k 次（即每个字符连续出现 k 次）
  核心转化
  等价于：在 S 重复 N 次的字符串中，能否按顺序找到：
  T[0] 重复 k 次、T[1] 重复 k 次、…、T[m-1] 重复 k 次。
  解法（二分答案 + 贪心判断）
  二分 k（范围 0 ~ N*|S|）
  检查可行性 check(k)：
  遍历 T 的每个字符 c，需要找到 c 连续出现 k 次
  维护当前在 f(S,N) 中的位置 idx（0‑based）
  用预处理快速跳转：
  pre[ch][i] = S 的前 i 个字符中字符 ch 的出现次数
  pos[ch][cnt] = S 中第 cnt 次出现 ch 的位置（1‑based）
  对每个字符 c：
  看当前 S 片段（从 idx 到末尾）中 c 的剩余个数 remain
  若 remain >= k：在本片段跳到第 pre[c][idx] + k 次出现的位置
  否则：消耗完本片段，跨若干完整 S 周期，再用 pos 跳到剩余需要的次数
  若总移动长度超过 N * |S|，则失败
  输出最大 k
  复杂度
  预处理：O(|S|)
  每次 check：O(|T|)
  总复杂度：O(|T| log(N|S|))
*/
import java.util.*;

public class Main_2026_4_22_1 {
    static Scanner sc = new Scanner(System.in);
    static int[][] pre;  // pre[ch][i] : 在S的前i个字符中，字符ch出现的次数
    static int[][] pos;  // pos[ch][cnt] : 字符ch第cnt次出现的位置（1-indexed）

    /**
     * 检查是否存在一个长度为 maxLen 的 f(S,N) 前缀，能匹配 g(T,mid) 作为子序列
     * @param mid     当前二分的k值，即g(T,k)中每个字符重复的次数
     * @param maxLen  f(S,N)的总长度，即 N * |S|
     * @param s       原字符串S
     * @param t       原字符串T
     * @return        true 如果g(T,mid)是f(S,N)的子序列
     */
    static boolean check(long mid, long maxLen, String s, String t) {
        int n = s.length(), m = t.length();
        long used = 0;   // 在f(S,N)中已经用掉的字符位置（长度）
        int idx = 0;     // 当前在S中的位置（0-indexed），表示下一个待匹配的起始点

        for (int i = 0; i < m; ++i) {
            int ch = t.charAt(i) - 'a';

            // 在当前S片段（从idx到末尾）中，字符ch还剩多少个
            int remain = pre[ch][n] - pre[ch][idx];

            if (remain >= mid) {
                // 情况1：当前片段内就能找到mid个ch
                // 需要找到第(pre[ch][idx] + mid)次出现的位置
                int targetPos = pos[ch][pre[ch][idx] + (int) mid];
                used += targetPos - idx;   // 移动这个距离
                idx = targetPos;           // 新的起始位置
            } else {
                // 情况2：当前片段不够，需要跨片段（甚至跨周期）
                used += n - idx;           // 先把当前片段用完
                long need = mid - remain;  // 还缺多少个ch
                long cycles = need / pre[ch][n]; // 需要完整走过的S的周期数
                int rem = (int) (need % pre[ch][n]); // 最后一个周期需要多少个

                used += cycles * n;        // 走完整周期

                if (rem == 0) {
                    // 如果刚好整周期，最后一个周期不需要走完
                    used -= n;
                    idx = pos[ch][pre[ch][n]]; // 定位到本周期最后一个ch后
                    used += idx;
                } else {
                    idx = pos[ch][rem];    // 定位到本周期第rem个ch的位置
                    used += idx;
                }
            }

            if (used > maxLen) return false; // 超出总长度，失败
        }
        return true; // 全部匹配成功
    }

    static void solve() {
        long N = sc.nextLong();
        sc.nextLine();                     // 吞掉换行符
        String s = sc.nextLine();
        String t = sc.nextLine();
        int len = s.length();

        // 初始化 pre 和 pos 数组
        pre = new int[26][len + 1];
        pos = new int[26][len + 1];
        for (int i = 0; i < 26; ++i) {
            Arrays.fill(pos[i], 0);
        }

        // 预处理 pre 和 pos
        // pre[ch][i] 表示 S 前 i 个字符中 ch 的出现次数
        // pos[ch][cnt] 表示 ch 第 cnt 次出现的位置（1-indexed）
        for (int i = 1; i <= len; ++i) {
            // 复制上一列
            for (int ch = 0; ch < 26; ++ch) {
                pre[ch][i] = pre[ch][i - 1];
            }
            // 更新当前字符
            int chIdx = s.charAt(i - 1) - 'a';
            pre[chIdx][i]++;
            if (pos[chIdx][pre[chIdx][i]] == 0) {
                pos[chIdx][pre[chIdx][i]] = i;
            }
        }

        // 特判：如果 T 中有字符在 S 中完全不存在，则直接输出 0
        for (int i = 0; i < t.length(); ++i) {
            if (pre[t.charAt(i) - 'a'][len] == 0) {
                System.out.println(0);
                return;
            }
        }

        // 二分答案 k，范围 [0, N * len]
        long l = 0, r = N * len;
        while (l < r) {
            long mid = (l + r + 1) >> 1;  // 上取整
            if (check(mid, N * len, s, t)) l = mid;
            else r = mid - 1;
        }
        System.out.println(l);
    }

    public static void main(String[] args) {
        solve();
    }
}
