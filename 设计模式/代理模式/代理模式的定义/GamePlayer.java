package 代理模式.代理模式的定义;

/**
 * @Author: xybh
 * @Description:
 * @Date: Created in 20:43 2021/3/8
 * @Modified:
 */
public class GamePlayer implements IGamePlayer{
    private String name = "";

    public GamePlayer(String name){ this.name = name; }

    @Override
    public void login(String user, String password) {
        System.out.println("登录名为"+ user +"的用户" + name + "登录了游戏");
    }

    @Override
    public void killBoss() {
        System.out.println(this.name + "在打怪");
    }

    @Override
    public void upgrade() {
        System.out.println(this.name+"又升了一级");
    }
}
