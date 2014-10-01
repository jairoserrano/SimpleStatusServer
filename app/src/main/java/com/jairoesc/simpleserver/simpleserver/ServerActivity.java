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
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.List;

public class ServerActivity extends Activity {

    @Override
    protected void onPostResume() {
        super.onPostResume();
        this.fillSpinner();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);
        this.fillSpinner();
        TableLayout infopanel = (TableLayout) findViewById(R.id.infopanel);
        //infopanel.setVisibility(View.INVISIBLE);
    }

    protected void fillSpinner() {
        ServerSQLiteHelper db = new ServerSQLiteHelper(this);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        TextView tvConnectTo = (TextView) findViewById(R.id.tvConnectTo);
        List<ServerData> list = db.getAllServers();
        if (list.isEmpty()) {
            spinner.setVisibility(View.INVISIBLE);
            tvConnectTo.setVisibility(View.INVISIBLE);
        } else {
            ArrayAdapter<ServerData> dataAdapter = new ArrayAdapter<ServerData>
                    (this, android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(dataAdapter);
        }
    }

    public void ConnectClick(View v) {
        /*Intent intent = new Intent(getApplicationContext(), ServerActivityAdd.class);
        startActivity(intent);*/

        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        Log.v("demo", "aja esa era " + spinner.getSelectedItemId());

        ServerSQLiteHelper dbinfo = new ServerSQLiteHelper(getBaseContext());
        ServerData data = dbinfo.getServer(spinner.getSelectedItemId());

        Log.v("demo", data.toString());

        new openSSHConnection().execute(data);

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

    private class openSSHConnection extends AsyncTask<ServerData, Integer, ServerData> {

        @Override
        protected ServerData doInBackground(ServerData... info) {
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
            publishProgress(10);
            SSHConnection conn = new SSHConnection(info[0]);
            info[0].setMemRaw(conn.CaptureOutput("free -m"));
            info[0].setUptime(conn.CaptureOutput("uptime"));
            publishProgress(40);
            return info[0];
        }

        protected void onPostExecute(ServerData info) {

            publishProgress(50);

            TextView memtotal_tx = (TextView) findViewById(R.id.memtotal_tx);
            TextView memfree_tx = (TextView) findViewById(R.id.memfree_tx);
            TextView uptime_tx = (TextView) findViewById(R.id.uptime_tx);
            //TextView cpu_tx = (TextView) findViewById(R.id.cpu_tx);

            publishProgress(70);

            memtotal_tx.setText(info.getMemTotal());
            memfree_tx.setText(info.getMemFree());
            uptime_tx.setText(info.getUptime());
            info.getMemCache();

            publishProgress(90);

            TableLayout infopanel = (TableLayout) findViewById(R.id.infopanel);
            infopanel.setVisibility(View.VISIBLE);
            publishProgress(100);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
            //Log.v("Progreso",values[0].toString());
            progressBar.setProgress(values[0]);

        }
    }

}
