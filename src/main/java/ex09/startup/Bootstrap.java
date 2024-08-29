package ex09.startup;

import ex09.core.SimpleContextConfig;
import ex09.core.SimpleWrapper;
import org.apache.catalina.*;
import org.apache.catalina.connector.http.HttpConnector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.loader.WebappClassLoader;
import org.apache.catalina.loader.WebappLoader;
import org.apache.catalina.session.StandardManager;

import java.io.File;

public class Bootstrap {

    public void start() {
        String path = "";
        try {
            path = new File(Bootstrap.class.getResource("/webroot").toURI()).getPath();
        } catch (Exception e) {
            //
        }
        System.out.println(path);
        System.setProperty("catalina.base", path);
        Connector connector = new HttpConnector();
        Wrapper wrapper1 = new SimpleWrapper();
        wrapper1.setName("Session");
        wrapper1.setServletClass("webroot.myApp.SessionServlet");

        Context context = new StandardContext();
        //Context context = new SimpleContext();
        // StandardContext's start method adds a default mapper
        context.setPath("/myApp");
        context.setDocBase("myApp");

        context.addChild(wrapper1);

        context.addServletMapping("/myApp/Session", "Session");
        context.addServletMapping("/Session", "Session");

        LifecycleListener listener = new SimpleContextConfig();
        ((Lifecycle) context).addLifecycleListener(listener);

        // here is our loader
        Loader loader = new WebappLoader();
        // associate the loader with the Context
        context.setLoader(loader);

        connector.setContainer(context);

        Manager manager = new StandardManager();
        context.setManager(manager);

        try {
            connector.initialize();
            ((Lifecycle) connector).start();
            ((Lifecycle) context).start();
            // now we want to know some details about WebappLoader
            WebappClassLoader classLoader = (WebappClassLoader) loader.getClassLoader();
            String[] repositories = classLoader.findRepositories();
            for (int i=0; i<repositories.length; i++) {
                System.out.println("  repository: " + repositories[i]);
            }

            // make the application wait until we press a key.
            System.in.read();
            ((Lifecycle) context).stop();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
