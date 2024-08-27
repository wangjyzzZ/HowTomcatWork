package ex03.startup;

import ex03.connector.http.HttpConnector;

public class Bootstrap {

    public void start() {
        HttpConnector connector = new HttpConnector();
        connector.start();
    }
}
