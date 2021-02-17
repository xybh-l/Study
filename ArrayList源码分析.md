# ArrayList源码分析

在面试过程中,难免会遇到对Java集合的一个考查，涉及到集合必不可少的就是ArrayList，LinkedList，HashMap等几个常见的集合类，本文将通过分析源码对ArrayList在面试中常见的重难点进行解析。

### 一、属性

首先我们先看到ArrayList的属性值

```java
public class ArrayList<E> extends AbstractList<E>
        implements List<E>, RandomAccess, Cloneable, java.io.Serializable
{
    // 序列化ID
    private static final long serialVersionUID = 8683452581122892189L;
    // ArrayList默认容量(10)
    private static final int DEFAULT_CAPACITY = 10;
    // 用无参构造的共享空数组实例
    private static final Object[] EMPTY_ELEMENTDATA = {};
	// 用于无参构造的实例大小共享空的数组实例。
    private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};
    /**
     * 元素数组
     * tips: 当添加第一个元素时，elementData的容量将被扩展成DEFAULT_CAPACITY
     *       当数组为空时elementData==EMPTY_ELEMENTDATA
     */
    transient Object[] elementData;
    //ArrayList中存储元素数量
    private int size;
    // 数组可分配容量的最大值,所JVM限制,默认为Integer类型的最大值-8
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
}
```

ArrayList基于List接口实现,继承了AbstracList类，实现了List接口中的所有方法，在这些属性中比较重要的有

- ArrayList的默认容量DEFAULT_CAPACITY(10)，未指定容量时，ArrayList默认容量为10
- DEFAULTCAPACITY_EMPTY_ELEMENTDATA数组，ArrayList中设置了一个不可修改的空数组，用于在创建ArrayList将elementData赋值为DEFAULTCAPACITY_EMPTY_ELEMENTDATA
- MAX_ARRAY_SIZE:ArrayList的最大容量受不同VM限制，默认为Integer.MAX_VALUE - 8，在某些情况下，ArrayList的容量大小最大可为Integer.MAX_VALUE

### 二、构造函数

```java
 	/**
     * 使用自定义的数组容量来创建数组, 数组容量为0时相当于无参构造
     */
    public ArrayList(int initialCapacity) {
        if (initialCapacity > 0) {
            // 当初始化容量大于0时
            this.elementData = new Object[initialCapacity];
        } else if (initialCapacity == 0) {
            // 当初始化容量等于0时，将elementData置为EMPTY_ELEMETDATA(空数组)
            this.elementData = EMPTY_ELEMENTDATA;
        } else {
            throw new IllegalArgumentException("Illegal Capacity: "+
                    initialCapacity);
        }
    }
    /**
     * 无参构造函数，将用默认容量10来初始化数组
     */
    public ArrayList() {
        // 默认容量空数组(用于无参构造)
        this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
    }

    /**
     * 使用Collection容器类来初始化ArrayList，使用Arrays.copyOf()来复制容器内的元素
     */
    public ArrayList(Collection<? extends E> c) {
        // 将c变成数组,将生成的数组赋值给elementData
        elementData = c.toArray();
        if ((size = elementData.length) != 0) {
            // c.toArray might (incorrectly) not return Object[] (see 6260652)
            // c.toArray()可能返回的不是Object数组，可能被重写
            if (elementData.getClass() != Object[].class)
                elementData = Arrays.copyOf(elementData, size, Object[].class);
        } else {
            // 初始化大小为0，用EMPTY_ELEMENTDATA来初始化数组
            this.elementData = EMPTY_ELEMENTDATA;
        }
    }
```

> ArrayList使用了3种构造方式，其中无参构造中是将elementData赋值为DEAFAULTCAPACITY_EMPTY_ELEMENTDATA,容量为默认容量，而使用了初始化容量的构造函数，如果初始化容量大于0则new一个数组，容量为0时则将elementData赋值为EMPTY_ELEMENTDATA。