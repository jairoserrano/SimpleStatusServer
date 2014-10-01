package com.jairoesc.simpleserver.simpleserver;

/**
 * Created by jairo on 1/09/14.
 */
public class ServerData {

    private Integer id;
    private String hostname;
    private String username;
    private String password;

    private String error;
    private String memRaw;
    private String memFree;
    private String memTotal;
    private String memCache;
    private String cpuRaw;
    private String cpuTotal;
    private String cpuUsage;

    public String getUptime() {
        return uptime;
    }

    public void setUptime(String uptime) {
        this.uptime = uptime;
    }

    private String uptime;

    private boolean hostStatus;

    public ServerData(String _hostname, String _username, String _password) {
        setHostname(_hostname);
        setUsername(_username);
        setPassword(_password);
    }

    ServerData() {
        setHostname("");
        setUsername("");
        setPassword("");
        setMemFree("");
        setMemTotal("");
        setMemCache("");
        setCpuTotal("");
        setCpuUsage("");
    }

    public Integer getId() {

        return id;
    }

    public void setId(Integer id) {

        this.id = id;
    }

    public void setPassword(String password) {

        this.password = password;
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

        return this.toMem(memFree);
    }

    public void setMemFree(String memFree) {

        this.memFree = memFree;
    }

    public String getMemCache() {

        return this.toMem(this.memCache);
    }

    public void setMemCache(String memCache) {

        this.memCache = memCache;
    }

    public String getMemTotal() {

        return this.toMem(memTotal);
    }

    public void setMemTotal(String memTotal) {

        this.memTotal = memTotal;
    }

    public String getHostname() {

        return hostname;
    }

    public String toMem(String mem){

        Double tmp = Double.parseDouble(mem);

        if(tmp>1000){
            tmp = tmp/1024;
            return String.format("%.2f",tmp)+" GB";
        }else{
            return String.format("%.2f",tmp)+" MB";
        }
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

    public String getPassword() {

        return password;
    }
}
