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

    ServerInfo _info;

    SSHConnection(ServerInfo info){
        this._info = info;
    }

    ServerInfo CaptureOutput(String command) {
        try {
            JSch jsch = new JSch();

            Session session = jsch.getSession(_info.getUsername(), _info.getHostname(), 22);
            session.setPassword(_info.getPassword());

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
                    _info.setMemRaw(new String(tmp, 0, i));
                }
                if (channel.isClosed()) {
                    if (in.available() > 0) continue;
                    _info.setError("exit-status: " + channel.getExitStatus());
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    _info.setError(e.getMessage());
                }
            }
            channel.disconnect();
            session.disconnect();
        } catch (JSchException e) {
            _info.setError(e.getMessage());
        } catch (IOException e) {
            _info.setError(e.getMessage());
        }
        return _info;
    }
}
