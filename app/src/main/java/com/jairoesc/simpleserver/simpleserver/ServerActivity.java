package com.jairoesc.simpleserver.simpleserver;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

public class ServerActivity extends Activity {
    @Override
    protected void onResume() {
        super.onResume();
        this.fillSpinner();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);
        this.fillSpinner();

        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.v("demo", "aja esa era " + i + " " + adapterView.getSelectedItemId());

                ServerSQLiteHelper dbinfo = new ServerSQLiteHelper(getBaseContext());
                ServerDataPrivate data = dbinfo.getServer(0);

                Log.v("demo", data.getUsername());

                new openSSHConnection().execute(data);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    protected void fillSpinner() {
        ServerSQLiteHelper db = new ServerSQLiteHelper(this);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        TextView tvConnectTo = (TextView) findViewById(R.id.tvConnectTo);
        WebView webview = (WebView) findViewById(R.id.webView);
        List<ServerDataPrivate> list = db.getAllServers();
        if (list.isEmpty()) {
            spinner.setVisibility(View.INVISIBLE);
            tvConnectTo.setVisibility(View.INVISIBLE);
            webview.setVisibility(View.INVISIBLE);
        } else {
            ArrayAdapter<ServerDataPrivate> dataAdapter = new ArrayAdapter<ServerDataPrivate>
                    (this, android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(dataAdapter);
        }
    }

    public void AddHostClick(View v) {
        Intent intent = new Intent(getApplicationContext(), ServerActivityAdd.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.server, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(getApplicationContext(), ServerActivityAdd.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private class openSSHConnection extends AsyncTask<ServerDataPrivate, Void, ServerDataPrivate> {

        @Override
        protected ServerDataPrivate doInBackground(ServerDataPrivate... info) {
            SSHConnection conn = new SSHConnection(info[0]);
            info[0] = conn.CaptureOutput("free -m");
            return info[0];
        }

        protected void onPostExecute(ServerData info) {
            WebView webview = (WebView) findViewById(R.id.webView);
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
            WebSettings webSettings = webview.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webview.requestFocusFromTouch();
            webview.loadDataWithBaseURL( "file:///android_asset/", content, "text/html", "utf-8", null );
        }

    }

}
