package 代理模式.动态代理.Test;

/**
 * @Author: xybh
 * @Description:
 * @Date: Created in 1:23 2021/3/9
 * @Modified:
 */
public class RealSubject implements Subject{

    @Override
    public void doSomething() {
        System.out.println("call doSomethins()");
    }
}
