package 责任链模式;

/**
 * @Author: xybh
 * @Description:
 * @Date: Created in 15:12 2021/3/9
 * @Modified:
 */
public class Client {
    public static void main(String[] args) {
        ConcreteHandler1 handler1 = new ConcreteHandler1();
        ConcreteHandler2 handler2 = new ConcreteHandler2();
        ConcreteHandler3 handler3 = new ConcreteHandler3();
        handler1.setNext(handler2);
        handler2.setNext(handler3);
        Request r1 = new Request();
        Request r2 = new Request();
        Request r3 = new Request();
        r1.setRequestLevel(Level.FIRST);
        r2.setRequestLevel(Level.SECOND);
        r3.setRequestLevel(Level.THIRD);
        Response response1 = handler1.handleMessage(r1);
        Response response2 = handler1.handleMessage(r2);
        Response response3 = handler1.handleMessage(r3);
        System.out.println(response1);
        System.out.println(response2);
        System.out.println(response3);
    }
}
