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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ServerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);

        ServerSQLiteHelper db = new ServerSQLiteHelper(this);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        List<ServerInfo> list = db.getAllServers();

        ArrayAdapter<ServerInfo> dataAdapter = new ArrayAdapter<ServerInfo>
                (this, android.R.layout.simple_spinner_item,list);

        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(dataAdapter);


    }
    public void ConnectClick(View v) {
        //ServerInfo info = new ServerInfo("","","");
        //new openSSHConnection().execute(info);
        Intent intent = new Intent(getApplicationContext(), AdicionarServidor.class);
        startActivityForResult(intent,RESULT_OK);
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
            Intent intent = new Intent(getApplicationContext(), AdicionarServidor.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private class openSSHConnection extends AsyncTask<ServerInfo, Void, ServerInfo> {

        @Override
        protected ServerInfo doInBackground(ServerInfo... info) {
            SSHConnection conn = new SSHConnection(info[0]);
            info[0] = conn.CaptureOutput("free -m");
            return info[0];
        }

        protected void onPostExecute(ServerInfo info) {
            //WebView webview = (WebView) findViewById(R.id.webView);
            String content = "<html>"
                    + "  <head>"
                    + "    <script type='text/javascript' src='https://www.google.com/jsapi'></script>"
                    + "    <script type=\"text/javascript\">"
                    + "         google.load('visualization', '1', {packages:['corechart']});"
                    + "         google.setOnLoadCallback(drawChart);"
                    + "         function drawChart() { "
                    + "             var data = google.visualization.arrayToDataTable([ "
                    + "                     ['Memoria', 'en KBs'],"
                    + "                     ['Total', " + info.getMemTotal() + "], "
                    + "                     ['Libre', " + info.getMemFree() + "], "
                    + "                     ['Cache', "   + info.getMemCache() + "] "
                    + "                 ]); "
                    + "             var options = { "
                    + "                     legend: 'none', "
                    + "                     pieSliceText: 'label', "
                    + "                     title: 'Cantidad de memoria libre en el servidor', "
                    + "                     pieStartAngle: 100, "
                    + "                 }; "
                    + "             var chart = new google.visualization.PieChart(document.getElementById('piechart')); "
                    + "    </script>"
                    + "  </head>"
                    + "  <body>"
                    + "    <div id='piechart' style='width: 49%; height: 100%; float:left;'></div>"
                    + "  </body>" + "</html>";
            Log.v("proceso",content);
            //WebSettings webSettings = webview.getSettings();
            //webSettings.setJavaScriptEnabled(true);
            //webview.requestFocusFromTouch();
            //webview.loadDataWithBaseURL( "file:///android_asset/", content, "text/html", "utf-8", null );
        }

    }

}
