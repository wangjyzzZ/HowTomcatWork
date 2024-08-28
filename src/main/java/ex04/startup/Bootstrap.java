package ex04.startup;

import ex04.core.SimpleContainer;
import org.apache.catalina.connector.http.HttpConnector;

public class Bootstrap {

    public void start() {
        HttpConnector connector = new HttpConnector();
        SimpleContainer container = new SimpleContainer();
        connector.setContainer(container);
        try {
            connector.initialize();
            connector.start();

            System.in.read();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
