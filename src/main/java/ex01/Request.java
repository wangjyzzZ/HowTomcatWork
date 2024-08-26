package ex01;

import java.io.InputStream;

public class Request {
    private final InputStream inputStream;
    private String uri;

    public Request(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public void parse() {
        StringBuffer sb = new StringBuffer(2048);
        byte[] buffer = new byte[2048];
        int i;
        try {
            i = inputStream.read(buffer);
        } catch (Exception e) {
            e.printStackTrace();
            i = -1;
        }
        for (int j = 0; j < i; j++) {
            sb.append((char) buffer[j]);
        }
        System.out.println(sb);
        this.uri = parseUri(sb.toString());
    }

    private String parseUri(String request) {
        int index1, index2;
        index1 = request.indexOf(" ");
        if (index1 != -1) {
            index2 = request.indexOf(" ", index1 + 1);
            if (index2 > index1) {
                return request.substring(index1 + 1, index2);
            }
        }
        return null;
    }

    public String getUri() {
        return uri;
    }
}
