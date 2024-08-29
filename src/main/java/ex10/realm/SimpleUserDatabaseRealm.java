package ex10.realm;

import org.apache.catalina.realm.RealmBase;
import org.apache.catalina.startup.UserDatabase;

import java.security.Principal;

public class SimpleUserDatabaseRealm extends RealmBase {

    protected UserDatabase database = null;
    protected static final String name = "SimpleUserDatabaseRealm";

    protected String resourceName = "UserDatabase";

    public void createDatabase(String path) {
        database = new MemoryUserDatabase(name);
        ((MemoryUserDatabase) database).setPathname(path);
        try {
            database.open();
        }
        catch (Exception e)  {
        }
    }

    @Override
    protected String getName() {
        return null;
    }

    @Override
    protected String getPassword(String s) {
        return null;
    }

    @Override
    protected Principal getPrincipal(String s) {
        return null;
    }
}
