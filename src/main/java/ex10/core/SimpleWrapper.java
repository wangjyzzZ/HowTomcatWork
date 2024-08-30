package ex10.core;

import org.apache.catalina.*;
import org.apache.catalina.util.LifecycleSupport;

import javax.naming.directory.DirContext;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import java.beans.PropertyChangeListener;
import java.io.IOException;

public class SimpleWrapper implements Wrapper, Pipeline, Lifecycle {
    private Servlet instance;
    private String servletClass;
    private Loader loader;
    private String name;
    private final SimplePipeline pipeline = new SimplePipeline(this);
    protected Container parent;
    protected LifecycleSupport lifecycleSupport = new LifecycleSupport(this);
    protected boolean started = false;

    public SimpleWrapper() {
        pipeline.setBasic(new SimpleWrapperValve());
    }

    @Override
    public Servlet allocate() throws ServletException {
        if (instance == null) {
            try {
                instance = loadServlet();
            } catch (ServletException e) {
                throw e;
            } catch (Throwable e) {
                throw new ServletException("Cannot allocate a servlet instance", e);
            }
        }
        return instance;
    }

    private Servlet loadServlet() throws ServletException {
        if (instance != null) {
            return instance;
        }

        Servlet servlet = null;
        String actualClass = servletClass;
        if (actualClass == null) {
            throw new ServletException("servlet class has not been specified");
        }

        Loader loader = getLoader();
        if (loader == null) {
            throw new ServletException("no loader");
        }
        ClassLoader classLoader = loader.getClassLoader();

        Class classClass = null;
        try {
            if (classLoader != null) {
                classClass = classLoader.loadClass(actualClass);
            }
        } catch (ClassNotFoundException e) {
            throw new ServletException("servlet class not found");
        }

        try {
            servlet = (Servlet) classClass.newInstance();
        } catch (Throwable e) {
            throw new ServletException("fail to instantiate servlet");
        }

        try {
            servlet.init(null);
        } catch (Throwable e) {
            throw new ServletException("fail to initialize servlet");
        }
        return servlet;
    }

    @Override
    public Loader getLoader() {
        if (loader != null) {
            return loader;
        }
        if (parent != null) {
            return parent.getLoader();
        }
        return null;
    }

    @Override
    public void invoke(Request request, Response response) throws IOException, ServletException {
        pipeline.invoke(request, response);
    }

    @Override
    public void addLifecycleListener(LifecycleListener lifecycleListener) {

    }

    @Override
    public void removeLifecycleListener(LifecycleListener lifecycleListener) {

    }

    @Override
    public void start() throws LifecycleException {
        System.out.println("Starting Wrapper " + name);
        if (started) {
            throw new LifecycleException("Wrapper already started");
        }
        started = true;

        // Start our subordinate components, if any
        if ((loader != null) && (loader instanceof Lifecycle)) {
            ((Lifecycle) loader).start();
        }

        // Start the Valves in our pipeline (including the basic), if any
        if (pipeline instanceof Lifecycle) {
            ((Lifecycle) pipeline).start();
        }

        // Notify our interested LifecycleListeners
        lifecycleSupport.fireLifecycleEvent(START_EVENT, null);
    }

    @Override
    public void stop() throws LifecycleException {
        System.out.println("Stopping wrapper " + name);
        // Shut down our servlet instance (if it has been initialized)
        try {
            instance.destroy();
        } catch (Throwable t) {
        }
        instance = null;
        if (!started) {
            throw new LifecycleException("Wrapper " + name + " not started");
        }

        // Notify our interested LifecycleListeners
        lifecycleSupport.fireLifecycleEvent(STOP_EVENT, null);
        started = false;

        // Stop the Valves in our pipeline (including the basic), if any
        if (pipeline instanceof Lifecycle) {
            ((Lifecycle) pipeline).stop();
        }

        // Stop our subordinate components, if any
        if ((loader != null) && (loader instanceof Lifecycle)) {
            ((Lifecycle) loader).stop();
        }

    }

