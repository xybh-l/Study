package 代理模式.动态代理;

/**
 * @Author: xybh
 * @Description:
 * @Date: Created in 20:53 2021/3/8
 * @Modified:
 */
public interface IGamePlayer {
    void login(String user, String password);

    void killBoss();

    void upgrade();
}
