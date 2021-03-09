package 代理模式.动态代理;

import java.lang.reflect.Proxy;

/**
 * @Author: xybh
 * @Description:
 * @Date: Created in 23:42 2021/3/8
 * @Modified:
 */
public class Client {
    public static void main(String[] args) {
        IGamePlayer player = new GamePlayer("张三");
        GamePlayIH handler = new GamePlayIH(player);
        // 类加载器
        ClassLoader cl = player.getClass().getClassLoader();
        IGamePlayer proxy = (IGamePlayer) Proxy.newProxyInstance(cl, player.getClass().getInterfaces(), handler);
        proxy.login("zhangsan", "password");
        proxy.killBoss();
        proxy.upgrade();
    }
}