    @Override
    public Valve getBasic() {
        return pipeline.getBasic();
    }

    @Override
    public void setBasic(Valve valve) {
        pipeline.setBasic(valve);
    }

    @Override
    public void addValve(Valve valve) {
        pipeline.addValve(valve);
    }

    @Override
    public Valve[] getValves() {
        return pipeline.getValves();
    }

    @Override
    public void removeValve(Valve valve) {
        pipeline.removeValve(valve);
    }

    @Override
    public long getAvailable() {
        return 0;
    }

    @Override
    public void setAvailable(long l) {

    }

    @Override
    public String getJspFile() {
        return null;
    }

    @Override
    public void setJspFile(String s) {

    }

    @Override
    public int getLoadOnStartup() {
        return 0;
    }

    @Override
    public void setLoadOnStartup(int i) {

    }

    @Override
    public String getRunAs() {
        return null;
    }

    @Override
    public void setRunAs(String s) {

    }

    @Override
    public String getServletClass() {
        return null;
    }

    @Override
    public void setServletClass(String s) {
        this.servletClass = s;
    }

    @Override
    public boolean isUnavailable() {
        return false;
    }

    @Override
    public void addInitParameter(String s, String s1) {

    }

    @Override
    public void addInstanceListener(InstanceListener instanceListener) {

    }

    @Override
    public void addSecurityReference(String s, String s1) {

    }

    @Override
    public void deallocate(Servlet servlet) throws ServletException {

    }

    @Override
    public String findInitParameter(String s) {
        return null;
    }

    @Override
    public String[] findInitParameters() {
        return new String[0];
    }

    @Override
    public String findSecurityReference(String s) {
        return null;
    }

    @Override
    public String[] findSecurityReferences() {
        return new String[0];
    }

    @Override
    public void load() throws ServletException {
        instance = loadServlet();
    }

    @Override
    public void removeInitParameter(String s) {

    }

    @Override
    public void removeInstanceListener(InstanceListener instanceListener) {

    }

    @Override
    public void removeSecurityReference(String s) {

    }

    @Override
    public void unavailable(UnavailableException e) {

    }

    @Override
    public void unload() throws ServletException {

    }

    @Override
    public String getInfo() {
        return null;
    }

    @Override
    public void setLoader(Loader loader) {
        this.loader = loader;
    }

    @Override
    public Logger getLogger() {
        return null;
    }

    @Override
    public void setLogger(Logger logger) {

    }

    @Override
    public Manager getManager() {
        return null;
    }

    @Override
    public void setManager(Manager manager) {

    }

    @Override
    public Cluster getCluster() {
        return null;
    }

    @Override
    public void setCluster(Cluster cluster) {

    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Container getParent() {
        return parent;
    }

    @Override
    public void setParent(Container container) {
        this.parent = container;
    }

    @Override
    public ClassLoader getParentClassLoader() {
        return null;
    }

    @Override
    public void setParentClassLoader(ClassLoader classLoader) {

    }

    @Override
    public Realm getRealm() {
        return null;
    }

    @Override
    public void setRealm(Realm realm) {

    }

    @Override
    public DirContext getResources() {
        return null;
    }

    @Override
    public void setResources(DirContext dirContext) {

    }

    @Override
    public void addChild(Container container) {

    }

    @Override
    public void addContainerListener(ContainerListener containerListener) {

    }

    @Override
    public void addMapper(Mapper mapper) {

    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {

    }

    @Override
    public Container findChild(String s) {
        return null;
    }

    @Override
    public Container[] findChildren() {
        return new Container[0];
    }

    @Override
    public Mapper findMapper(String s) {
        return null;
    }

    @Override
    public Mapper[] findMappers() {
        return new Mapper[0];
    }

    @Override
    public Container map(Request request, boolean b) {
        return null;
    }

    @Override
    public void removeChild(Container container) {

    }

    @Override
    public void removeContainerListener(ContainerListener containerListener) {

    }

    @Override
    public void removeMapper(Mapper mapper) {

    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {

    }
}
