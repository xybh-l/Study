package 原型模式;

import java.util.Random;

/**
 * @Author: xybh
 * @Description:
 * @Date: Created in 10:20 2021/3/9
 * @Modified:
 */
public class Client {
    private static int MAX_COUNT = 6;
    public static void main(String[] args){
        int i = 0;
        Mail mail = new Mail(new AdvTemplate());
        mail.setTail("银行");
        while(i < MAX_COUNT){
            Mail cloneMail = mail.clone();
            cloneMail.setApplication("abcd"+ new Random().nextInt() + "先生/女士");
            cloneMail.setReceiver(new Random().nextInt()+"@"+new Random().nextInt()+".com");
            sendMail(cloneMail);
            i++;
        }
    }
    public static void sendMail(Mail mail){
        System.out.println("标题: "+ mail.getSubject() + "\t收件人:"+mail.getReceiver()+"\t...发送成功!");
    }
}
