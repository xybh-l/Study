package 迭代器模式;

import java.util.List;

/**
 * @Author: xybh
 * @Description:
 * @Date: Created in 14:18 2021/3/9
 * @Modified:
 */
public class ConcreteIterator implements Iterator{
    private List list;
    private int cursor = 0;

    public ConcreteIterator(List list){
        this.list = list;
    }

    @Override
    public boolean hasNext() {
        return cursor < list.size() && list.get(cursor) != null;
    }

    @Override
    public Object next() {
        if(hasNext()){
            return list.get(cursor++);
        }
        return null;
    }

    @Override
    public boolean remove() {
        list.remove(cursor);
        return true;
    }
}
