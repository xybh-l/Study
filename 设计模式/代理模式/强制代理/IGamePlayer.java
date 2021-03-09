package 代理模式.强制代理;

/**
 * @Author: xybh
 * @Description:
 * @Date: Created in 20:41 2021/3/8
 * @Modified:
 */
public interface IGamePlayer {
    void login(String user, String password);

    void killBoss();

    void upgrade();
}
