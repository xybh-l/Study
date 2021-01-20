### 二分模板

```java
class Solution {
    public int[] searchRange(int[] nums, int target) {
        if(nums.length == 0) return new int[]{-1,-1};
        return new int[]{leftRange(nums, target), rightRange(nums, target)};
    }
    
    //查找左边界
    private int leftRange(int[] nums, int target){
        int l = 0, r = nums.length - 1;
        while(l <= r){
            int mid = l + ((r - l) >>> 1);
            if(nums[mid] == target) r = mid - 1;
            else if(nums[mid] < target) l = mid + 1;
            else if(nums[mid] > target) r = mid - 1;
        }
        if(l >= nums.length || nums[l] != target) return -1;
        return l;
    }
    
    //查找右边界
    private int rightRange(int[] nums, int target){
        int l = 0, r = nums.length - 1;
        while(l <= r){
            int mid = l + ((r - l) >>> 1);
            if(nums[mid] == target) l = mid + 1;
            else if(nums[mid] < target) l = mid + 1;
            else if(nums[mid] > target) r = mid-1;
        }
        if(r < 0 || nums[r] != target) return -1;
        return r;
    }
}
```

### No.5 最长回文子串

```java
class Solution {
    public String longestPalindrome(String s) {
        //如果为空串,直接返回
        if (s == null || s.length() < 1) return "";
        
        //初始化回文串起点和终点
        int start = 0, end = 0;
        
        //从第一个字符向两边扩展
        for (int i = 0; i < s.length(); i++) {
            
            //字符长度为奇数
            int len1 = expandAroundCenter(s, i, i);
            //字符长度为偶数
            int len2 = expandAroundCenter(s, i, i + 1);
            //判断回文串最长长度
            int len = Math.max(len1, len2);
            //如果长度大于当前最长长度
            if (len > end - start) {
                //起始点为i左侧(len-1)/2
                start = i - (len - 1) / 2;
                //终止点为i右侧len/2
                end = i + len / 2;
            }
        }
        return s.substring(start, end + 1);
    }

    private int expandAroundCenter(String s, int left, int right) {
        int L = left, R = right;
        while (L >= 0 && R < s.length() && s.charAt(L) == s.charAt(R)) {
            L--;
            R++;
        }
        return R - L - 1;
    }
}
```

### No.21 合并两个有序链表

```java
class Solution {
    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        //如果其中一个链表为空,则连接非空链表
        if(l1 == null || l2 == null) return l1 == null ? l2 : l1;
        
        //判断两个结点的值,并连接值小的链表
        ListNode head = l1.val < l2.val ? l1 : l2;
        
        //继续连接
        head.next = head == l1 ? mergeTwoLists(head.next, l2) : mergeTwoLists(head.next, l1);
        
        return head;
    }
}
```

### No.42 接雨水

双指针

```java
class Solution {
    public int trap(int[] height) {
        int l = 0, r = height.length-1;
        int max_left = 0, max_right = 0;
        int res = 0;
        while(l <= r){
            //左边更低
            if(max_left < max_right){
                //左边最高高度高于当前柱子,可接雨水即为两个柱子的差值
                if(max_left >= height[l]){
                    res += max_left - height[l];
                }else max_left = height[l];
                l++;
            }else{
                if(max_right >= height[r]){
                    res += max_right - height[r];
                }else max_right = height[r];
                r--;
            }
        }
        return res;
    }
}
```

### No.54 螺旋矩阵

模拟

```java
class Solution {
    public List<Integer> spiralOrder(int[][] matrix) {
        ArrayList<Integer> res = new ArrayList<>();
        if(matrix.length == 0 || matrix[0].length == 0) return res;

        int m = matrix.length;
        int n = matrix[0].length;
        boolean[][] vis = new boolean[m][n];
        int [][]dir = new int[][]{{0,1},{1,0},{0,-1},{-1,0}};
        int x = 0, y = 0;
        int down = m, top = 0, left = 0, right = n;
        int nx = 0, ny = 0, d = 0;
        
        for(int i = 1; i <= m*n; i++){
            vis[x][y] = true;
            res.add(matrix[x][y]);
            nx = x + dir[d%4][0];
            ny = y + dir[d%4][1];
            if(nx >= down || nx < top || ny >= right || ny < left || vis[nx][ny]){
                d++;
                nx = x + dir[d%4][0];
                ny = y + dir[d%4][1];
            }
            x = nx;
            y = ny;
        }
        return res;
    }
}
```

### No.56 合并区间

```java
class Solution {
    public int[][] merge(int[][] intervals) {
        //根据区间的左端点排序
        Arrays.sort(intervals, (a, b) -> a[0]-b[0]);
        
        int i = 0;
        List<int[]> list = new ArrayList<>();
        
        while(i < intervals.length){
            int left = intervals[i][0], right = intervals[i][1];
            //当右端点大于等于后一个元素的左端点时
            while(i < intervals.length - 1 && right >= intervals[i+1][0]){
                i++;
                //判断是当前区间右端点更大还是下一个区间右端点更大
                right = Math.max(right, intervals[i][1]);
            }
            //合并完成
            list.add(new int[]{left, right});
            i++;
        }
        return list.toArray(new int[0][]);
    }
}
```

