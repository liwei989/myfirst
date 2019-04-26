import java.net.MalformedURLException;
import java.net.URL;

public class Copy {
    public static void main(String[] args) {
        try {
            URL url=new URL("http://www.baidu.com:80/en/index.html?name=liwei#first");
            System.out.println("protocol:"+url.getProtocol());
            System.out.println("port:"+url.getPort());
            System.out.println("port:"+url.getHost());
            System.out.println("file:"+url.getFile());
            System.out.println("query:"+url.getQuery());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

}
