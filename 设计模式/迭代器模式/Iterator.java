package 迭代器模式;

/**
 * @Author: xybh
 * @Description:
 * @Date: Created in 14:17 2021/3/9
 * @Modified:
 */
public interface Iterator {
    boolean hasNext();
    Object next();
    boolean remove();
}
