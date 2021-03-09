package 单例模式;

/**
 * @Author: xybh
 * @Description: 懒汉模式(线程安全)
 * @Date: Created in 17:46 2021/3/6
 * @Modified:
 */
public class Singleton2 {
    private static Object instance;
    private Singleton2(){}

    public static synchronized Object getInstance(){
        if(instance == null) {
            instance = new Object();
        }
        return instance;
    }
}
