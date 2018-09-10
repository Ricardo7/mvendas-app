package lr.maisvendas.adaptadorModelo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import lr.maisvendas.modelo.Dispositivo;

public class DispositivoAdap {

    private Context context;

    public void DispositivoAdap(Context context){
        this.context = context;
    }

    public Dispositivo sqlToDispositivo(Cursor cursor){
        Dispositivo dispositivo = new Dispositivo();

        dispositivo.setId(cursor.getInt(cursor.getColumnIndex("ID")));
        dispositivo.setDataSincronizacao(cursor.getString(cursor.getColumnIndex("DATA_SINCRONIZACAO")));

        return dispositivo;
    }

    public ContentValues dispositivoToContentValue(Dispositivo dispositivo){
        ContentValues content = new ContentValues();

        content.put("ID",dispositivo.getId());
        content.put("DATA_SINCRONIZACAO",dispositivo.getDataSincronizacao());

        return content;
    }
}
