package com.jairoesc.simpleserver.simpleserver;

/**
 * Created by jairo on 1/09/14.
 */
public class ServerDataPrivate {

    private Integer id;
    protected String hostname;
    protected String username;
    private String password;

    public ServerDataPrivate(String hostname, String username, String password) {
        setHostname(hostname);
        setUsername(username);
        setPassword(password);
    }

    public ServerDataPrivate() {
    }

    @Override
    public String toString() {
        return this.username+"@"+this.hostname;
    }

    public Integer getId() {

        return id;
    }

    public void setId(Integer id) {

       this.id = id;
    }

    public String getHostname() {

        return hostname;
    }

    public void setHostname(String hostname) {

        this.hostname = hostname;
    }

    public String getPassword() {

        return password;
    }

    public void setPassword(String password) {

        this.password = password;
    }

    public String getUsername() {

        return username;
    }

    public void setUsername(String username) {

        this.username = username;
    }

}
