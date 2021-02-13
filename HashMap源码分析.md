  最近看了遍HashMap的源码,打算写一篇HashMap的源码解析,主要针对HashMap的增删改查操作进行分析，接下来直接进入正题。

  先看看hashMap在jdk1.8的结构，用的是数组+链表+红黑树的结构，也叫哈希桶，在jdk1.8之前都是数组+链表的结构，因为在链表的查询操作是O(N)的时间复杂度，而且hashMap中查询操作也是占了很大比例的，如果当节点数量多，转换为红黑树结构，那么将会提高很大的效率，因为红黑树结构中，增删改查都是O(log n)。

  哈希桶就是数组里面的一个位置中所占所有数据

<img src="http://image.xybh.online/HashMap%E7%BB%93%E6%9E%84.png" alt="HashMap结构" style="zoom: 67%;" />

## HashMap的属性

```java
// 序列号，用于序列化时使用
private static final long serialVersionUID = 362498820763181265L;
// 默认初始化大小(1 << 4, 16) - 必须要为2的次方
static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
// 最大容量(1 << 30, 1073741824)
static final int MAXIMUM_CAPACITY = 1 << 30;
// 负载因子，默认为0.75
static final float DEFAULT_LOAD_FACTOR = 0.75f;
// 树化阈值（与MIN_TREEIFY_CAPACITY同时作用,当HashMap大小超过64,且链表长度大于8时,将当前链表转化成红黑树)
static final int TREEIFY_THRESHOLD = 8;
// 当红黑树内的结点数小于6时,红黑树将退化成为链表
static final int UNTREEIFY_THRESHOLD = 6;
// 树化的最小容量，即当链表长度大于8，但HashMap大小小于64时，HashMap将进行扩容而不是链表转化为红黑树
static final int MIN_TREEIFY_CAPACITY = 64;

/* ---------------- 字段 -------------- */
// 存储元素的Node数组
transient Node<K,V>[] table;
// 将数据转换成set的另一种存储形式，这个变量主要用于迭代功能
transient Set<Map.Entry<K,V>> entrySet;
// 元素数量
transient int size;
// map被修改的次数
transient int modCount;
// 扩容阈值,当HashMap里的元素大于该数量时进行扩容
int threshold;
// 负载因子
final float loadFactor;
```

1. 负载因子为什么要是0.75，为什么不选取其他值？

   在HashMap的源码中有这么一段解释:

   ```
    Ideally, under random hashCodes, the frequency of
        * nodes in bins follows a Poisson distribution
        * (http://en.wikipedia.org/wiki/Poisson_distribution) with a
        * parameter of about 0.5 on average for the default resizing
        * threshold of 0.75, although with a large variance because of
        * resizing granularity. Ignoring variance, the expected
        * occurrences of list size k are (exp(-0.5) * pow(0.5, k) /
        * factorial(k)). The first values are:
        *
        * 0:    0.60653066
        * 1:    0.30326533
        * 2:    0.07581633
        * 3:    0.01263606
        * 4:    0.00157952
        * 5:    0.00015795
        * 6:    0.00001316
        * 7:    0.00000094
        * 8:    0.00000006
        * more: less than 1 in ten million
        *
   ```

   在理想情况下，使用随机哈希吗，节点出现的频率在hash桶中遵循泊松分布，同时给出了桶中元素的个数和概率的对照表。从上表可以看出当桶中元素到达8个的时候，概率已经变得非常小，也就是说用0.75作为负载因子，每个碰撞位置的链表长度超过8个是几乎不可能的。

   1. 当负载因子过小，设负载因子为0.5

   > 负载因子是0.5的时候，这也就意味着，当数组中的元素达到了一半就开始扩容，既然填充的元素少了，Hash冲突也会减少，那么底层的链表长度或者是红黑树的高度就会降低。查询效率就会增加。但是与此同时的空间利用率会大幅的降低，原来存储空间占用1M的数据，现在就意味着需要2M的空间。

   2. 当负载因子过大，设负载因子为1

   > 当负载因子是1.0的时候，也就意味着，只有当数组的8个值全部填充了，才会发生扩容。这就带来了很大的问题，因为Hash冲突是避免不了的。当负载因子是1.0的时候，意味着会出现大量的Hash的冲突，底层的红黑树变得异常复杂。对于查询效率极其不利。这种情况就是牺牲了时间来保证空间的利用率。

   **HashMap负载因子为0.75是空间和时间成本的一种折中。**

