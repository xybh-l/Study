package 代理模式.普通代理;


/**
 * @Author: xybh
 * @Description:
 * @Date: Created in 20:46 2021/3/8
 * @Modified:
 */
public class Client {
    public static void main(String[] args) {
        // 不用去涉及真实类是如何实现的
        GamePlayerProxy proxy = new GamePlayerProxy("张三");
        proxy.login("zhansan", "password");
        proxy.killBoss();
        proxy.upgrade();
    }
}
