package 迭代器模式;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: xybh
 * @Description:
 * @Date: Created in 14:23 2021/3/9
 * @Modified:
 */
public class ConcreteAggregate implements  Aggregate{
    private List list = new ArrayList<>();

    @Override
    public void add(Object object) {
        list.add(object);
    }

    @Override
    public void remove(Object object) {
        list.remove(object);
    }

    @Override
    public Iterator iterator() {
        return new ConcreteIterator(this.list);
    }
}
