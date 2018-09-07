package lr.maisvendas.adaptadorModelo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import lr.maisvendas.modelo.SegmentoMercado;

public class SegmentoMercadoAdap {

    private Context context;

    public void SegmentoMercadoAdap(Context context){
        this.context = context;
    }

    public SegmentoMercado sqlToSegmentoMercado(Cursor cursor){
        SegmentoMercado segmentoMercado = new SegmentoMercado();

        segmentoMercado.setId(cursor.getInt(cursor.getColumnIndex("ID")));
        segmentoMercado.setDescricao(cursor.getString(cursor.getColumnIndex("DESCRICAO")));

        return segmentoMercado;
    }

    public ContentValues segmentoMercadoToContentValue(SegmentoMercado segmentoMercado){
        ContentValues content = new ContentValues();

        content.put("ID",segmentoMercado.getId());
        content.put("DESCRICAO",segmentoMercado.getDescricao());

        return content;
    }
}
