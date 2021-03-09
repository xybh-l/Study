package 迭代器模式;

/**
 * @Author: xybh
 * @Description:
 * @Date: Created in 14:25 2021/3/9
 * @Modified:
 */
public class Client {
    public static void main(String[] args) {
        ConcreteAggregate concreteAggregate = new ConcreteAggregate();
        concreteAggregate.add("abc");
        concreteAggregate.add("aaa");
        concreteAggregate.add("1234");
        Iterator iterator = concreteAggregate.iterator();
        System.out.println(iterator.next());
        iterator.remove();
        System.out.println(iterator.next());
    }
}
