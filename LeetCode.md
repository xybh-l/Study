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