2. **易混点：**大家都知道HashMap底层是数组+链表/红黑树，并且当链表长度大于默认值为8时会将链表转化成红黑树。但是实际上还有另外一个参数*MIN_TREEIFY_CAPACITY*,只有当HashMap里的元素数量大于MIN_TREEIFY_CAPACITY(默认为64)时才会将链表转化成红黑树，这是为了避免在哈希表建立初期，多个键值对恰好被放入了同一个链表中而导致不必要的转化。

## HashMap的构造函数

看完了HashMap的属性,接下来我们来看看HashMap的构造函数，HashMap共有4个构造函数，首先是无参构造

```java
/**
* 无参构造
* 将loadFactor赋值为默认的负载因子0.75, 其他的参数为默认值
*/
public HashMap() {
    this.loadFactor = DEFAULT_LOAD_FACTOR; 
}
```

第二个构造函数，设置HashMap的初始大小和负载因子，并对默认大小和负载因子进行检测

```java
public HashMap(int initialCapacity, float loadFactor) {
    if (initialCapacity < 0)
        throw new IllegalArgumentException("Illegal initial capacity: " +
                                           initialCapacity);
    if (initialCapacity > MAXIMUM_CAPACITY)
        initialCapacity = MAXIMUM_CAPACITY;
    if (loadFactor <= 0 || Float.isNaN(loadFactor))
        throw new IllegalArgumentException("Illegal load factor: " +
                                           loadFactor);
    this.loadFactor = loadFactor;
    this.threshold = tableSizeFor(initialCapacity);
}
```

第三个，仅设置初始大小，负载因子使用默认值0.75并调用第二个构造函数进行初始化

```java
public HashMap(int initialCapacity) {
    this(initialCapacity, DEFAULT_LOAD_FACTOR);
}
```

最后一个构造方法看起来有点复杂，其实并不然。就是将一个Map转换成为HashMap。

1. 首先判断一下传进来的Map的大小是不是大于0（等于0就没必要初始化了），由于上图是通过构造器新建一个HashMap，所以table是null（table是HashMap的Node数组）。然后计算阀值ft，判断一下阀值是否超过了设定的最大容量（1<<30），如果没超过则还是原阀值ft。这里将ft赋给t，再判断阀值t是否大于原阀值
2. 我们假设它超过阀值了，这里用到tableSizeFor()方法进行扩容，我们后面会讲。参数evict可以忽略，这个参数传递到afterNodeInsertion(evict)，这个方法是空的。
3. 最后使用了forEach循环遍历，将传入HashMap构造器的形参m中的所有key-value全部放到当前HashMap对象中(this)。

```java
public HashMap(Map<? extends K, ? extends V> m) {
    this.loadFactor = DEFAULT_LOAD_FACTOR;
    putMapEntries(m, false);
}

final void putMapEntries(Map<? extends K, ? extends V> m, boolean evict) {
    // 获取map的大小
    int s = m.size();
    if (s > 0) {
        // 判断当前HashMap的table是否被初始化
        if (table == null) { // pre-size
            /* 计算需要的容量 实际使用的长度=容量*0.75得来的，+1是因为小数相除，基本都不会是整数，容量大小不能为小数的，后面转换为int，多余的小数就要被丢掉，所以+1，例如，map实际长度22，22/0.75=29.3,所需要的容量肯定为30*/
            float ft = ((float)s / loadFactor) + 1.0F;
            int t = ((ft < (float)MAXIMUM_CAPACITY) ?
                     (int)ft : MAXIMUM_CAPACITY);
            if (t > threshold)
                // 对HashMap进行容量设置
                threshold = tableSizeFor(t);
        }
        // 如果table已经初始化，则进行扩容操作
        else if (s > threshold)
            resize();
        // 遍历Map,将值复制进HashMap中
        for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
            K key = e.getKey();
            V value = e.getValue();
            putVal(hash(key), key, value, false, evict);
        }
    }
}
```

