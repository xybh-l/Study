package 代理模式.代理模式的定义;

/**
 * @Author: xybh
 * @Description:
 * @Date: Created in 20:46 2021/3/8
 * @Modified:
 */
public class Client {
    public static void main(String[] args) {
        IGamePlayer player = new GamePlayer("张三");
        GamePlayerProxy proxy = new GamePlayerProxy(player);
        proxy.login("zhansan", "password");
        proxy.killBoss();
        proxy.upgrade();
    }
}
