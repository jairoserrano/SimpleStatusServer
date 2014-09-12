package com.jairoesc.simpleserver.simpleserver;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.IOException;
import java.io.InputStream;

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

        btnConnect = (Button) findViewById(R.id.button);
        hostname = (EditText) findViewById(R.id.edthostname);
        username = (EditText) findViewById(R.id.edtusername);
        password = (EditText) findViewById(R.id.edtpassword);

    }
    public void ConnectClick(View v) {
        ServerInfo info = new ServerInfo(
                hostname.getText().toString(),
                username.getText().toString(),
                password.getText().toString()
        );
        new openSSHConnection().execute(info);
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
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private class openSSHConnection extends AsyncTask<ServerInfo, Void, ServerInfo> {

        @Override
        protected ServerInfo doInBackground(ServerInfo... info) {

            try {
                JSch jsch = new JSch();

                Session session = jsch.getSession(info[0].getUsername(), info[0].getHostname(), 22);
                session.setPassword(info[0].getPassword());

                java.util.Properties config = new java.util.Properties();
                config.put("StrictHostKeyChecking", "no");
                session.setConfig(config);

                session.connect();
                Channel channel = session.openChannel("exec");
                ((ChannelExec) channel).setCommand("free -m");
                channel.setInputStream(null);
                ((ChannelExec) channel).setErrStream(null);
                InputStream in = channel.getInputStream();

                channel.connect();
                byte[] tmp = new byte[1024];
                while (true) {
                    while (in.available() > 0) {
                        int i = in.read(tmp, 0, 1024);
                        if (i < 0) break;
                        info[0].setMeminfo(new String(tmp, 0, i));
                    }
                    if (channel.isClosed()) {
                        if (in.available() > 0) continue;
                        info[0].setError("exit-status: " + channel.getExitStatus());
                        break;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (Exception ee) {
                    }
                }
                channel.disconnect();
                session.disconnect();
            } catch (JSchException e) {
                info[0].setError(e.getMessage());
            } catch (IOException e) {
                info[0].setError(e.getMessage());
            }
            return info[0];
        }

        protected void onPostExecute(ServerInfo info) {
            WebView webview = (WebView) findViewById(R.id.webView);
            Double[] prc = info.getMeminfo();
            String content = "<html>"
                    + "  <head>"
                    + "    <script type='text/javascript' src='https://www.google.com/jsapi'></script>"
                    + "    <script type=\"text/javascript\">"
                    + "         google.load('visualization', '1', {packages:['corechart']});"
                    + "         google.setOnLoadCallback(drawChart);"
                    + "         function drawChart() { "
                    + "             var data = google.visualization.arrayToDataTable([ "
                    + "                     ['Memoria', 'en KBs'],"
                    + "                     ['Total', " + prc[0].toString() + "], "
                    + "                     ['Libre', " + prc[1].toString() + "], "
                    + "                     ['Cacheada', "   + prc[2].toString() + "] "
                    + "                 ]); "
                    + "             var options = { "
                    + "                     legend: 'none', "
                    + "                     pieSliceText: 'label', "
                    + "                     title: 'Cantidad de memoria libre en el servidor', "
                    + "                     pieStartAngle: 100, "
                    + "                 }; "
                    + "             var chart = new google.visualization.PieChart(document.getElementById('piechart')); "
                    + "             chart.draw(data, options); "
                    + "             var data2 = google.visualization.arrayToDataTable([ "
                    + "                     ['CPU', 'en KBs'],"
                    + "                     ['Total', " + prc[0].toString() + "], "
                    + "                     ['Libre', " + prc[1].toString() + "], "
                    + "                     ['Cacheada', "   + prc[2].toString() + "] "
                    + "                 ]); "
                    + "             var options2 = { "
                    + "                     legend: 'none', "
                    + "                     pieSliceText: 'label', "
                    + "                     title: 'Uso de la CPU', "
                    + "                     pieStartAngle: 100, "
                    + "                 }; "
                    + "             var chart2 = new google.visualization.PieChart(document.getElementById('piechart2')); "
                    + "             chart2.draw(data2, options2); "                    + "          } "
                    + "    </script>"
                    + "  </head>"
                    + "  <body>"
                    + "    <div id='piechart' style='width: 49%; height: 100%; float:left;'></div>"
                    + "    <div id='piechart2' style='width: 49%; height: 100%; float:left;'></div>"
                    + "  </body>" + "</html>";
            Log.v("proceso",content);
            WebSettings webSettings = webview.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webview.requestFocusFromTouch();
            webview.loadDataWithBaseURL( "file:///android_asset/", content, "text/html", "utf-8", null );
        }

    }


}
