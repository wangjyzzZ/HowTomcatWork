package ex05.startup;

import ex05.core.SimpleContext;
import ex05.core.SimpleContextMapper;
import ex05.core.SimpleLoader;
import ex05.core.SimpleWrapper;
import ex05.valves.ClientIPLoggerValve;
import ex05.valves.HeaderLoggerValve;
import org.apache.catalina.*;
import org.apache.catalina.connector.http.HttpConnector;

public class Bootstrap2 {

    public void start() {
        HttpConnector connector = new HttpConnector();
        Wrapper wrapper1 = new SimpleWrapper();
        wrapper1.setName("Primitive");
        wrapper1.setServletClass("webroot.PrimitiveServlet");
        Wrapper wrapper2 = new SimpleWrapper();
        wrapper2.setName("Modern");
        wrapper2.setServletClass("webroot.ModernServlet");

        Context context = new SimpleContext();
        context.addChild(wrapper1);
        context.addChild(wrapper2);

        Valve valve1 = new HeaderLoggerValve();
        Valve valve2 = new ClientIPLoggerValve();

        ((Pipeline) context).addValve(valve1);
        ((Pipeline) context).addValve(valve2);

        Mapper mapper = new SimpleContextMapper();
        mapper.setProtocol("http");
        context.addMapper(mapper);
        Loader loader = new SimpleLoader();
        context.setLoader(loader);
        // context.addServletMapping(pattern, name);
        context.addServletMapping("/Primitive", "Primitive");
        context.addServletMapping("/Modern", "Modern");
        connector.setContainer(context);
        try {
            connector.initialize();
            connector.start();

            // make the application wait until we press a key.
            System.in.read();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