## HashMap常用函数

### 1.put()/putVal()添加元素

HashMap的构造函数我们学习完了,接下来必不可少的就是HashMap的增删改查操作和一些重要的方法了。

首先我们来看添加元素：

```java
public V put(K key, V value) {
    return putVal(hash(key), key, value, false, true);
}

/**
* 	计算hash值，与自己右移16位进行异或运算（高低位异或）
*   高16bit不变，低16bit和高16bit做了一个异或，目的是减少碰撞。
*   相比在1.7中的4次位运算，5次异或运算（9次扰动），在1.8中，只进行了1次位运算和1次异或运算（2次扰动）；
*/
static final int hash(Object key) {
    int h;
    return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}

final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
               boolean evict) {
    Node<K,V>[] tab; Node<K,V> p; int n, i;
    // 判断HashMap中的table是否被初始化或者是否为空
    if ((tab = table) == null || (n = tab.length) == 0)
        // 初次扩容
        n = (tab = resize()).length;
    // 计算index, 并对null进行处理
    // (n - 1) & hash 确定元素存放在哪个桶中，桶为空，新生成结点放入桶中(此时，这个结点是放在数组中)
    if ((p = tab[i = (n - 1) & hash]) == null)
        tab[i] = newNode(hash, key, value, null);
    else {
        // 结点中的key存在，直接覆盖value
        Node<K,V> e; K k;
        // 判断数组中的结点hash相等，key相等
        if (p.hash == hash &&
            ((k = p.key) == key || (key != null && key.equals(k))))
            // 如果相等，用e来记录当前结点
            e = p;
        // 判断该链是否为红黑树
        else if (p instanceof TreeNode)
            // 放入树中
            e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
        // 当前链为链表
        else {
            // 遍历链表，同时寻找key相同的结点，若找到跳出循环
            for (int binCount = 0; ; ++binCount) {
                // 到达链表末尾，将元素插入链表末尾
                if ((e = p.next) == null) {
                    p.next = newNode(hash, key, value, null);
                    // 判断当前链表结点数是否达到转化成红黑树的临界值
                    if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
                        // 链表树化
                        treeifyBin(tab, hash);
                    break;
                }
                // 判断链表中结点的key值与插入的元素的key值是否相等, 相同则跳出循环
                if (e.hash == hash &&
                    ((k = e.key) == key || (key != null && key.equals(k))))
                    break;
                // 遍历下一个结点
                p = e;
            }
        }
        // 如果e不为空，即HashMap中存在相同的key
        if (e != null) { // existing mapping for key
            V oldValue = e.value;
            // onlyIfAbsent为false或者旧值为null
            if (!onlyIfAbsent || oldValue == null)
                //用新值替换旧值
                e.value = value;
            // 访问后回调
            afterNodeAccess(e);
            // 返回旧值
            return oldValue;
        }
    }
    // 修改次数加一
    ++modCount;
    // 如果添加元素后元素数量大于最大容量,则扩容
    if (++size > threshold)
        resize();
    // 插入后回调
    afterNodeInsertion(evict);
    return null;
}
```

上面就是HashMap的一个添加元素的过程，其中涉及到了多次的resize()扩容方法，这是HashMap中的一个重难点。

接下来用文字和流程图来总结一下HashMap的添加元素过程

1. 判断键值对数组table是否为null或者元素为空，是则将使用resize()方法对table进行初次初始化扩容

