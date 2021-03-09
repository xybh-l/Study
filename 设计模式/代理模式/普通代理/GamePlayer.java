package 代理模式.普通代理;

/**
 * @Author: xybh
 * @Description:
 * @Date: Created in 20:53 2021/3/8
 * @Modified:
 */
public class GamePlayer implements IGamePlayer {
    private String name = "";

    public GamePlayer(IGamePlayer proxy, String name) {
        if (proxy == null) {
            throw new RuntimeException("不能创建真实账号");
        } else {
            this.name = name;
        }
    }

    @Override
    public void login(String user, String password) {
        System.out.println("登录名为" + user + "的用户" + name + "登录了游戏");
    }

    @Override
    public void killBoss() {
        System.out.println(this.name + "在打怪");
    }

    @Override
    public void upgrade() {
        System.out.println(this.name + "又升了一级");
    }
}
