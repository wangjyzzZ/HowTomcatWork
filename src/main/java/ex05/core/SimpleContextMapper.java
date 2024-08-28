package ex05.core;

import org.apache.catalina.*;

import javax.servlet.http.HttpServletRequest;

public class SimpleContextMapper implements Mapper {
    /**
     * The Container with which this Mapper is associated.
     */
    private SimpleContext context = null;

    @Override
    public Container getContainer() {
        return context;
    }

    @Override
    public void setContainer(Container container) {
        if (!(container instanceof SimpleContext)) {
            throw new IllegalArgumentException("Illegal type of container");
        }
        context = (SimpleContext) container;
    }

    @Override
    public String getProtocol() {
        return null;
    }

    @Override
    public void setProtocol(String s) {

    }

    @Override
    public Container map(Request request, boolean b) {
        // Identify the context-relative URI to be mapped
        String contextPath = ((HttpServletRequest) request.getRequest()).getContextPath();
        String requestURI = ((HttpRequest) request).getDecodedRequestURI();
        String relativeURI = requestURI.substring(contextPath.length());
        // Apply the standard request URI mapping rules from the specification
        Wrapper wrapper = null;
        String servletPath = relativeURI;
        String pathInfo = null;
        String name = context.findServletMapping(relativeURI);
        if (name != null) {
            wrapper = (Wrapper) context.findChild(name);
        }
        return wrapper;
    }
}
