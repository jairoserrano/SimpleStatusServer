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
            "id INTEGER PRIMARY KEY AUTOINCREMENT, hostname TEXT, "+
            "username TEXT, password TEXT )";

    private static final String[] COLUMNS = {KEY_ID,KEY_HOSTNAME,KEY_USERNAME,KEY_PASSWORD};

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

    public long createServer(ServerInfo server) {

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

    public ServerInfo getServer(int id){

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(TABLE_SERVER, // a. table
                        COLUMNS, // b. column names
                        " id = ?", // c. selections
                        new String[] { String.valueOf(id) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();

        // 4. build ServerInfo object
        ServerInfo server = new ServerInfo();
        server.setId(Integer.parseInt(cursor.getString(0)));
        server.setHostname(cursor.getString(1));
        server.setUsername(cursor.getString(2));
        server.setPassword(cursor.getString(3));

        //log
        Log.d("getServer("+id+")", server.toString());

        // 5. return ServerInfo
        return server;
    }

    // Get All ServerInfo
    public List<ServerInfo> getAllServers() {
        List<ServerInfo> ServerInfo = new LinkedList<ServerInfo>();

        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_SERVER;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build ServerInfo and add it to list
        ServerInfo server = null;
        if (cursor.moveToFirst()) {
            do {
                server = new ServerInfo();
                server.setId(Integer.parseInt(cursor.getString(0)));
                server.setHostname(cursor.getString(1));
                server.setUsername(cursor.getString(2));
                server.setPassword(cursor.getString(3));
                
                // Add ServerInfo to ServerInfo
                ServerInfo.add(server);
            } while (cursor.moveToNext());
        }

        Log.d("getAllServers()", ServerInfo.toString());

        // return ServerInfo
        return ServerInfo;
    }

    // Updating single ServerInfo
    public int updateServerInfo(ServerInfo ServerInfo) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put("hostname", ServerInfo.getHostname()); // get title
        values.put("username", ServerInfo.getUsername()); // get author
        values.put("password", ServerInfo.getPassword()); // get author

        // 3. updating row
        int i = db.update(TABLE_SERVER, //table
                values, // column/value
                KEY_ID+" = ?", // selections
                new String[] { String.valueOf(ServerInfo.getId()) }); //selection args

        // 4. close
        db.close();

        return i;

    }

    // Deleting single ServerInfo
    public void deleteServerInfo(ServerInfo ServerInfo) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. delete
        db.delete(TABLE_SERVER,
                KEY_ID+" = ?",
                new String[] { String.valueOf(ServerInfo.getId()) });

        // 3. close
        db.close();

        Log.d("deleteServerInfo", ServerInfo.toString());

    }

}
