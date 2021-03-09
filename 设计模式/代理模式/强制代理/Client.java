package 代理模式.强制代理;


/**
 * @Author: xybh
 * @Description:
 * @Date: Created in 20:46 2021/3/8
 * @Modified:
 */
public class Client {
    public static void main(String[] args) {
        GamePlayer gamePlayer = new GamePlayer("张三");
        GamePlayerProxy proxy = gamePlayer.getProxy();
        proxy.login("zhanshang", "password");
        proxy.killBoss();
        proxy.upgrade();
    }
}
