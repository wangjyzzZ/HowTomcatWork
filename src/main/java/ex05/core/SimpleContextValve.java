package ex05.core;

import org.apache.catalina.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SimpleContextValve implements Valve, Contained {
    protected Container container;

    @Override
    public void invoke(Request request, Response response, ValveContext valveContext) throws IOException, ServletException {
        if (!(request.getRequest() instanceof HttpServletRequest)
                || !(response.getResponse() instanceof HttpServletResponse)) {
            return;
        }

        // Disallow any direct access to resources under WEB-INF or META-INF
        HttpServletRequest httpServletRequest = (HttpServletRequest) request.getRequest();
        String contextPath = httpServletRequest.getContextPath();
        String requestUri = ((HttpRequest) request).getDecodedRequestURI();
        String relativeUri = requestUri.substring(contextPath.length()).toUpperCase();

        Context context = (Context) getContainer();
        // Select the Wrapper to be used for this Request
        Wrapper wrapper;
        try {
            wrapper = (Wrapper) context.map(request, true);
        } catch (IllegalArgumentException e) {
            badRequest(requestUri, (HttpServletResponse) response.getResponse());
            return;
        }
        if (wrapper == null) {
            notFound(requestUri, (HttpServletResponse) response.getResponse());
            return;
        }
        // Ask this Wrapper to process this Request
        response.setContext(context);
        wrapper.invoke(request, response);
    }

    private void badRequest(String requestURI, HttpServletResponse response) {
        try {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, requestURI);
        } catch (IllegalStateException e) {
            ;
        } catch (IOException e) {
            ;
        }
    }

    private void notFound(String requestURI, HttpServletResponse response) {
        try {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, requestURI);
        } catch (IllegalStateException e) {
            ;
        } catch (IOException e) {
            ;
        }
    }

    @Override
    public Container getContainer() {
        return container;
    }

    @Override
    public void setContainer(Container container) {
        this.container = container;
    }

    @Override
    public String getInfo() {
        return null;
    }
}
