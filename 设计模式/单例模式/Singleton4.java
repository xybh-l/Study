package 单例模式;

/**
 * @Author: xybh
 * @Description: 双重校验锁(线程安全)
 * @Date: Created in 17:46 2021/3/6
 * @Modified:
 */
public class Singleton4 {
    private static volatile Object instance;
    private Singleton4(){}

    public static Object getInstance(){
        if(instance == null){
            synchronized (Singleton4.class){
                if(instance == null){
                    instance = new Object();
                }
            }
        }
        return instance;
    }
}
