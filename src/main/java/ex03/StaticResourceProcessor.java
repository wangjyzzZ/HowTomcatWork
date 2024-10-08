package ex03;

import ex03.connector.http.HttpRequest;
import ex03.connector.http.HttpResponse;

import java.io.IOException;

public class StaticResourceProcessor {
    public void process(HttpRequest request, HttpResponse response) {
        try {
            response.sendStaticResource();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
