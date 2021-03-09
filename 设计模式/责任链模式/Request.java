package 责任链模式;

/**
 * @Author: xybh
 * @Description:
 * @Date: Created in 14:57 2021/3/9
 * @Modified:
 */
public class Request {
    private Level requestLevel;
    public Level getRequestLevel(){
        return requestLevel;
    }
    public void setRequestLevel(Level requestLevel){
        this.requestLevel = requestLevel;
    }
}
