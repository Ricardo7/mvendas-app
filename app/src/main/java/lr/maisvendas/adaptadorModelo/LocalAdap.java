package lr.maisvendas.adaptadorModelo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import lr.maisvendas.modelo.Local;

public class LocalAdap {

    private Context context;

    public void LocalAdap(Context context){
        this.context = context;
    }

    public Local sqlToLocal(Cursor cursor){
        Local local = new Local();

        /*private Integer id;
    private Double latitude;
    private Double longitude;
    private Cliente cliente;
    private String dtCriacao;
    private String dtAtualizacao;*/

        return local;
    }

    public ContentValues localToContentValue(Local local){
        ContentValues content = new ContentValues();

        /*"ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "LATITUDE BLOB NOT NULL, " +
            "LONGITUDE BLOB NOT NULL, " +
            "CLIENTE_ID INTEGER," +
            "DT_CRIACAO TEXT, " +
            "DT_ATUALIZACAO TEXT);";*/

        return content;
    }
}
