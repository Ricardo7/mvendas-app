package lr.maisvendas.adaptadorModelo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import lr.maisvendas.modelo.Pais;

public class PaisAdap {

    private Context context;

    public void PaisAdap(Context context){
        this.context = context;
    }

    public Pais sqlToPais(Cursor cursor){
        Pais pais = new Pais();

        pais.setId(cursor.getInt(cursor.getColumnIndex("ID")));
        pais.setIdWS(cursor.getInt(cursor.getColumnIndex("ID_WS")));
        pais.setDescricao(cursor.getString(cursor.getColumnIndex("DESCRICAO")));
        pais.setSigla(cursor.getString(cursor.getColumnIndex("SIGLA")));

        return pais;
    }

    public ContentValues paisToContentValue(Pais pais){
        ContentValues content = new ContentValues();

        content.put("ID",pais.getId());
        content.put("ID_WS",pais.getIdWS());
        content.put("DESCRICAO",pais.getDescricao());
        content.put("SIGLA",pais.getSigla());

        return content;
    }
}
