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



