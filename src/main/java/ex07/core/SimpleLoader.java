package ex07.core;

import org.apache.catalina.*;

import java.beans.PropertyChangeListener;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;

public class SimpleLoader implements Loader, Lifecycle {
    public static final String WEB_ROOT = System.getProperty("user.dir")
            + File.separator + "src"
            + File.separator + "main"
            + File.separator + "java"
            + File.separator + "webroot";

    private ClassLoader classLoader;
    private Container container;

    public SimpleLoader() {
        try {
            URL[] urls = new URL[1];
            URLStreamHandler streamHandler = null;
//            File classPath = new File(WEB_ROOT);
//            String repository = (new URL("file", null, classPath.getCanonicalPath() + File.separator)).toString();
//            urls[0] = new URL(null, repository, streamHandler);
            urls[0] = new File(SimpleLoader.class.getResource("/webroot/").toURI()).toURI().toURL();
            classLoader = new URLClassLoader(urls);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public ClassLoader getClassLoader() {
        return classLoader;
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
    public boolean getDelegate() {
        return false;
    }

    @Override
    public void setDelegate(boolean b) {

    }

    @Override
    public String getInfo() {
        return null;
    }

    @Override
    public boolean getReloadable() {
        return false;
    }

    @Override
    public void setReloadable(boolean b) {

    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {

    }

    @Override
    public void addRepository(String s) {

    }

    @Override
    public String[] findRepositories() {
        return new String[0];
    }

    @Override
    public boolean modified() {
        return false;
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {

    }

    @Override
    public void addLifecycleListener(LifecycleListener lifecycleListener) {

    }

    @Override
    public void removeLifecycleListener(LifecycleListener lifecycleListener) {

    }

    @Override
    public void start() throws LifecycleException {
        System.out.println("Starting SimpleLoader");
    }

    @Override
    public void stop() throws LifecycleException {

    }
}
