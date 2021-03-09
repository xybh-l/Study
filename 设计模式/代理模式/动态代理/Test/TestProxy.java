package 代理模式.动态代理.Test;

/**
 * @Author: xybh
 * @Description:
 * @Date: Created in 1:26 2021/3/9
 * @Modified:
 */
public class TestProxy {
    public static void main(String[] args) {
        Subject subject = (Subject) new ProxyHandler().bind(new RealSubject());
        subject.doSomething();
    }
}