2. 使用(n-1)&hash计算索引i来记录元素应该存放在数组的位置，判断是否table[i]==null来判断是否存在hash冲突，不存在则直接将元素存入数组，转向8

3. 判断table[i]是否与需要插入的元素hash值相等，元素相同，如果相同直接覆盖value，后者转向4

4. 判断table[i]是否为TreeNode,即判断table[i]是否为红黑树，是则使用putTreeVal()方法直接放入树中，转向；否则转向5

5. 如果当前table[i]不为红黑树,则当前结点为链表，遍历当前链表，判断是否有存在相同元素，如存在则记录当前结点，并跳出循环转向7，否则在链表末端插入元素

6. 判断插入后的链表长度是否大于树化阈值（默认为8），大于的话则使用treeifyBin()树化链表，转向8

7. 将记录下来的结点的值进行覆盖

8. 插入成功后，判断实际存在的键值对数量size是否超多了最大容量threshold，如果超过，进行扩容

   <img src="http://image.xybh.online/HashMap%E6%8F%92%E5%85%A5%E7%BB%93%E7%82%B9.png" alt="HashMap插入结点" style="zoom: 25%;" />

### 2.resize()扩容函数

在添加元素的过程中多次涉及到了resize()函数对HashMap的容量进行动态扩容,接下来我们就接着看源码来分析一下resize()函数

```java
final Node<K,V>[] resize() {
    // oldTab指向hash桶数组
    Node<K,V>[] oldTab = table;
    // 判断当前HashMap是否为空
    int oldCap = (oldTab == null) ? 0 : oldTab.length;
    // 原扩容阈值(即容量*负载因子)
    int oldThr = threshold;
    int newCap, newThr = 0;
    if (oldCap > 0) {
        // 如果原数组空间已经大于最大容量空间无法扩容时，直接返回原数组
        if (oldCap >= MAXIMUM_CAPACITY) {
            threshold = Integer.MAX_VALUE;
            return oldTab;
        }
        //如果当前hash桶数组的长度在扩容后仍然小于最大容量 并且oldCap大于默认值16
        else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
                 oldCap >= DEFAULT_INITIAL_CAPACITY)
            newThr = oldThr << 1; // 两倍化扩容阈值
    }
    // 旧的容量为0，但threshold大于零，代表有参构造有cap传入，threshold已经被初始化成最小2的n次幂
    // 直接将该值赋给新的容量
    else if (oldThr > 0)
        newCap = oldThr;
    // 无参构造,将容量和扩容阈值设置成默认值
    else {            
        newCap = DEFAULT_INITIAL_CAPACITY;
        // 默认负载因子*默认初始化容量
        newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
    }
    if (newThr == 0) {
        float ft = (float)newCap * loadFactor;
        newThr = (newCap < MAXIMUM_CAPACITY && ft < (float)MAXIMUM_CAPACITY ?
                  (int)ft : Integer.MAX_VALUE);
    }
    threshold = newThr;
    @SuppressWarnings({"rawtypes","unchecked"})
    // 计算出新的数组长度后赋给当前成员变量table
    Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];
    table = newTab;
    // 如果原数组不为空,则需要重排逻辑
    if (oldTab != null) {
        // 遍历元数组的所有桶下标
        for (int j = 0; j < oldCap; ++j) {
            Node<K,V> e;
            //当前哈希桶的位置值不为null，也就是数组下标处有值，因为有值表示可能会发生冲突
            if ((e = oldTab[j]) != null) {
               	// 旧数组的桶下标赋给临时变量e，并且解除旧数组中的引用，否则就数组无法被GC回收
                oldTab[j] = null;
                // 当前桶只有一个元素
                if (e.next == null)
                    // 用hash算法重排元素位置
                    newTab[e.hash & (newCap - 1)] = e;
                // 如果当前桶不为空，且存在红黑树
                else if (e instanceof TreeNode)
                   	// 将当前红黑树中的元素分隔重排
                    ((TreeNode<K,V>)e).split(this, newTab, j, oldCap);
                else { // preserve order
                    Node<K,V> loHead = null, loTail = null;
                    Node<K,V> hiHead = null, hiTail = null;
                    Node<K,V> next;
                    // 遍历链表
                    do {
                        next = e.next;
                        if ((e.hash & oldCap) == 0) {
                            if (loTail == null)
                                // 初始化head指向链表当前元素e，e不一定是链表的第一个元素，初始化后loHead
                                // 代表下标保持不变的链表的头元素
                                loHead = e;
                            else
                                // loTail.next指向当前e
                                loTail.next = e;
                            // loTail指向当前的元素e
                            // 初始化后，loTail和loHead指向相同的内存，所以当loTail.next指向下一个元素时，
                            // 底层数组中的元素的next引用也相应发生变化，造成lowHead.next.next.....
                            // 跟随loTail同步，使得lowHead可以链接到所有属于该链表的元素。
                            loTail = e;
                        }
                        else {
                            if (hiTail == null)
                                // 初始化head指向链表当前元素e, 初始化后hiHead代表下标更改的链表头元素
                                hiHead = e;
                            else
                                hiTail.next = e;
                            hiTail = e;
                        }
                    } while ((e = next) != null);
                    // 遍历结束, 将tail指向null，并把链表头放入新数组的相应下标，形成新的映射。
                    if (loTail != null) {
                        loTail.next = null;
                        newTab[j] = loHead;
                    }
                    if (hiTail != null) {
                        hiTail.next = null;
                        newTab[j + oldCap] = hiHead;
                    }
                }
            }
        }
    }
    return newTab;
}
```

