package 代理模式.强制代理;

/**
 * @Author: xybh
 * @Description:
 * @Date: Created in 20:43 2021/3/8
 * @Modified:
 */
public class GamePlayer implements IGamePlayer {
    private volatile GamePlayerProxy proxy = null;
    private String name = "";

    public GamePlayer(String name){ this.name = name; }

    public GamePlayerProxy getProxy(){
        if(proxy == null){
            synchronized (GamePlayer.class){
                if(proxy == null){
                    proxy = new GamePlayerProxy(this);
                }
            }
        }
        return proxy;
    }

    public boolean isProxy(){
        return proxy != null;
    }
    @Override
    public void login(String user, String password) {
        if(isProxy()){
            System.out.println("登录名为"+ user +"的用户" + name + "登录了游戏");
        }else {
            System.out.println("请使用代理访问");
        }
    }

    @Override
    public void killBoss() {
        if(isProxy()){
            System.out.println(this.name + "在打怪");
        }else {
            System.out.println("请使用代理访问");
        }
    }

    @Override
    public void upgrade() {
        if(isProxy()){
            System.out.println(this.name+"又升了一级");
        }else {
            System.out.println("请使用代理访问");
        }
    }
}
