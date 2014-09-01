package com.jairoesc.simpleserver.simpleserver;

/**
 * Created by jairo on 1/09/14.
 */
public class ServerInfo {

    public String hostname;
    public String username;
    public String password;
    public String error;

    private boolean started;
    private String meminfo;
    private String cpuinfo;

    ServerInfo(String hostname, String username, String password) {

        this.hostname = hostname;
        this.username = username;
        this.password = password;

    }

    public boolean getStarted() {
        return this.started;
    }

    public String getMeminfo() {
        return this.meminfo;
    }

    public String getCpuinfo() {
        return this.cpuinfo;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public void setMeminfo(String meminfo) {
        this.meminfo = meminfo;
    }

    public void setCpuinfo(String cpuinfo) {
        this.cpuinfo = cpuinfo;
    }

}
