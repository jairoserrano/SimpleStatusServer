package com.jairoesc.simpleserver.simpleserver;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.view.View.OnClickListener;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.IOUtils;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.transport.verification.ConsoleKnownHostsVerifier;
import net.schmizz.sshj.transport.verification.OpenSSHKnownHosts;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;


public class ServerActivity extends Activity {

    private TextView logOut;
    private Button   btnConnect;
    private EditText hostname;
    private EditText username;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);

        logOut = (TextView) findViewById(R.id.textLog);
        btnConnect = (Button) findViewById(R.id.button);
        hostname = (EditText) findViewById(R.id.edthostname);
        username = (EditText) findViewById(R.id.edtusername);
        password = (EditText) findViewById(R.id.edtpassword);

        OnClickListener oclBtnOk = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServerInfo info = new ServerInfo(
                        hostname.getText().toString(),
                        username.getText().toString(),
                        password.getText().toString()
                );
                new openSSHConnection().execute(info);

            }
        };
        btnConnect.setOnClickListener(oclBtnOk);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.server, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class openSSHConnection extends AsyncTask<ServerInfo, Void, ServerInfo> {

        private Switch   logStarted;
        private TextView logMeminfo;
        private TextView logCpuinfo;

        @Override
        protected ServerInfo doInBackground(ServerInfo... info) {

            try {
                final SSHClient client = new SSHClient();
                //Adicionar el host
                final File khFile = new File(getFilesDir(), "known_hosts");
                client.addHostKeyVerifier(new ConsoleKnownHostsVerifier(khFile, System.console()));

                client.connect(info[0].hostname);
                try {
                    client.authPassword(info[0].username, info[0].password);
                    final Session session = client.startSession();
                    try {
                        final Session.Command cmd = session.exec("free");
                        info[0].setStarted(true);
                        String tmp1 = IOUtils.readFully(cmd.getInputStream()).toString();
                        Log.d("infoServer",tmp1);
                        info[0].setMeminfo(tmp1);
                        cmd.join(5, TimeUnit.SECONDS);
                        String tmp2 = "\n** exit status: " + cmd.getExitStatus();
                        Log.d("infoServer",tmp2);
                        info[0].setCpuinfo(tmp2);
                    } finally {
                        session.close();
                    }
                } finally {
                    client.disconnect();
                }
            } catch (IOException e) {
                info[0].error = e.getMessage();
            }
            return info[0];
        }

        protected void onPostExecute(ServerInfo info) {

            logStarted = (Switch) findViewById(R.id.logStarted);
            logMeminfo = (TextView) findViewById(R.id.logMeminfo);
            logCpuinfo = (TextView) findViewById(R.id.logCpuinfo);

            logOut.setText("Conectando");
            logStarted.setChecked(info.getStarted());
            logMeminfo.setText(info.getMeminfo());
            logCpuinfo.setText(info.error);
        }

    }


}
