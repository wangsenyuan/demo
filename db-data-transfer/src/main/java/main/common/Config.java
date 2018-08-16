package main.common;

/**
 * the db config
 */
public class Config {
    private String url;
    private String user;
    private String password;

    public static Config example() {
        Config config = new Config();
        config.setUrl("jdbc:postgresql://s4.maycur.cc:5432/maycur-pro");
        config.setUser("abc");
        config.setPassword("def");
        return config;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
