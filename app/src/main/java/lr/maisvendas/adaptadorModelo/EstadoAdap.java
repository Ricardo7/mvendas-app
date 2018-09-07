package lr.maisvendas.adaptadorModelo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import lr.maisvendas.modelo.Estado;

public class EstadoAdap {

    private Context context;

    public void EstadoAdap(Context context){
        this.context = context;
    }

    public Estado sqlToEstado(Cursor cursor){
        Estado estado = new Estado();

        estado.setId(cursor.getInt(cursor.getColumnIndex("ID")));
        estado.setDescricao(cursor.getString(cursor.getColumnIndex("DESCRICAO")));
        estado.setSigla(cursor.getString(cursor.getColumnIndex("SIGLA")));

        return estado;
    }

    public ContentValues estadoToContentValue(Estado estado){
        ContentValues content = new ContentValues();

        content.put("ID",estado.getId());
        content.put("DESCRICAO",estado.getDescricao());
        content.put("SIGLA",estado.getSigla());
        content.put("PAIS_ID",estado.getPais().getId());

        return content;
    }
}
