package ex10.realm;

import org.apache.catalina.Container;
import org.apache.catalina.Realm;
import org.apache.catalina.realm.GenericPrincipal;

import java.beans.PropertyChangeListener;
import java.security.Principal;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;

public class SimpleRealm implements Realm {

    private Container container;
    private ArrayList users = new ArrayList();

    public SimpleRealm() {
        createUserDatabase();
    }

    private void createUserDatabase() {
        User user1 = new User("ken", "blackcomb");
        user1.addRole("manager");
        user1.addRole("programmer");
        User user2 = new User("cindy", "bamboo");
        user2.addRole("programmer");

        users.add(user1);
        users.add(user2);
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
        return "A Simple Realm implementation";
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {

    }

    @Override
    public Principal authenticate(String userName, String credentials) {
        System.out.println("SimpleRealm.authenticate()");
        if (userName == null || credentials == null) {
            return null;
        }
        User user = getUser(userName, credentials);
        if (user == null) {
            return null;
        }
        return new GenericPrincipal(this, userName, credentials, user.getRoles());
    }

    private User getUser(String userName, String password) {
        Iterator iterator = users.iterator();
        while (iterator.hasNext()) {
            User user = (User) iterator.next();
            if (user.userName.equals(userName) && user.password.equals(password)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public Principal authenticate(String s, byte[] bytes) {
        return null;
    }

    @Override
    public Principal authenticate(String s, String s1, String s2, String s3, String s4, String s5, String s6, String s7) {
        return null;
    }

    @Override
    public Principal authenticate(X509Certificate[] x509Certificates) {
        return null;
    }

    @Override
    public boolean hasRole(Principal principal, String role) {
        if (principal == null || role == null || !(principal instanceof GenericPrincipal)) {
            return false;
        }
        GenericPrincipal gp = (GenericPrincipal) principal;
        if (!(gp.getRealm() == this)) {
            return false;
        }
        boolean result = gp.hasRole(role);
        return result;
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {

    }

    class User {
        private String userName;
        private String password;
        private ArrayList roles = new ArrayList();

        public User(String userName, String password) {
            this.userName = userName;
            this.password = password;
        }

        public void addRole(String role) {
            roles.add(role);
        }

        public ArrayList getRoles() {
            return roles;
        }
    }
}
