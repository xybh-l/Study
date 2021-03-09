package 单例模式;

/**
 * @Author: xybh
 * @Description: 懒汉模式(线程不安全)
 * @Date: Created in 17:46 2021/3/6
 * @Modified:
 */
public class Singleton1 {
    private static Object instance;
    private Singleton1(){}

    public static Object getInstance(){
        if(instance == null) {
            instance = new Object();
        }
        return instance;
    }
}
