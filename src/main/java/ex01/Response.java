package ex01;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Response {
    private static final int BUFFER_SIZE = 1024;

    private Request request;
    private final OutputStream outputStream;

    public Response(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void sendStaticResource() throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        FileInputStream fis = null;
        try {
            File file = new File(HttpServer.WEB_ROOT, request.getUri());
            if (file.exists()) {
                fis = new FileInputStream(file);
                int ch = fis.read(buffer, 0, BUFFER_SIZE);
                while (ch != -1) {
                    outputStream.write(buffer, 0, ch);
                    ch = fis.read(buffer, ch, BUFFER_SIZE);
                }
            } else {
                String errorMessage = "HTTP/1.1 404 File Not Found\r\n"
                        + "Content-Type: text/html\r\n"
                        + "Content-length: 23\r\n"
                        + "\r\n"
                        + "<h1>File Not Found</h1>";
                outputStream.write(errorMessage.getBytes());
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
    }

    public void setRequest(Request request) {
        this.request = request;
    }
}
