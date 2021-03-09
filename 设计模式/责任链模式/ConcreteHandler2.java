package 责任链模式;

/**
 * @Author: xybh
 * @Description:
 * @Date: Created in 15:06 2021/3/9
 * @Modified:
 */
public class ConcreteHandler2 extends Handler{
    @Override
    protected Level getHandlerLevel() {
        return Level.SECOND;
    }

    @Override
    protected Response echo(Request request) {
        System.out.println("handler2处理");
        return new Response(request);
    }
}
