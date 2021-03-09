package 单例模式;

/**
 * @Author: xybh
 * @Description: 饿汉模式(线程安全)
 * @Date: Created in 17:46 2021/3/6
 * @Modified:
 */
public class Singleton3 {
    private static final Object instance = new Object();
    private Singleton3(){}

    public static synchronized Object getInstance(){
        return instance;
    }
}
