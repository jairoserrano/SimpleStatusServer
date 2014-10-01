package com.jairoesc.simpleserver.simpleserver;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by jairo on 12/09/14.
 */
public class ServerSQLiteHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 3;
    // Database Name
    private static final String DATABASE_NAME = "ServesDB";

    // ServerInfo table name
    private static final String TABLE_SERVER = "server";

    // ServerInfo Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_HOSTNAME = "hostname";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";

    static final String CREATE_SERVER_TABLE = "CREATE TABLE server ( " +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, hostname TEXT, " +
            "username TEXT, password TEXT )";

    private static final String[] COLUMNS = {KEY_ID, KEY_HOSTNAME, KEY_USERNAME, KEY_PASSWORD};

    public ServerSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create ServerInfo table
        db.execSQL(CREATE_SERVER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older ServerInfo table if existed
        db.execSQL("DROP TABLE IF EXISTS server");
        // create fresh ServerInfo table
        this.onCreate(db);
    }

    public long createServer(ServerData server) {

        Log.d("addServer", server.toString());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_HOSTNAME, server.getHostname());
        values.put(KEY_USERNAME, server.getUsername());
        values.put(KEY_PASSWORD, server.getPassword());

        // insert row
        long server_id = db.insert(TABLE_SERVER, null, values);

        return server_id;
    }

    public ServerData getServer(long id) {

        //UGLY UGLY HACK
        id++;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_SERVER, // a. table
                            COLUMNS, // b. column names
                            " id = ?", // c. selections
                            new String[]{String.valueOf(id)}, // d. selections args
                            null, // e. group by
                            null, // f. having
                            null, // g. order by
                            null); // h. limit

        ServerData server = new ServerData();
        if (cursor != null) {
            cursor.moveToFirst();
            Log.d("getServer(" + id + ")", cursor.toString());
            server.setId(Integer.parseInt(cursor.getString(0)));
            server.setHostname(cursor.getString(1));
            server.setUsername(cursor.getString(2));
            server.setPassword(cursor.getString(3));
            Log.d("getServer(" + id + ")", server.toString());
        }
        return server;
    }

    // Get All ServerInfo
    public List<ServerData> getAllServers() {
        List<ServerData> ServerData = new LinkedList<ServerData>();

        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_SERVER;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build ServerInfo and add it to list
        ServerData server = null;
        if (cursor.moveToFirst()) {
            do {
                server = new ServerData();
                server.setId(Integer.parseInt(cursor.getString(0)));
                server.setHostname(cursor.getString(1));
                server.setUsername(cursor.getString(2));
                server.setPassword(cursor.getString(3));

                // Add ServerInfo to ServerInfo
                ServerData.add(server);
            } while (cursor.moveToNext());
        }

        Log.d("getAllServers()", ServerData.toString());

        // return ServerInfo
        return ServerData;
    }

    // Updating single ServerInfo
    public int updateServerInfo(ServerData ServerData) {

        // Instancia de la DB
        SQLiteDatabase db = this.getWritableDatabase();

        // Actualización del registro
        ContentValues values = new ContentValues();
        values.put("hostname", ServerData.getHostname()); // get title
        values.put("username", ServerData.getUsername()); // get author
        values.put("password", ServerData.getPassword()); // get author

        // Actualizar el registro
        int i = db.update(TABLE_SERVER, //table
                values, // column/value
                KEY_ID + " = ?", // selections
                new String[]{String.valueOf(ServerData.getId())}); //selection args

        // 4. Cerrar la DB
        db.close();

        return i;

    }

    // Deleting single ServerInfo
    public void deleteServerInfo(ServerData ServerData) {

        // Instancia de la DB
        SQLiteDatabase db = this.getWritableDatabase();

        // Eliminar
        db.delete(TABLE_SERVER, KEY_ID + " = ?",
                new String[]{String.valueOf(ServerData.getId())});

        // Cerrar la DB
        db.close();

        Log.d("deleteServerInfo", ServerData.toString());

    }

}
