package com.jairoesc.simpleserver.simpleserver;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class ServerActivityAdd extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_add);
    }

    public void SaveServerClick(View v) {
        //Declaración de los campos de texto para extraer la información
        EditText etHostname = (EditText) findViewById(R.id.hostname);
        EditText etUsername = (EditText) findViewById(R.id.username);
        EditText etPassword = (EditText) findViewById(R.id.password);

        //Extracción de los datos
        String hostname = etHostname.getText().toString();
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        if (hostname.isEmpty()){
            //Mostrando mensaje de error
            etHostname.setError("Error");
        }else if (username.isEmpty()){
            //Mostrando mensaje de error
            etUsername.setError("Error");
        }else if (password.isEmpty()) {
            //Mostrando mensaje de error
            etPassword.setError("Error");
        }else{
            Log.v("AdicionarServidor", "Guardando");
            //abriendo enlace con la DB para guardar el servidor
            ServerSQLiteHelper db = new ServerSQLiteHelper(this);
            //Creación en la DB del hostna
            // me
            db.createServer(new ServerData(hostname, username, password));
            //forma correcta de cerrar una actividad.
            finish();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.adicionar_servidor, menu);
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

}
