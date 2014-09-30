package com.jairoesc.simpleserver.simpleserver;

/**
 * Created by jairo on 1/09/14.
 */
public class ServerData extends ServerDataPrivate {

    private String error;
    private String memRaw;
    private String memFree;
    private String memTotal;
    private String memCache;
    private String cpuRaw;
    private String cpuTotal;
    private String cpuUsage;

    private boolean hostStatus;

    ServerData() {
        setMemFree("");
        setMemTotal("");
        setMemCache("");
        setCpuTotal("");
        setCpuUsage("");
    }

    @Override
    public String toString() {

        return this.username+"@"+this.hostname;
    }

    public boolean isHostStatus() {

        return hostStatus;
    }

    public void setHostStatus(boolean hostStatus) {
        this.hostStatus = hostStatus;
    }

    public String getMemRaw() {

        return memRaw;
    }

    public void setMemRaw(String memRaw) {
        String[] linea = memRaw.split("\n");
        linea[1] = linea[1].replaceAll("\\s+", " ");
        String[] linea_exp = linea[1].split(" ");

        setMemTotal(linea_exp[1].toString().trim());
        setMemFree(linea_exp[3].toString().trim());
        setMemCache(linea_exp[6].toString().trim());
    }

    public String getMemFree() {

        return memFree;
    }

    public void setMemFree(String memFree) {

        this.memFree = memFree;
    }

    public String getMemCache() {

        return memCache;
    }

    public void setMemCache(String memCache) {

        this.memCache = memCache;
    }

    public String getMemTotal() {

        return memTotal;
    }

    public void setMemTotal(String memTotal) {
        this.memTotal = memTotal;
    }

    public String getHostname() {

        return hostname;
    }

    public void setHostname(String hostname) {

        this.hostname = hostname;
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

    public String getCpuUsage() {

        return cpuUsage;
    }

    public void setCpuUsage(String cpuUsage) {

        this.cpuUsage = cpuUsage;
    }

    public String getCpuRaw() {

        return cpuRaw;
    }

    public void setCpuRaw(String cpuRaw) {

        this.cpuRaw = cpuRaw;
    }

    public String getCpuTotal() {

        return cpuTotal;
    }

    public void setCpuTotal(String cpuTotal) {

        this.cpuTotal = cpuTotal;
    }

    @Override
    public String getPassword() {
        return null;
    }
}