### No.124 二叉树的路径和

递归

```java
class Solution {
    int maxSum = Integer.MIN_VALUE;
    
    public int maxPathSum(TreeNode root) {
        dfs(root); 
        
        return maxSum;
    }
    
    private int dfs(TreeNode root){
        if(root == null) return 0;
        
        int left = Math.max(dfs(root.left), 0);
        int right = Math.max(dfs(root.right), 0);
        
        // 节点的最大路径和取决于该节点的值与该节点的左右子节点的最大贡献值
        int priceNewpath = root.val + left + right;

        // 更新答案
        maxSum = Math.max(maxSum, priceNewpath);

        // 返回节点的最大贡献值
        
        return root.val + Math.max(left, right);
    }
}
```

### No.146 LRU缓存机制

```java
class LRUCache {
    class DNode {
        DNode prev;
        DNode next;
        int val;
        int key;
    }
    
    Map<Integer, DNode> map = new HashMap<>();
    DNode head, tail;
    int cap;
    public LRUCache(int capacity) {
        head = new DNode();
        tail = new DNode();
        head.next = tail;
        tail.prev = head;
        cap = capacity;
    }
    
    public int get(int key) {
        if (map.containsKey(key)) {
            DNode node = map.get(key);
            removeNode(node);
            addToHead(node);
            return node.val;
        } else {
            return -1;
        }
    }
    
    public void put(int key, int value) {
        if (map.containsKey(key)) {
            DNode node = map.get(key);
            node.val = value;
            removeNode(node);
            addToHead(node);
        } else {
            DNode newNode = new DNode();
            newNode.val = value;
            newNode.key = key;
            addToHead(newNode);
            map.put(key, newNode);
            if (map.size() > cap) {
                map.remove(tail.prev.key);
                removeNode(tail.prev);
            }
        }
    }
    
    public void removeNode(DNode node) {
        DNode prevNode = node.prev;
        DNode nextNode = node.next;
        prevNode.next = nextNode;
        nextNode.prev = prevNode;
    }
    
    public void addToHead(DNode node) {
        DNode firstNode = head.next;
        head.next = node;
        node.prev = head;
        node.next = firstNode;
        firstNode.prev = node;
    }
}

/**
 * Your LRUCache object will be instantiated and called as such:
 * LRUCache obj = new LRUCache(capacity);
 * int param_1 = obj.get(key);
 * obj.put(key,value);
 */
```

### No.206 反转链表

头插法

```java
class Solution {
    public ListNode reverseList(ListNode head) {
        if(head == null) return null;
        ListNode p = head;
        head.next = null;
        p = p.next;
        while(p != null){
            ListNode q = p.next;
            p.next = head;
            head = p;
            p = q;
        }
        return head;
    }
}
```

双指针法

![img](https://pic.leetcode-cn.com/9ce26a709147ad9ce6152d604efc1cc19a33dc5d467ed2aae5bc68463fdd2888.gif)

```java
class Solution {
    public ListNode reverseList(ListNode head) {
        if(head == null || head.next == null) return head;
        ListNode pre = null, cur = head;
        while(cur != null){
            ListNode tmp = cur.next;
            cur.next = pre;
            pre = cur;
            cur = tmp;
        }
        return pre;
    }
}
```

### No.215 数组中的第K大数

优先队列

```java
class Solution {
    public int findKthLargest(int[] nums, int k) {
        PriorityQueue<Integer> q = new PriorityQueue<>(nums.length, (a,b) -> b-a);
        for(int i = 0; i < nums.length; i++) q.add(nums[i]);
        for(int i = 1; i < k; i++) q.poll();
        
        return q.peek();
    }
}
```

分治

```java

import java.util.Random;

public class Solution {

    private static Random random = new Random(System.currentTimeMillis());

    public int findKthLargest(int[] nums, int k) {
        int len = nums.length;
        int target = len - k;
        int left = 0;
        int right = len - 1;
        while (true) {
            int index = partition(nums, left, right);
            if (index < target) {
                left = index + 1;
            } else if (index > target) {
                right = index - 1;
            } else {
                return nums[index];
            }
        }
    }

    // 在区间 [left, right] 这个区间执行 partition 操作

    private int partition(int[] nums, int left, int right) {
        // 在区间随机选择一个元素作为标定点
        if (right > left) {
            int randomIndex = left + 1 + random.nextInt(right - left);
            swap(nums, left, randomIndex);
        }

        int pivot = nums[left];
        int j = left;
        for (int i = left + 1; i <= right; i++) {
            if (nums[i] < pivot) {
                j++;
                swap(nums, j, i);
            }
        }
        swap(nums, left, j);
        return j;
    }

    private void swap(int[] nums, int index1, int index2) {
        int temp = nums[index1];
        nums[index1] = nums[index2];
        nums[index2] = temp;
    }
} 
```

