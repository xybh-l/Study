package 原型模式;

/**
 * @Author: xybh
 * @Description:
 * @Date: Created in 10:16 2021/3/9
 * @Modified:
 */
public class Mail implements Cloneable{
    private String receiver;
    private String subject;
    private String application;
    private String context;
    private String tail;

    public Mail(AdvTemplate advTemplate){
        this.context = advTemplate.getAdvContext();
        this.subject = advTemplate.getAdvSubject();
    }
    @Override
    public Mail clone(){
        try {
            return (Mail) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getTail() {
        return tail;
    }

    public void setTail(String tail) {
        this.tail = tail;
    }
}
