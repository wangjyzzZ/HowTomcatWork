package ex02;

import javax.servlet.Servlet;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;

public class ServletProcessor1 {

    public void process(Request request, Response response) {
        String uri = request.getUri();
        String servletName = uri.substring(uri.lastIndexOf("/") + 1);
        URLClassLoader loader = null;

        try {
            URL[] urls = new URL[1];
            URLStreamHandler streamHandler = null;
            File classPath = new File(Constants.WEB_ROOT);
            String repository = (new URL("file", null, classPath.getCanonicalPath() + File.separator)).toString();
            urls[0] = new URL(null, repository, streamHandler);
            loader = new URLClassLoader(urls);
        } catch (Exception e) {
            System.out.println(e);
        }

        Class myClass = null;
        try {
            myClass = loader.loadClass(servletName);
        } catch (Exception e) {
            System.out.println(e);
        }

        Servlet servlet = null;
        try {
            if (myClass != null) {
                servlet = (Servlet) myClass.newInstance();
                servlet.service(request, response);
            }
        } catch (Throwable e) {
            System.out.println(e);
        }
    }
}
