package com.example.fanlynk;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SLQliteConexion extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "fanlink.db";
    private static final int DATABASE_VERSION = 1;

    // Table and column names
    private static final String TABLE_USUARIO = "usuario";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NOMBRE = "nombre";
    private static final String COLUMN_CORREO_ELECTRONICO = "correo_electronico";
    private static final String COLUMN_CONTRASENA = "contrasena";

    private SQLiteDatabase db;

    public SLQliteConexion(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear tabla usuarios
        String CREATE_USUARIO_TABLE = "CREATE TABLE " + TABLE_USUARIO + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NOMBRE + " TEXT,"
                + COLUMN_CORREO_ELECTRONICO + " TEXT,"
                + COLUMN_CONTRASENA + " TEXT" + ")";
        db.execSQL(CREATE_USUARIO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USUARIO);
        onCreate(db);
    }

    // Método para abrir la base de datos
    public void open() {
        if (db == null || !db.isOpen()) {
            db = this.getWritableDatabase();
        }
    }

    // Método para cerrar la base de datos
    public void close() {
        if (db != null && db.isOpen()) {
            db.close();
        }
    }

    // Insertar datos
    public long addUser(String nombre, String correoElectronico, String contrasena) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOMBRE, nombre);
        values.put(COLUMN_CORREO_ELECTRONICO, correoElectronico);
        values.put(COLUMN_CONTRASENA, contrasena);

        // Insertar fila
        return db.insert(TABLE_USUARIO, null, values);
    }

    // Revisar si existe el dato, usando el correo
    public boolean checkUser(String correoElectronico) {
        String[] columns = { COLUMN_ID };
        String selection = COLUMN_CORREO_ELECTRONICO + " = ?";
        String[] selectionArgs = { correoElectronico };

        Cursor cursor = db.query(TABLE_USUARIO, columns, selection, selectionArgs, null, null, null);
        int cursorCount = cursor.getCount();
        cursor.close();
        return cursorCount > 0;
    }
}



