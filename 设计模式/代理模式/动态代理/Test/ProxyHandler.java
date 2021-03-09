package 代理模式.动态代理.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @Author: xybh
 * @Description:
 * @Date: Created in 1:24 2021/3/9
 * @Modified:
 */
public class ProxyHandler implements InvocationHandler {
    private Object obj;
    public Object bind(Object obj){
        this.obj = obj;
        return Proxy.newProxyInstance(obj.getClass().getClassLoader(), obj.getClass().getInterfaces(), this);
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("前置通知");
        Object o = method.invoke(obj, args);
        System.out.println("后置通知");
        return o;
    }
}
