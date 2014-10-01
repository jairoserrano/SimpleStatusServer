package com.jairoesc.simpleserver.simpleserver;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by jairo on 17/09/14.
 */
public class SSHConnection {

    ServerData _server;

    String _error;

    SSHConnection(ServerData server){

        this._server = server;
    }

    String CaptureOutput(String command) {

        String result = "";

        try {
            JSch jsch = new JSch();

            Session session = jsch.getSession(_server.getUsername(), _server.getHostname(), 22);
            session.setPassword(_server.getPassword());

            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);

            session.connect();
            Channel channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);
            channel.setInputStream(null);
            ((ChannelExec) channel).setErrStream(null);
            InputStream in = channel.getInputStream();

            channel.connect();
            byte[] tmp = new byte[1024];
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0) break;
                    result = new String(tmp, 0, i);
                }
                if (channel.isClosed()) {
                    if (in.available() > 0) continue;
                    set_error("exit-status: " + channel.getExitStatus());
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    set_error(e.getMessage());
                }
            }
            channel.disconnect();
            session.disconnect();
        } catch (JSchException e) {
            set_error(e.getMessage());
        } catch (IOException e) {
            set_error(e.getMessage());
        }
        return result;
    }

    public String get_error() {
        return _error;
    }

    public void set_error(String _error) {
        this._error += _error;
    }

}
