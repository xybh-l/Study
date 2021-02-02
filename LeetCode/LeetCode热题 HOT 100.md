# :fire: LeetCode热题 HOT 100

## 1.两数之和(简单)

```java
class Solution {
    public int[] twoSum(int[] nums, int target) {
        // 1.暴力 O(n^2)
        for(int i = 0; i < nums.length - 1; i++){
            for(int j = i + 1; j < nums.length; j++){
                if(nums[i] + nums[j] == target){
                    return new int[] {i, j};
                }
            }
        }
        return new int[]{-1, -1};
        
        // 2.hash O(n)
        HashMap<Integer, Integer> map = new HashMap<>();
        for(int i = 0; i < nums.length; i++){
            int res = target - nums[i];
            if(map.containsKey(res)) return new int[]{i, map.get(res)};
            else map.put(nums[i], i);
        }
        return new int[]{-1,-1};
    }
}
```

## 2.两数相加(中等)

```java
class Solution {
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        // 模拟 O(m+n)
        if(l1 == null || l2 == null) return l1 == null ? l2 : l1;
        ListNode head = new ListNode(0);
        ListNode p = head;
        int carry = 0;
        while(l1 != null || l2 != null){
            int v1 = l1 == null ? 0 : l1.val;
            int v2 = l2 == null ? 0 : l2.val;
            int sum = v1 + v2 + carry;
            
            carry = sum / 10;
            p.next = new ListNode(sum % 10);
            p = p.next;
            
            if(l1 != null) l1 = l1.next;
            if(l2 != null) l2 = l2.next;
        }
        if(carry == 1) p.next = new ListNode(1);
        return head.next;
    }
}
```

3.无重复字符串的最长子串(中等)

```java
class Solution {
    public int lengthOfLongestSubstring(String s) {
        // 双指针 O(n)
        if (s.length()==0) return 0;
        HashMap<Character, Integer> map = new HashMap<Character, Integer>();
        int max = 0;
        int left = 0;
        for(int i = 0; i < s.length(); i ++){
            if(map.containsKey(s.charAt(i))){
                left = Math.max(left,map.get(s.charAt(i)) + 1);
            }
            map.put(s.charAt(i),i);
            max = Math.max(max,i-left+1);
        }
        return max;
        
    }
}
```

## 3.寻找两个有序数组的中位数(困难)

```java
class Solution {
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        // 模拟 O(n1+n2)
        int i = 0, j = 0, n1 = nums1.length, n2 = nums2.length, len = 0;
        if(n1 == 0 && n2 == 0) return 0;
        int [] res = new int[n1+n2];
        while(i < n1 && j < n2){
            if(nums1[i] < nums2[j]){
                res[len++] = nums1[i++];
            }else res[len++] = nums2[j++];
        }
        while(i < n1) res[len++] = nums1[i++];
        while(j < n2) res[len++] = nums2[j++];
        return ((n1+n2)&1)==1?res[len/2]:(res[len/2]+res[len/2-1])/2.0;
    }
}
```

## 4、最长回文子串(中等)

```java
class Solution {
    public String longestPalindrome(String s) {
        //中心扩展 O(n^2)
         if(s == null || s.length() < 1) return "";
        
         int start = 0, end = 0;
         for(int i = 0; i < s.length(); i++){
             int len1 = expandMid(s, i, i);
             int len2 = expandMid(s, i, i+1);
             int len = Math.max(len1, len2);
             if(len > end - start){
                 start = i - (len - 1) / 2;
                 end = i + len / 2;
             }
         }
         return s.substring(start, end+1);
     }
    
     private int expandMid(String s, int start, int end){
         int l = start, r = end;
         while(l >= 0 && r < s.length() && s.charAt(l) == s.charAt(r)){
             l--;
             r++;
         }
         return r - l - 1;
     }
        
        //DP
        if(s == null || s.length() < 1) return "";
        int len = s.length();
        boolean[][] dp = new boolean[len][len];
        String ans = "";
        
        for(int l = 0; l < len; l++){
            for(int i = 0; i < len - l; i++){
                int j = l + i;
                if(l == 0){
                    dp[i][j] = true;
                }else if(l == 1){
                    dp[i][j] = s.charAt(i) == s.charAt(j);
                }else{
                    dp[i][j] = s.charAt(i) == s.charAt(j) && dp[i+1][j-1];
                }
                
                if (dp[i][j] && l + 1 > ans.length()) {
                    ans = s.substring(i, i + l + 1);
                }
            }
        }
        return ans;
    }
}
```

## 5、盛最多水的容器(中等)

```java
class Solution {
    public int maxArea(int[] height) {
        // 双指针O(n)
        int l = 0, r = height.length - 1, max = Integer.MIN_VALUE;
        while(l < r){
            if(height[l] < height[r]){
                max = Math.max(max, height[l] * (r-l));
                l++;
            }else{
                max = Math.max(max, height[r] * (r-l));
                r--;
            }
        }
        return max;
    }
}
```

6、三数之和（中等）

```java
class Solution {
    public List<List<Integer>> threeSum(int[] nums) {
        Arrays.sort(nums);
        List result = new ArrayList<>(nums.length/3);
        int v, l, r;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] > 0) break;
            if (i > 0 && nums[i] == nums[i - 1]) continue;
            l = i + 1;
            r = nums.length - 1;
            while (l < r) {
                v = nums[i] + nums[l] + nums[r];
                if (v == 0) {
                    result.add(new int[]{nums[i], nums[l++], nums[r--]});
                    while (l < r && nums[l] == nums[l - 1]) l++;
                    while (l < r && nums[r] == nums[r + 1]) r--;
                } else if (v < 0){
                    l++;
                } else {
                    r--;
                }
            }
        }
        return result;
    }
}
```







