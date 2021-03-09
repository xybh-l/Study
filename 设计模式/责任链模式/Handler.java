package 责任链模式;

/**
 * @Author: xybh
 * @Description:
 * @Date: Created in 14:58 2021/3/9
 * @Modified:
 */
public abstract class Handler {
    private Handler nextHandler;
    public void setNext(Handler handler){
        this.nextHandler = handler;
    }
    public final Response handleMessage(Request request){
        Response response = null;
        if(this.getHandlerLevel().equals(request.getRequestLevel())){
            response = this.echo(request);
        }else{
            if(this.nextHandler != null){
                response = this.nextHandler.handleMessage(request);
            }else {
                throw new RuntimeException("没有适当的处理器");
            }
        }
        return response;
    }

    protected abstract Level getHandlerLevel();

    protected abstract Response echo(Request request);
}
