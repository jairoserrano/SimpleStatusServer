package com.jairoesc.simpleserver.simpleserver;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
                HostnameInformation data = dbinfo.getServer(1);

                Log.v("demo", data.getUsername());

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
        List<HostnameInformation> list = db.getAllServers();
        if (list.isEmpty()) {
            spinner.setVisibility(View.INVISIBLE);
            tvConnectTo.setVisibility(View.INVISIBLE);
        } else {
            ArrayAdapter<HostnameInformation> dataAdapter = new ArrayAdapter<HostnameInformation>
                    (this, android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(dataAdapter);
        }
    }

    public void ConnectClick(View v) {
        //ServerInfo info = new ServerInfo("","","");
        //new openSSHConnection().execute(info);
        Intent intent = new Intent(getApplicationContext(), AddServerToDB.class);
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
            Intent intent = new Intent(getApplicationContext(), AddServerToDB.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private class openSSHConnection extends AsyncTask<HostnameInformation, Void, HostnameInformation> {

        @Override
        protected HostnameInformation doInBackground(HostnameInformation... info) {
            SSHConnection conn = new SSHConnection(info[0]);
            info[0] = conn.CaptureOutput("free -m");
            return info[0];
        }

        protected void onPostExecute(ServerData info) {
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