### 3.remove()删除函数

讲完了增删改查中的增，接下来我们接着看删这个步骤，同样的在删的这个步骤中也没有直接在remove函数中完成详细的删除细节，而是在removeNode中具体实现

```java
public V remove(Object key) {
    Node<K,V> e;
    // 判断key是否存在，存在的话返回value否则返回null
    return (e = removeNode(hash(key), key, null, false, true)) == null ?
        null : e.value;
}

final Node<K,V> removeNode(int hash, Object key, Object value,
                           boolean matchValue, boolean movable) {
    Node<K,V>[] tab; Node<K,V> p; int n, index;
    // 判断table不为空，table的长度大于0，然后获取删除key的结点所在数组的下标的值
    if ((tab = table) != null && (n = tab.length) > 0 &&
        (p = tab[index = (n - 1) & hash]) != null) {
        Node<K,V> node = null, e; K k; V v;
        // 判断当前桶的第一个元素是否就是需要查找的元素
        if (p.hash == hash &&
            ((k = p.key) == key || (key != null && key.equals(k))))
            node = p;
        else if ((e = p.next) != null) {
            // 判断当前桶是否为红黑树结构
            if (p instanceof TreeNode)
                // 获取树中结点
                node = ((TreeNode<K,V>)p).getTreeNode(hash, key);
            else {
                // 遍历链表, 获取结点
                do {
                    if (e.hash == hash &&
                        ((k = e.key) == key ||
                         (key != null && key.equals(k)))) {
                        node = e;
                        break;
                    }
                    p = e;
                } while ((e = e.next) != null);
            }
        }
        //找到要删除的节点后，判断!matchValue，我们正常的remove删除，!matchValue都为true
        if (node != null && (!matchValue || (v = node.value) == value ||
                             (value != null && value.equals(v)))) {
            // 如果是红黑树结点,调用removeTreeNode删除树中结点
            if (node instanceof TreeNode)
                ((TreeNode<K,V>)node).removeTreeNode(this, tab, movable);
            // 如果是桶的第一个结点,直接删除头结点
            else if (node == p)
                tab[index] = node.next;
            // 不为头结点,将next指针指向下一个元素
            else
                p.next = node.next;
            // 修改次数加一
            ++modCount;
            // HashMap中元素数量减一
            --size;
            // 回调函数
            afterNodeRemoval(node);
            // 返回结点
            return node;
        }
    }
    // 如果没有查找到结点,返回null
    return null;
}
```

 删除还有clear方法，把所有的数组下标元素都置位null。

