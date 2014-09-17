package com.jairoesc.simpleserver.simpleserver;

/**
 * Created by jairo on 1/09/14.
 */
public class ServerInfo {

    private Integer id;
    private String hostname;
    private String username;
    private String password;
    private String error;
    private String cpuinfo;
    private Double[] meminfo;

    private boolean started;

    ServerInfo(String hostname, String username, String password) {
        this.hostname = hostname;
        this.username = username;
        this.password = password;
        this.meminfo = new Double[3];
    }

    ServerInfo() { }

    @Override
    public String toString() {
        return "Server [id=" + id + ", hostname=" + this.hostname + ", username=" + this.username
                + ", password=" + this.password + "]";
    }

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

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

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error += error;
    }

    public boolean getStarted() {
        return this.started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public Double[] getMeminfo() {
        return this.meminfo;
    }

    public void setMeminfo(String rawMeminfo) {
        String[] linea = rawMeminfo.split("\n");

        linea[1] = linea[1].replaceAll("\\s+", " ");
        String[] linea_exp = linea[1].split(" ");
        //memoria Total
        this.meminfo[0] = Double.parseDouble(linea_exp[1].toString().trim());
        //memoria libre real
        this.meminfo[1] = Double.parseDouble(linea_exp[3].toString().trim());
        //memoria cacheada
        this.meminfo[2] = Double.parseDouble(linea_exp[6].toString().trim());

    }

    public String getCpuinfo() {
        return this.cpuinfo;
    }

    public void setCpuinfo(String cpuinfo) {
        this.cpuinfo = cpuinfo;
    }

}
