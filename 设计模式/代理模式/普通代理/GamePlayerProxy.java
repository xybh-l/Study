package 代理模式.普通代理;

/**
 * @Author: xybh
 * @Description:
 * @Date: Created in 20:47 2021/3/8
 * @Modified:
 */
public class GamePlayerProxy implements IGamePlayer {
    private IGamePlayer player;
    public GamePlayerProxy(String name){
        this.player = new GamePlayer(this, name);
    }
    @Override
    public void login(String user, String password) {
        player.login(user, password);
    }

    @Override
    public void killBoss() {
        player.killBoss();
    }

    @Override
    public void upgrade() {
        player.upgrade();
    }
}