```java
public void clear() {
    Node<K,V>[] tab;
    modCount++;
    if ((tab = table) != null && size > 0) {
        size = 0;
        for (int i = 0; i < tab.length; ++i)
            tab[i] = null;
    }
}
```

### 4.get()获取元素函数

相比于增加元素和删除元素来讲，查找元素的过程就很简单了，通过比对hash值先判断元素是否存在，在根据桶类型来获取元素。

```java
public V get(Object key) {
    Node<K,V> e;
    //也是调用getNode方法来完成的
    return (e = getNode(hash(key), key)) == null ? null : e.value;
}

final Node<K,V> getNode(int hash, Object key) {
    //first 头结点，e 临时变量，n 长度,k key
    Node<K,V>[] tab; Node<K,V> first, e; int n; K k;
    //头结点也就是数组下标的节点
    if ((tab = table) != null && (n = tab.length) > 0 &&
        (first = tab[(n - 1) & hash]) != null) {
        //如果是头结点，则直接返回头结点
        if (first.hash == hash && 
            ((k = first.key) == key || (key != null && key.equals(k))))
            return first;
        //不是头结点
        if ((e = first.next) != null) {
            //判断是否是红黑树结构
            if (first instanceof TreeNode)
                //去红黑树中找，然后返回
                return ((TreeNode<K,V>)first).getTreeNode(hash, key);
            do { //链表节点，一样遍历链表，找到该节点并返回
                if (e.hash == hash &&
                    ((k = e.key) == key || (key != null && key.equals(k))))
                    return e;
            } while ((e = e.next) != null);
        }
    }
    //找不到，表示不存在该节点
    return null;
}
```

### 5.replace()/replaceAll()修改元素函数

为什么先说获取元素而不是修改元素呢，其实通过我们分析增加元素的源码时我们就已经发现使用put()可以添加相同的元素，存在相同的元素就会直接覆盖value，这其实就达到了覆盖的目的，我在这稍微提一嘴，虽然可以通过put来覆盖数据，但其实HashMap的源码中其实是有replace()和replaceAll()替换函数的，这里给大家简单分析一下。

```java
/**
 * 使用newValue来替换key中的oldValue
 * @return 是否替换成功
 */
@Override
public boolean replace(K key, V oldValue, V newValue) {
    Node<K,V> e; V v;
    // 判断key是否存在, 用e获取到key所在的结点，直接替换结点
    if ((e = getNode(hash(key), key)) != null &&
        ((v = e.value) == oldValue || (v != null && v.equals(oldValue)))) {
        e.value = newValue;
        afterNodeAccess(e);
        return true;
    }
    return false;
}

/**
 * 使用value来替换key中的value
 * @return key中原来的值
 */
@Override
public V replace(K key, V value) {
    Node<K,V> e;
    // 判断key是否存在, 用e获取到key所在的结点
    if ((e = getNode(hash(key), key)) != null) {
        // 保存原值
        V oldValue = e.value;
        e.value = value;
        afterNodeAccess(e);
        // 返回原值
        return oldValue;
    }
    return null;
}

@Override
public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
    Node<K,V>[] tab;
    if (function == null)
        throw new NullPointerException();
    if (size > 0 && (tab = table) != null) {
        int mc = modCount;
        for (int i = 0; i < tab.length; ++i) {
            for (Node<K,V> e = tab[i]; e != null; e = e.next) {
                e.value = function.apply(e.key, e.value);
            }
        }
        if (modCount != mc)
            throw new ConcurrentModificationException();
    }
}
```

HashMap的源码分析暂时到这里，主要的增删改查操作都已经分析完了，因个人能力有限，如果里面内容存在错误，欢迎批评指正。后续会添加一些关于HashMap的面试题，欢迎大家关注。

