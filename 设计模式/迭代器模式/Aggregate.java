package 迭代器模式;

/**
 * @Author: xybh
 * @Description:
 * @Date: Created in 14:23 2021/3/9
 * @Modified:
 */
public interface Aggregate {
    void add(Object object);
    void remove(Object object);
    Iterator iterator();
}
