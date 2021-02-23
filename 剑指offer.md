# 剑指offer

## 1.二维数组的查找

题目链接:[本题链接](https://www.nowcoder.com/practice/abc3fe2ce8e146608e868a70efebf62e?tpId=13&tqId=11154&rp=1&ru=%2Fta%2Fcoding-interviews&qru=%2Fta%2Fcoding-interviews%2Fquestion-ranking&tab=answerKey)

题解:二分思想，不能简单的将二维看成一维，要找到正确的中点,比如右上角的点(大于左边,小于下边),左下角的点(大于上边,小于右边)

```java
public class Solution {
    public boolean Find(int target, int [][] array) {
        // !!!判断数组是否为空
        if(array == null || array.length == 0 || array[0].length == 0) return false;
        int n = array.length;
        int i = 0, j = n - 1;
        while(i < n && j >= 0){
            // 以右上角的点为中点
            int mid = array[i][j];
            if(mid == target) return true;
            if(mid > target){
                // 中点大于目标点, 指针向左移动
                j--;
            }else{
                // 中点小于目标的, 指针向右移动
                i++;
            }
        }
        return false;
    }
}
```

## 2.替换空格

题目链接: [本题链接](https://www.nowcoder.com/practice/4060ac7e3e404ad1a894ef3e17650423?tpId=13&tqId=11155&rp=1&ru=%2Fta%2Fcoding-interviews&qru=%2Fta%2Fcoding-interviews%2Fquestion-ranking&tab=answerKey)

### 解法一: replaceAll() API解法

```java
/**
 * 面试时用这方法看面试官挂不挂你
 */
public class Solution {
    public String replaceSpace(StringBuffer str) {
        String s = str.toString();
        s = s.replaceAll(" ", "%20");
        return s;
    }
}
```

### 解法二: 使用char[]求解

```java
class Solution {
    public String replaceSpace(String s) {
        int space = 0;
        // 获取空格数
        for(int i = 0; i < s.length(); i++)
            if(s.charAt(i) == ' ') space++;
        int index = 0;
        // 创建一个能装下替换后的字符的送祝福
        char[] ans = new char[s.length() + space * 2];
        for(int i = 0; i < s.length(); i++){
            if(s.charAt(i) != ' ') ans[index++] = s.charAt(i);
            else{
                // 当遇到空格插入%20
                ans[index++] = '%';
                ans[index++] = '2';
                ans[index++] = '0';
            }
        }
        return new String(ans);
    }
}
```

### 解法三: 使用StringBuilder

```java
class Solution {
    public String replaceSpace(String s) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < s.length(); i++){
            if(s.charAt(i) == ' ') sb.append("%20");
            else sb.append(s.charAt(i));
        }
        return sb.toString();
    }
}
```

## 3.从尾到头打印链表

题目链接:[本题链接](https://www.nowcoder.com/practice/d0267f7f55b3412ba93bd35cfa8e8035?tpId=13&tqId=11156&rp=1&ru=%2Fta%2Fcoding-interviews&qru=%2Fta%2Fcoding-interviews%2Fquestion-ranking&tab=answerKey)

### 解法一: 递归法

```java
import java.util.ArrayList;
public class Solution {
    ArrayList<Integer> ans = new ArrayList<>();
    public ArrayList<Integer> printListFromTailToHead(ListNode listNode) {
        if(listNode == null) return ans;
        // 递归到最后一个结点
        printListFromTailToHead(listNode.next);
        ans.add(listNode.val);
        return ans;
    }
}
```

### 解法二: 迭代法

```java
import java.util.ArrayList;
public class Solution {
    ArrayList<Integer> ans = new ArrayList<>();
    public ArrayList<Integer> printListFromTailToHead(ListNode listNode) {
        while(listNode != null){
            // 头插法
            ans.add(0, listNode.val);
            listNode = listNode.next;
        }
        return ans;
    }
}
```

## 4.重建二叉树

题目链接:[本题链接](https://www.nowcoder.com/practice/8a19cbe657394eeaac2f6ea9b0f6fcf6?tpId=13&tqId=11157&rp=1&ru=%2Fta%2Fcoding-interviews&qru=%2Fta%2Fcoding-interviews%2Fquestion-ranking&tab=answerKey)

```java
import java.util.Arrays;
public class Solution {
    public TreeNode reConstructBinaryTree(int [] pre,int [] in) {
        if(pre.length == 0 || in.length == 0) return null;
        TreeNode root = new TreeNode(pre[0]);
        
        for(int i = 0; i < in.length; i++){
            // 查找根节点在中序遍历序列中的位置
            if(in[i] == pre[0]){
                // 递归查找左子树
                root.left = reConstructBinaryTree(Arrays.copyOfRange(pre, 1, i + 1), Arrays.copyOfRange(in, 0, i));
                // 递归查找右子树
                root.right = reConstructBinaryTree(Arrays.copyOfRange(pre, i + 1, pre.length), Arrays.copyOfRange(in, i + 1, in.length));
                break;
            }
        }
        return root;
    }
}
```

## 5.两个栈实现队列

题目链接: [本题链接](https://www.nowcoder.com/practice/54275ddae22f475981afa2244dd448c6?tpId=13&tqId=11158&rp=1&ru=%2Fta%2Fcoding-interviews&qru=%2Fta%2Fcoding-interviews%2Fquestion-ranking&tab=answerKey)

```java
import java.util.Stack;

public class Solution {
    Stack<Integer> stack1 = new Stack<Integer>();
    Stack<Integer> stack2 = new Stack<Integer>();
    
    public void push(int node) {
        stack1.push(node);
    }
    
    public int pop() {
        if(!stack2.isEmpty()){
            return stack2.pop();
        }
        while(!stack1.isEmpty()){
            stack2.push(stack1.pop());
        }
        return stack2.pop();
    }
}
```

## 6.旋转数组的最小数字

题目链接:[本题链接](https://www.nowcoder.com/practice/9f3231a991af4f55b95579b44b7a01ba?tpId=13&tqId=11159&rp=1&ru=%2Fta%2Fcoding-interviews&qru=%2Fta%2Fcoding-interviews%2Fquestion-ranking&tab=answerKey)

### 解法一: 排序

```java
import java.util.ArrayList;
import java.util.Arrays;
public class Solution {
    public int minNumberInRotateArray(int [] array) {
        if(array == null || array.length == 0) return 0;
        Arrays.sort(array);
        return array[0];
    }
}
```

### 解法二: 二分

```java
import java.util.ArrayList;
public class Solution {
    public int minNumberInRotateArray(int [] array) {
        if(array == null || array.length == 0) return 0;
        int n = array.length;
        int l = 0, r = n - 1;
        while(l < r){
            // 判断左端点是否小于右端点，截断循环
            if(array[l] < array[r]) return array[l];
            int mid = l + ((r - l) >>> 1);
            // 中点大于右端点
            if(array[mid] > array[r]){
                l = mid + 1;
            // 中点小于右端点
            }else if(array[mid] < array[r]){
                r = mid;
            // 中点等于右端点
            }else{
                r--;
            }
        }
        return array[l];
    }
}
```

## 7.斐波那契数列

题目链接:[本题链接](https://www.nowcoder.com/practice/c6c7742f5ba7442aada113136ddea0c3?tpId=13&tqId=11160&rp=1&ru=%2Fta%2Fcoding-interviews&qru=%2Fta%2Fcoding-interviews%2Fquestion-ranking&tab=answerKey)

### 解法一: 递归+记忆化

```java
public class Solution {
    // dp数组
    int[] dp = new int[40];
    public int Fibonacci(int n) {
        // 特殊值判断
        if(n <= 0) return 0;
        if(n == 1) return 1;
        if(dp[n] != 0){
            return dp[n];
        }
        dp[n] = Fibonacci(n-2) + Fibonacci(n-1);
        return dp[n];
    }
}
```

### 解法二: 迭代

```java
public class Solution {
    public int Fibonacci(int n) {
        if(n <= 0) return 0;
        if(n == 1) return 1;
        int a = 0, b = 1;
        for(int i = 2; i <= n; i++){
            int t = a + b;
            a = b;
            b = t;
        }
        return b;
    }
}
```

## 8.跳台阶

题目链接:[本题链接](https://www.nowcoder.com/practice/8c82a5b80378478f9484d87d1c5f12a4?tpId=13&tqId=11161&rp=1&ru=%2Fta%2Fcoding-interviews&qru=%2Fta%2Fcoding-interviews%2Fquestion-ranking&tab=answerKey)

```java
// 类斐波那契数列
public class Solution {
    public int JumpFloor(int target) {
        if(target <= 2) return target;
        int t, a = 1, b = 2;
        for(int i = 3; i <= target; i++){
            t = a + b;
            a = b;
            b = t;
        }
        return b;
    }
}
```

## 9.变态跳台阶

### 解法一: 动态规划

∵ F(n) = F(1) + F(2) + F(3) + F(4) + ... + F(n-2) + F(n-1)

  F(n-1) = F(1) + F(2) + F(3) + F(4) + ... + F(n-2)

∴ F(n) = 2 * F(n-1)

```java
public class Solution {
    public int JumpFloorII(int target) {
        if(target == 1) return 1;
        if(target == 0) return 0;
        
        return 2*JumpFloorII(target-1);
    }
}
```

## 10.矩形覆盖

```java
public class Solution {
    public int rectCover(int target) {
         if(target < 1) return 0;
        if(target == 1 || target == 2) return target;
        return rectCover(target-1)+rectCover(target-2);
    }
}
```

## 11.二进制中的1的个数

题目链接:[本题链接](https://www.nowcoder.com/practice/8ee967e43c2c4ec193b040ea7fbb10b8?tpId=13&tqId=11164&rp=1&ru=%2Fta%2Fcoding-interviews&qru=%2Fta%2Fcoding-interviews%2Fquestion-ranking&tab=answerKey)

```java
public class Solution {
    public int NumberOf1(int n) {
        int ans = 0;
        while(n != 0){
            n &= n-1;
            ans++;
        }
        return ans;
    }
}
```

## 12.数的整数次方

题目链接：[本题链接](https://www.nowcoder.com/practice/1a834e5e3e1a4b7ba251417554e07c00?tpId=13&tqId=11165&rp=1&ru=%2Fta%2Fcoding-interviews&qru=%2Fta%2Fcoding-interviews%2Fquestion-ranking&tab=answerKey)

解法一: 快速幂

```java
public class Solution {
    public double Power(double base, int exponent) {
        double ans = 1;
        // 判断次方数是否为负数
        if(exponent < 0){
            base = 1.0 / base;
            exponent = -exponent;
        }
        while(exponent > 0){
            if(exponent % 2  == 1){
                ans = ans * base;
            }
            base *= base;
            exponent >>= 1;
        }
        return ans;
  }
}
```

## 14.链表中的倒数第K个结点

题目链接:[本题链接](https://www.nowcoder.com/practice/529d3ae5a407492994ad2a246518148a?tpId=13&tqId=11167&rp=1&ru=%2Fta%2Fcoding-interviews&qru=%2Fta%2Fcoding-interviews%2Fquestion-ranking&tab=answerKey)

解法一: 快慢指针

```java
public class Solution {
    public ListNode FindKthToTail(ListNode head,int k) {
        if(head == null) return null;
        ListNode p = head, q = head;
        for(int i = 0; i < k; i++){
            if(q == null) return null;
            q = q.next;
        }
        while(q != null){
            p = p.next;
            q = q.next;
        }
        return p;
    }
}
```

## 15.翻转链表

题目链接:[本题链接](https://www.nowcoder.com/practice/75e878df47f24fdc9dc3e400ec6058ca?tpId=13&tqId=11168&rp=1&ru=%2Fta%2Fcoding-interviews&qru=%2Fta%2Fcoding-interviews%2Fquestion-ranking&tab=answerKey)

```java
public class Solution {
    public ListNode ReverseList(ListNode head) {
        if(head == null) return null;
        ListNode t = null;
        ListNode p = head;
        while(p != null){
            ListNode q = p.next;
            p.next = t;
            t = p;
            p = q;
        }
        return t;
    }
}
```

## 16.合并两个有序链表

题目链接:[本题链接](https://www.nowcoder.com/practice/d8b6b4358f774294a89de2a6ac4d9337?tpId=13&tqId=11169&rp=1&ru=%2Fta%2Fcoding-interviews&qru=%2Fta%2Fcoding-interviews%2Fquestion-ranking&tab=answerKey)

```java
public class Solution {
    public ListNode Merge(ListNode list1,ListNode list2) {
        if(list1 == null || list2 == null) return list1 == null ? list2 : list1;
        ListNode head = list1.val > list2.val ? list2 : list1;
        head.next = head == list1 ? Merge(list1.next, list2) : Merge(list1, list2.next);
        return head;
    }
}
```

## 17.树的子结构



```java
public class Solution {
    public boolean HasSubtree(TreeNode root1,TreeNode root2) {
        if(root1 == null || root2 == null) return false;
        
        // 当前结点是否等于子树的头结点
        if(root1.val == root2.val){
            // 比较每个结点
            if(dfs(root1, root2)) return true;
        }
        return HasSubtree(root1.left, root2) || HasSubtree(root1.right, root2);
    }
    
    boolean dfs(TreeNode root, TreeNode subtree){
        if(subtree == null) return true;
        if(root == null) return false;
        return root.val == subtree.val && dfs(root.left, subtree.left) && dfs(root.right, subtree.right);
    }
}
```

## 18.二叉树的镜像

题目链接:[本题链接](https://www.nowcoder.com/practice/564f4c26aa584921bc75623e48ca3011?tpId=13&tqId=11171&rp=1&ru=%2Fta%2Fcoding-interviews&qru=%2Fta%2Fcoding-interviews%2Fquestion-ranking&tab=answerKey)

```java
public class Solution {
    public void Mirror(TreeNode root) {
        if(root == null) return;
        
        Mirror(root.left);
        Mirror(root.right);
        
        // 交换左右子树
        TreeNode t = root.right;
        root.right = root.left;
        root.left = t;
    }
}
```

## 19.顺时针打印矩阵

题目链接:[本题链接](https://www.nowcoder.com/practice/9b4c81a02cd34f76be2659fa0d54342a?tpId=13&tqId=11172&rp=1&ru=%2Fta%2Fcoding-interviews&qru=%2Fta%2Fcoding-interviews%2Fquestion-ranking&tab=answerKey)

```java
import java.util.ArrayList;
public class Solution {
    public ArrayList<Integer> printMatrix(int [][] matrix) {
        ArrayList<Integer> ans = new ArrayList<>();
        if(matrix == null || matrix.length == 0 || matrix[0].length == 0) return ans;
        int n = matrix.length, m = matrix[0].length;
        // 记忆矩阵
        boolean[][] vis = new boolean[n][m];
        int x = 0, y = 0, nx = 0, ny = 0, index = 0;
        // 方向
        int[][] dr = {{0,1}, {1,0}, {0,-1}, {-1, 0}};
        for(int i = 0; i < n * m; i++){
            ans.add(matrix[x][y]);
            vis[x][y] = true;
            nx = x + dr[index][0];
            ny = y + dr[index][1];
            // 更换方向
            if(nx < 0 || ny < 0 || nx >= n || ny >= m || vis[nx][ny]){
                index = (index + 1) % 4;
                nx = x + dr[index][0];
                ny = y + dr[index][1];
            }
            x = nx;
            y = ny;
        }
        return ans;
    }
}
```

## 20.包含min函数的栈

题目链接:[本题链接](https://www.nowcoder.com/practice/4c776177d2c04c2494f2555c9fcc1e49?tpId=13&tqId=11173&rp=1&ru=%2Fta%2Fcoding-interviews&qru=%2Fta%2Fcoding-interviews%2Fquestion-ranking&tab=answerKey)

```java
import java.util.Stack;

public class Solution {

    Stack<Integer> stack1 = new Stack<Integer>();
    Stack<Integer> stack2 = new Stack<Integer>();

    public void push(int node) {
        stack1.push(node);
        if(stack2.isEmpty()){
            stack2.push(node);
        }else if(stack2.peek() >= node){
            stack2.push(node);
        }
    }
    
    public void pop() {
        int t = stack1.pop();
        if(t == stack2.peek()) stack2.pop();
    }
    
    public int top() {
        return stack1.peek();
    }
    
    public int min() {
        return stack2.peek();
    }
}
```

## 22.从上往下打印二叉树

题目链接:[本题链接](https://www.nowcoder.com/practice/7fe2212963db4790b57431d9ed259701?tpId=13&tqId=11175&rp=1&ru=%2Fta%2Fcoding-interviews&qru=%2Fta%2Fcoding-interviews%2Fquestion-ranking&tab=answerKey)

```java
import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;

public class Solution {
    public ArrayList<Integer> PrintFromTopToBottom(TreeNode root) {
        Queue<TreeNode> q = new LinkedList<>();
        ArrayList<Integer> ans = new ArrayList<>();
        if(root == null) return ans;
        q.offer(root);
        while(!q.isEmpty()){
            TreeNode t = q.poll();
            ans.add(t.val);
            if(t.left != null) q.offer(t.left);
            if(t.right != null) q.offer(t.right);
        }
        return ans;
    }
}
```

## 27.字符串排序

```java
import java.util.ArrayList;
public class Solution {
    ArrayList<String> ans = new ArrayList<String>();
    boolean[] vis;
    public ArrayList<String> Permutation(String str) {
       if(str == null || str.length() == 0) return ans;
     
       vis = new boolean[str.length()];
       dfs(str, new StringBuilder(), 0);
        return ans;
    }
    void dfs(String str, StringBuilder tmp, int index){
        if(index == str.length()){
            if(ans.contains(tmp.toString())) return;
            ans.add(tmp.toString());
            return;
        }
        for(int i = 0; i < str.length(); i++){
            if(!vis[i]){
                vis[i] = true;
                tmp.append(str.charAt(i));
                dfs(str, tmp, index + 1);
                tmp.deleteCharAt(tmp.length()-1);
                vis[i] = false;
            }
        }
    }
}
```

## 28.数组中出现次数超过一半的数字

题目链接:[本题链接](https://www.nowcoder.com/practice/e8a1b01a2df14cb2b228b30ee6a92163?tpId=13&tqId=11181&rp=1&ru=%2Fta%2Fcoding-interviews&qru=%2Fta%2Fcoding-interviews%2Fquestion-ranking&tab=answerKey)

### 解法一:投票法

```java
public class Solution {
    public int MoreThanHalfNum_Solution(int [] array) {
        int cond = -1, cnt = 0;
        for(int i = 0; i < array.length; i++){
            if(cnt == 0){
                cnt++;
                cond = array[i];
            } 
            else{
                if(cond != array[i]) cnt--;
                else cnt++;
            }
        }
        cnt = 0;
        for(int t : array){
            if(cond == t) cnt++;
            if(cnt > array.length / 2) return cond;
        }
        return 0;
    }
}
```

### 解法二：排序法

```java
import java.util.Arrays;
public class Solution {
    public int MoreThanHalfNum_Solution(int [] array) {
        Arrays.sort(array);
        int cond = array[array.length/2];
        int cnt = 0;
        for(int t : array){
            if(cond == t) cnt++;
            if(cnt > array.length / 2) return cond;
        }
        return 0;
    }
}
```

## 30.连续子数组的最大和

```java
public class Solution {
    public int FindGreatestSumOfSubArray(int[] array) {
        if(array == null || array.length == 0) return 0;
        int[] dp = new int[array.length];
        dp[0] = array[0];
        int max = Integer.MIN_VALUE;
        for(int i = 1; i < array.length; i++){
            if(dp[i-1] < 0) dp[i] = array[i];
            else dp[i] = array[i] + dp[i-1];
            max = Math.max(dp[i], max);
        }
        return max;
    }
}
```

## 33.丑数

```java
public class Solution {
    public int GetUglyNumber_Solution(int index) {
        if(index <= 0)return 0;
        int p2=0,p3=0,p5=0;//初始化三个指向三个潜在成为最小丑数的位置
        int[] result = new int[index];
        result[0] = 1;//
        for(int i=1; i < index; i++){
            result[i] = Math.min(result[p2]*2, Math.min(result[p3]*3, result[p5]*5));
            if(result[i] == result[p2]*2)p2++;//为了防止重复需要三个if都能够走到
            if(result[i] == result[p3]*3)p3++;//为了防止重复需要三个if都能够走到
            if(result[i] == result[p5]*5)p5++;//为了防止重复需要三个if都能够走到


        }
        return result[index-1];
    }
}
```

## 34.第一次只出现一次的字符

题目链接:[本题链接](https://www.nowcoder.com/practice/1c82e8cf713b4bbeb2a5b31cf5b0417c?tpId=13&tqId=11187&rp=1&ru=%2Fta%2Fcoding-interviews&qru=%2Fta%2Fcoding-interviews%2Fquestion-ranking&tab=answerKey)

```java
// hash存储
public class Solution {
    public int FirstNotRepeatingChar(String str) {
        int[] count = new int[256];
        for(int i = 0; i < str.length(); i++){
            count[str.charAt(i)]++;
        }
        for(int i = 0; i < str.length(); i++){
            if(count[str.charAt(i)] == 1){
                return i;
            }
        }
        return -1;
    }
}
```

