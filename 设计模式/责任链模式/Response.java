package 责任链模式;

/**
 * @Author: xybh
 * @Description:
 * @Date: Created in 15:00 2021/3/9
 * @Modified:
 */
public class Response {
    public Response(Request request){
        System.out.println(request.getRequestLevel());
    }
}
