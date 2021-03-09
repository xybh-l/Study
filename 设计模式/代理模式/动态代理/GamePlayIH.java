package 代理模式.动态代理;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @Author: xybh
 * @Description:
 * @Date: Created in 23:38 2021/3/8
 * @Modified:
 */
public class GamePlayIH implements InvocationHandler {
    /**
     * 被代理的实例
     */
    Object obj = null;
    /**
     * 被代理者
     */
    Class cls = null;
    public GamePlayIH(Object obj){
        this.obj = obj;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return method.invoke(this.obj, args);
    }
}
